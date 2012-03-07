package com.HappyChat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.jdesktop.application.ResourceManager;

import com.HappyChat.entity.Message;
import com.HappyChat.entity.Request;
import com.HappyChat.entity.RequestType;
import com.HappyChat.entity.Response;
import com.HappyChat.entity.User;
import com.HappyChat.frame.ChatRoom;
import com.HappyChat.frame.Login;
import com.HappyChat.frame.Register;
import com.HappyChat.util.ScreenCapture;

/**
 * �ͻ���д��
 * */
public class ClientThread extends Thread {
	private ObjectOutputStream oOutput;
	private ObjectInputStream oInput;//
	private Response response;// ���Է������˵���Ӧ
	private Request request;// ����
	private Socket socket;// �ͻ��˶���
	private User user;// ��ǰ�û�
	List<User> onLine = new ArrayList<User>();
	List<Message> messages = new ArrayList<Message>();
	Object obj;// ����м����

	private StyledDocument document = null;// �ĵ�����
	private SimpleAttributeSet attributes = new SimpleAttributeSet();// ���õ���ʽ

	private Register register;// ע�����
	private ChatRoom chatRoom;// �������
	private Login login;// ��¼����

	String ipAddress = "127.0.0.1";// Ĭ�Ϸ�����ip
	Integer port = 5200;// Ĭ�϶˿�

	/**
	 * ��ȡ���ݴ���
	 * */
	int readID = -1;

	public ClientThread() {
		// ��ʾ��¼����
		login = new Login();
		login.txtIPAddress.setText(ipAddress);
		login.txtPort.setText(port.toString());
		login.setTitle("��¼");
		login.setResizable(false);
		login.txtName.requestFocus();// ����Ϊ���� Ҫ���ڣ�show(),setVisible(true)�����ĺ���
		LoadLoginFrame();// ���ص�¼�¼�
	}

