package com.HappyChat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import com.HappyChat.entity.Message;
import com.HappyChat.entity.SavePoint;
import com.HappyChat.entity.User;
import com.HappyChat.frame.ServerFrame;
import com.HappyChat.util.Dao;
import com.HappyChat.util.FileDAO;

public class Server extends Thread {

	/**
	 * ������
	 */
	private ServerSocket serverSocket;
	/**
	 * ������ߵ��û�
	 */
	private static List<User> OnlineList = new ArrayList<User>();
	/**
	 * ������е���ʱ������Ϣ
	 */
	public static List<Message> Messages = new ArrayList<Message>();
	/**
	 * ������
	 */
	ObjectInputStream oInput;
	/**
	 * �����
	 */
	ObjectOutputStream oOutput;
	/**
	 * ���������
	 */
	ServerFrame serverFrame;
	/**
	 * ��������ַ
	 */
	InetAddress address;
	/**
	 * ����λ��, Ĭ��ΪFile
	 */
	private static SavePoint SavePlace = SavePoint.File;

	public static SavePoint getSavePlace() {
		return SavePlace;
	}

	public static void setSavePlace(SavePoint savePlace) {
		SavePlace = savePlace;
	}

	public Server() {

	}

	public Server(Integer port) {
		try {
			address = InetAddress.getLocalHost();
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "����������ʧ��!");
			System.exit(0);
		}
		LoadServer(address, port);
		this.start();
	}

	/**
	 * ���ط�����
	 * */
	private void LoadServer(InetAddress address, Integer port) {
		serverFrame = new ServerFrame();
		serverFrame.txtServerIP.setText(address.getHostAddress());
		serverFrame.txtPort.setText(port.toString());
		serverFrame.txtServiceName.setText(address.getHostName());
		serverFrame.txtState.setText("������....");
		// ����ϵͳ��Ϣ
		serverFrame.btnSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String serverMessage = serverFrame.txtpEditerServerMessage
						.getText();
				if ("".equals(serverMessage)) {
					JOptionPane.showMessageDialog(null, "�벻Ҫ���Ϳ���Ϣ!");
					return;
				}
				Message m = new Message(new User("ϵͳ", null, 0, null),
						new User("�����û�", null, 0, null), null, new Date());
				m.setServerMessage(serverMessage);
				Messages.add(m);
				// �����������
				WriteMessage(m);
				// ѡ�񱣴�λ��
				if (SavePoint.File.equals(SavePlace)) {
					FileDAO fdao = new FileDAO();
					m.setFrom(fdao.SelectUser(m.getFrom().getName()));
					m.setTo(fdao.SelectUser(m.getTo().getName()));
					fdao.SaveMessage(m);
				} else if (SavePoint.Oracle.equals(SavePlace)) {
					// ��ѯ�����û���id
					Dao dao = new Dao();
					m.setFrom(dao.SelectUser(m.getFrom().getName()));
					m.setTo(dao.SelectUser(m.getTo().getName()));
					dao.SaveMessage(m);
				}
				serverFrame.txtpEditerServerMessage.setText("");
			}

			/**
			 * ��ʾϵͳ��Ϣ
			 * 
			 * @param me
			 */
			private void WriteMessage(Message me) {
				if (me != null) {
					serverFrame.InsertMessage(me.getFrom().getName() + ":"
							+ me.getServerMessage() + "\n", null);
				}
			}
		});
		// ����
		serverFrame.btnTI.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (serverFrame.listOnlineUser.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "��ѡ���û�");
					return;
				}
				TiUser(new User(serverFrame.listOnlineUser.getSelectedValue()
						.toString(), null, 0, null));
			}
		});
		// ������־
		serverFrame.btnSaveLog.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					String str = serverFrame.txtpLogs.getText();
					(new FileDAO()).SaveLogs(str);
					JOptionPane.showMessageDialog(null,
							"��־������HapplyChatLog.txt");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "��־����ʧ��!");
				}
			}
		});
		// �رշ�����
		serverFrame.btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		// ѡ�񱣴�λ��
		// ������û���¼������ѡ��
		serverFrame.cmbSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (OnlineList.size() != 0) {
					JOptionPane.showMessageDialog(null, "���û��Ѿ���¼���޷�������ѡ��!");
					serverFrame.cmbSave.setSelectedItem(SavePlace.toString());
					return;
				}
				Object obj = serverFrame.cmbSave.getSelectedItem();// ��ȡѡ�е�ֵ
				if (obj.equals("File")) {
					SavePlace = SavePoint.File;
				} else if (obj.equals("Oracle")) {
					// �������� ������ӳɹ���ѡ��Oracle����ѡ��File
					if ((new Dao()).getConnection() != null) {
						JOptionPane.showMessageDialog(null, "Oracle���ӳɹ�!");
						SavePlace = SavePoint.Oracle;
					} else {
						JOptionPane.showMessageDialog(null, "Oracle����ʧ��!");
						SavePlace = SavePoint.File;
						serverFrame.cmbSave.setSelectedIndex(0);
					}
				}
			}
		});
	}

	/**
	 * �����ͻ������󣬵����û�����ʱ���� ServerThread�߳�
	 */
	public void run() {
		try {
			while (true) {
				Socket socket = serverSocket.accept();// �ȴ�����
				new ServerThread(serverFrame, socket, OnlineList, Messages);
			}
		} catch (IOException e) {
			System.out.println("һ���û��˳�");
		}
	}

	/**
	 * @param user
	 *            ���û�
	 */
	public void TiUser(User user) {
		for (User user_on : OnlineList) {
			if (user_on.getName().equals(user.getName())) {
				OnlineList.remove(user_on);
				WriteLogs(user.getName() + "������");
				break;
			}
		}
		serverFrame.txtOnline.setText(OnlineList.size() + "��");
	}

	/**
	 * @param string
	 *            ��־��Ϣ--------------
	 */
	private void WriteLogs(String string) {
		serverFrame.InsertLogs(string + "\n");
	}

	/**
	 * @return the onlineList
	 */
	public static List<User> getOnlineList() {
		return OnlineList;
	}

	/**
	 * @param onlineList
	 *            the onlineList to set
	 */
	public static void setOnlineList(List<User> onlineList) {
		OnlineList = onlineList;
	}

	public static void main(String[] args) {
		new Server(5200);
	}

}