	// ////////////////////////////////���ؿͻ����¼�//////////////////////////////////////////////////////
	/**
	 * ����Login���� ���¼����¼�
	 * */
	public void LoadLoginFrame() {
		// Login�ĵ�¼�¼�
		this.login.btnLogin.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				// �ж������Ƿ�Ϸ�
				String Name = login.txtName.getText();
				String Password = login.pdfPassword.getText();
				if (login.txtIPAddress.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "�����������ip");
					login.txtIPAddress.requestFocus();
				} else if (login.txtPort.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "�����������port");
					login.txtPort.requestFocus();
				} else if (Name.equals("")) {
					JOptionPane.showMessageDialog(null, "�������û���");
					login.txtName.requestFocus();
				} else if (Password.equals("")) {
					JOptionPane.showMessageDialog(null, "����������");
					login.pdfPassword.requestFocus();
				} else {
					// ��¼��֤
					user = new User(Name, Password, 0, null);
					if (Login(user)) {
						// ��֤ͨ��
						chatRoom = new ChatRoom();//
						// ���ص�¼�û��б�
						ShowOnlineUserToClient();
						chatRoom.txtpEditerMessage.requestFocus();
						LoadChatRoom();// ���ص�¼����
						chatRoom.setTitle("����������[�û�:" + Name + "]");
						login.setVisible(false);// ���ص�¼����
						// ����������������
						start();
					}
				}
			}

		});

		// ��ע�����
		this.login.btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ipAddress = login.txtIPAddress.getText().trim();// ���»�ȡip
				register = new Register();
				LoadRegister();// ����ע������¼�
			}
		});
	}

	/**
	 * ����ChatRoom���� ���¼����¼�
	 * */
	private void LoadChatRoom() {
		document = (StyledDocument) chatRoom.txtPMessage.getDocument();// �õ�����
		this.chatRoom.addWindowListener(new WindowListener() {

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				Exit(user);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		/**
		 * �˳�
		 * */
		this.chatRoom.btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Exit(user);
			}
		});
		// ������Ϣ
		chatRoom.btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (user.getName().equals(
						chatRoom.combUser.getSelectedItem().toString())) {
					JOptionPane.showMessageDialog(null, "�벻Ҫ��������");
					chatRoom.txtpEditerMessage.setText("");
					chatRoom.txtpEditerMessage.requestFocus();
					return;
				}
				if (chatRoom.txtpEditerMessage.getText().length() > 250) {
					JOptionPane.showMessageDialog(null, "��������ַ�����250������ض�");
					chatRoom.txtpEditerMessage.setText("");
					chatRoom.txtpEditerMessage.requestFocus();
					return;
				}
				if (chatRoom.txtpEditerMessage.getText().length() < 1) {
					JOptionPane.showMessageDialog(null, "�벻Ҫ���Ϳ���Ϣ");
					chatRoom.txtpEditerMessage.setText("");
					chatRoom.txtpEditerMessage.requestFocus();
					return;
				}
				Message me = new Message(user, new User(chatRoom.combUser
						.getSelectedItem().toString(), null, 0, null), null,
						new Date());
				me.setFont_message(chatRoom.txtpEditerMessage.getFont());
				me.setColor(chatRoom.txtpEditerMessage.getForeground());
				ArrayList<Icon> icons = new ArrayList<Icon>();
				for (int i = 0; i < chatRoom.txtpEditerMessage
						.getStyledDocument().getRootElements()[0].getElement(0)
						.getElementCount(); i++) {
					Icon icon = StyleConstants
							.getIcon(chatRoom.txtpEditerMessage
									.getStyledDocument().getRootElements()[0]
									.getElement(0).getElement(i)
									.getAttributes());
					if (icon != null) {
						icons.add(icon);
					}
				}
				me.setMessages(chatRoom.txtpEditerMessage.getStyledDocument());// �ѱ༭����document����message��
				me.setIcons(icons);
				SendMessage(me);
				chatRoom.txtpEditerMessage.setText(null);
				chatRoom.txtpEditerMessage.requestFocus();
			}
		});

		// ץ��
		chatRoom.btnCatchScreen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File tempFile = new File("d:", (new Date()).getHours() + ""
						+ (new Date()).getMinutes() + ""
						+ (new Date()).getSeconds() + "_message.jpg");
				ScreenCapture capture = ScreenCapture.getInstance();// ScreenCapture�����ڼ����ϵ�PrintScreen���Ĺ���
				capture.captureImage();
				chatRoom.txtpEditerMessage.insertIcon(capture.getPickedIcon());
				try {
					capture.saveToFile(tempFile);
				} catch (IOException e1) {

				}
				capture.captureImage();

			}
		});
		// ��ѡ�е������û���ӵ�����������˵���
		// ���û�ǰ���������û���ͷ��
		chatRoom.listOnlineUser
				.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						Object obj_u = chatRoom.listOnlineUser
								.getSelectedValue();
						boolean flag = false;
						int count = chatRoom.combUser.getItemCount();

						for (int i = 0; i < count; i++) {
							if (chatRoom.combUser.getItemAt(i).equals(obj_u)) {
								flag = true;
								break;
							}
						}
						if (obj_u == null) {
							return;
						}
						// ���
						if (!flag) {
							chatRoom.combUser.addItem(obj_u);
						}
						chatRoom.combUser.setSelectedItem(obj_u);
					}
				});
	}

	/**
	 * ����Register���� ���¼����¼�
	 * */
	private void LoadRegister() {
		// ȡ��ע��
		this.register.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				register.setVisible(false);
			}
		});
		// ע��
		this.register.btnRegister.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent event) {
				// ��֤���ݵĺϷ���
				String name = register.txtName.getText();
				String password = register.pdfPassword.getText();
				String rePassword = register.pdfRePassword.getText();
				String email = register.txtEmail.getText();
				String age = register.txtAge.getText();
				int ages = 0;

				boolean flag = false;
				String errorMessage = "";
				if (name.equals("")) {
					flag = true;
					errorMessage = "�������û���";
				} else if (password.equals("")) {
					flag = true;
					errorMessage = "����������";
				} else if (rePassword.equals("")) {
					flag = true;
					errorMessage = "������ȷ������";
				} else if (!rePassword.equals(password)) {
					flag = true;
					errorMessage = "������������ȷ�����벻һ�� ��";
				} else if (age.equals("")) {
					flag = true;
					errorMessage = "����������";
				} else if (email.equals("")) {
					flag = true;
					errorMessage = "����������ʼ����������һ�����";
				} else if (!email
						.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
					flag = true;
					errorMessage = "�����ʼ���ʽ����ȷ!";
				} else {
					flag = false;
				}
				if (flag) {
					JOptionPane.showMessageDialog(register, errorMessage);
				} else {
					try {
						ages = Integer.parseInt(age);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(register, "������������ʽ����ȷ");
						return;
					}
					if (ages > 220 || ages < 0) {
						JOptionPane.showMessageDialog(register, "����������䲻��ȷ");
						return;
					}

					Register(new User(name, password, ages, email));
				}
			}
		});
	}

	/**
	 * ˢ�������û��б�
	 */
	private void ShowOnlineUserToClient() {
		String[] strs = new String[50];
		for (int i = 0; i < onLine.size(); i++) {
			strs[i] = onLine.get(i).getName();
		}
		ListModel listm = new DefaultComboBoxModel(strs);
		chatRoom.listOnlineUser.setModel(listm);
	}

	// ///////////////////////////////����������/////////////////////////////////////////////////
	public void run() {// �½�һ��Socket���� �����������һ���յ�message ��ȡ�������ϵ�OnlineList ��
		// Messages
		while (true) {
			SendMessage(new Message(user, new User(chatRoom.combUser
					.getSelectedItem().toString(), null, 0, null), null,
					new Date()));
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * ��ʾ������Ϣ
	 */
	private void ShowMessage(Message me) {
		int end = chatRoom.txtPMessage.getSelectionEnd();// ��ȡ��textArea��ĩβ
		if (!"".equals(me)) {
			String message = me.getFrom().getName() + "("
					+ me.getDate().toLocaleString() + ")��"
					+ me.getTo().getName() + "˵:\n" + me.getMessages() + "\n";
			chatRoom.txtPMessage.setText(message);
		}
	}

	/**
	 * ��ʾϵͳ��Ϣ
	 */
	private void ShowMessage(String str) {
		int end = chatRoom.txtPMessage.getSelectionEnd();// ��ȡ��textArea��ĩβ
		if (!"".equals(str)) {
			chatRoom.InsertMessage(str + "\n");
		}
	}

	/**
	 * ע���¼� ע�� ok
	 * */
	public void Register(User user) {
		request = new Request(user, RequestType.register);
		try {
			Socket socket_r = new Socket(ipAddress, port);
			ObjectOutputStream output = new ObjectOutputStream(socket_r
					.getOutputStream());
			output.writeObject(request);// ����ע����Ϣ

			ObjectInputStream input = new ObjectInputStream(socket_r
					.getInputStream());
			response = (Response) input.readObject();// ������Ϣ

			JOptionPane.showMessageDialog(null, (response.isFlag() ? "ע��ɹ�!"
					: response.getInfo()));

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "���ӷ�����ʧ��");
			return;
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "���ӷ�����ʧ��");
			return;
		}
	}

	/**
	 * ��¼ �¼� ok
	 * */
	public boolean Login(User user) {
		boolean isPass = false;
		port = Integer.parseInt(login.txtPort.getText().trim());
		ipAddress = login.txtIPAddress.getText().trim();
		request = new Request(user, RequestType.login);
		try {
			socket = new Socket(ipAddress, port);// ��ȡһ��������

			oOutput = new ObjectOutputStream(socket.getOutputStream());
			oOutput.writeObject(request);// �����û���������

			oInput = new ObjectInputStream(socket.getInputStream());
			response = (Response) oInput.readObject();// ������Ӧ

		} catch (NumberFormatException e1) {
		} catch (UnknownHostException e1) {
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "���ӷ�����ʧ��");
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		isPass = response.isFlag();// �Ƿ�ͨ��
		if (!isPass) {
			JOptionPane.showMessageDialog(null, response.getInfo());// û��ͨ������ʾ��Ӧ�Ĵ�����Ϣ
		} else {
			onLine = (List) response.getOnLine();// ��ȡ�����û�
		}
		try {
			oOutput.close();
			oInput.close();
			socket.close();
		} catch (IOException e) {

		}
		return isPass;
	}

	/**
	 * ������Ϣ �¼� ����Ϣ��ŵ������� ��ȡ���е�������Ϣ ���ж�
	 * */
	public void SendMessage(Message message) {
		request = new Request(message, RequestType.sendMessage);
		try {
			Socket socket_m = new Socket(ipAddress, port);

			ObjectOutputStream output = new ObjectOutputStream(socket_m
					.getOutputStream());

			output.writeObject(request);// ������Ϣ

			ObjectInputStream input = new ObjectInputStream(socket_m
					.getInputStream());
			Response response = (Response) input.readObject();// ��ȡ����������Ӧ
			boolean isConnect = false;// �û��б����Ƿ�������û�
			for (User online_u : response.getOnLine()) {
				if (online_u.getName().equals(user.getName())) {
					isConnect = true;
					break;
				}
			}
			if (!isConnect) {
				JOptionPane.showMessageDialog(null, "�㱻����Ա����");
				System.exit(0);
			}
			// �ж��Ƿ����û��˳������
			for (User u_Server : response.getOnLine()) {
				boolean isFind = false;
				for (User u_client : onLine) {
					if (u_Server.getName().equals(u_client.getName())) {
						isFind = true;
						break;
					}
				}
				if (!isFind) {
					// ��ʾUser����
					ShowMessage(u_Server.getName() + "����");
				}
			}
			for (User u_client : onLine) {
				boolean isFind = false;
				for (User u_Server : response.getOnLine()) {
					if (u_Server.getName().equals(u_client.getName())) {
						isFind = true;
						break;
					}
				}
				if (!isFind) {
					// ��ʾUser����
					ShowMessage(u_client.getName() + "����");
				}
			}
			this.onLine = response.getOnLine();

			// ˢ�µ�¼�û��б�
			ShowOnlineUserToClient();
			// ��ȡ��Ϣ
			messages = (List<Message>) response.getInfo();
			// ����ǵ�һ�ζ�ȡ��Ϣ �������û���ȡ����Ϣ���ε�
			if (readID == -1) {
				readID = messages.size();
			}
			// ��ȡ��Ϣ
			for (int i = readID; i < messages.size(); i++) {
				// ���ô�����Ϣ����ʾ��ʽ
				chatRoom.createStyle(messages.get(i).getFont_message()
						.getSize(), messages.get(i).getFont_message()
						.getStyle(), 0, messages.get(i).getColor(), messages
						.get(i).getFont_message().getFamily());
				// �ж��Ƿ���ϵͳ��Ϣ
				if (messages.get(i).getServerMessage() != null) {
					chatRoom.InsertMessage("ϵͳ��Ϣ:"
							+ messages.get(i).getServerMessage() + "\n");
				} else {
					StyledDocument sDoc = (StyledDocument) messages.get(i)
							.getMessages();// ����Ϣǿ��ת��
					ArrayList<Icon> icons = messages.get(i).getIcons();
					int k = 0;
					chatRoom
							.InsertMessage(messages.get(i).getFrom().getName()
									+ "("
									+ messages.get(i).getDate()
											.toLocaleString() + ")��"
									+ messages.get(i).getTo().getName()
									+ "˵:\n");// 
					if (sDoc != null) {
						for (int j = 0; j < sDoc.getLength(); j++) {
							// �ж��Ƿ���icon����
							if ("icon".equals(sDoc.getCharacterElement(j)
									.getName())) {// ��ʾͼƬ
								chatRoom.InsertIcon(icons.get(k++));
							} else {// ��ʾ��Ϣ
								chatRoom.InsertMessage(sDoc.getText(j, 1));
							}
						}
					}
					chatRoom.InsertMessage("\n");// ����
				}
				readID++;
			}
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}
			if (socket_m != null) {
				socket_m.close();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "���ӷ�����ʧ�ܣ�");
			System.exit(0);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "���ӷ�����ʧ�ܣ�");
			System.exit(0);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(null, "��������ʧ�ܣ�");
			System.exit(0);
		}
	}

	/**
	 * �˳�
	 * 
	 * @param user
	 *            �˳��û�
	 * @return �Ƿ��˳�
	 */
	public boolean Exit(User user) {
		this.suspend();// �߳�����
		int number = JOptionPane.showConfirmDialog(null, "�Ƿ��˳�", "�˳���ʾ",
				JOptionPane.YES_NO_OPTION);
		if (number == 1) {
			this.resume();// �߳�����
			return false;
		}
		boolean flag = false;
		for (User u : onLine) {
			if (u.getName().equals(user.getName())) {
				user = u;
				break;
			}
		}
		request = new Request(user, RequestType.exit);
		try {
			Socket s = new Socket(ipAddress, port);

			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(request);

			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			Response res = (Response) in.readObject();

			if (res.isFlag()) {
				JOptionPane.showMessageDialog(null, "�˳��ɹ���");
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(null, "�˳�ʧ�ܣ�");
			}

			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (s != null) {
				s.close();
			}
		} catch (UnknownHostException e1) {
		} catch (IOException e1) {
		} catch (ClassNotFoundException e) {
		}
		return true;
	}
}
