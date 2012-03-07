package com.HappyChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.HappyChat.entity.Message;
import com.HappyChat.entity.Request;
import com.HappyChat.entity.RequestType;
import com.HappyChat.entity.Response;
import com.HappyChat.entity.SavePoint;
import com.HappyChat.entity.User;
import com.HappyChat.frame.ServerFrame;
import com.HappyChat.util.Dao;
import com.HappyChat.util.FileDAO;

/**
 * ������ ����������� ��ÿһ���ͻ��˵���Ϣ���д���
 * */
/**
 * @author Administrator
 * 
 */
public class ServerThread extends Thread {
	/**
	 * ��ͻ���ͨ��Socket
	 * */
	private Socket socket;
	/**
	 * �ͻ��˵���������������
	 * */
	private ObjectInputStream oInput;
	/**
	 * ���������ͻ��˵������
	 * */
	private ObjectOutputStream oOutput;
	/**
	 * ��ǰ�û�
	 * */
	private User user;
	/**
	 * ���������û�
	 * */
	List<User> OnlineList;
	/**
	 * ��������û��������ʱ��Ϣ
	 * */
	List<Message> messages;
	/**
	 * ��Ӧ���ͣ�������ŷ��ظ��û�����Ϣ
	 * */
	private Response response;
	/**
	 * ����������
	 * */
	private ServerFrame serverFrame;

	public ServerThread() {

	}

	public ServerThread(ServerFrame serverFrame, Socket socket,
			List<User> OnlineList, List<Message> messages) {

		this.serverFrame = serverFrame;
		this.socket = socket;
		this.OnlineList = OnlineList;
		this.messages = messages;
		this.start();
	}

	/**
	 * ����ͻ���������߳�
	 * */
	public void run() {
		try {
			this.oInput = new ObjectInputStream(socket.getInputStream());
			this.oOutput = new ObjectOutputStream(socket.getOutputStream());
			Object o = oInput.readObject();
			GetRequest(o);// �ж���������
		} catch (IOException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
		}
		try {
			if (this.oInput != null) {
				this.oInput.close();
			}
			if (this.oOutput != null) {
				this.oOutput.close();
			}
			if (this.socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			System.out.println("��ȡ�������");
		}
	}

	/**
	 * ����obj�����ͣ�������ִ��ʲô���� ������ɺ󷵻�һ��response
	 * */
	public void GetRequest(Object obj) {
		// obj��ֻ��һ������:Request
		Request request = (Request) obj;
		if (request.getRt().equals(RequestType.login)) {
			// ���obj��login��ִ�е�¼��֤
			Login(request);
		} else if (request.getRt().equals(RequestType.register)) {
			// ע��
			Register(request);
		} else if (request.getRt().equals(RequestType.sendMessage)) {
			// ������Ϣ
			SendMessage(request);
		} else if (request.getRt().equals(RequestType.exit)) {
			// �˳�
			ClientExit(request);
		} else {
			// ��ȡ����ʧ��
			response = new Response(RequestType.sendMessage, false, OnlineList,
					null);
		}
		LoadOnlineUser();
	}

	/**
	 * �û������˳� ok
	 * */
	private void ClientExit(Request request) {
		// ������Ϣ�������û� ��ʾ���û�����
		response = new Response(RequestType.exit, true, OnlineList, request
				.getObj());

		// ��OnlineList��ɾ�����û�
		for (int i = 0; i < Server.getOnlineList().size(); i++) {
			if (Server.getOnlineList().get(i).getName().equals(
					((User) request.getObj()).getName())) {
				Server.getOnlineList().remove(i);
				break;
			}
		}
		WriteLogs(((User) (request.getObj())).getName() + "�˳���");
		serverFrame.txtOnline.setText(Server.getOnlineList().size() + "��");
		// ���ظ��û�
		try {
			oOutput.writeObject(response);
			oOutput.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 *������Ϣ ok
	 **/
	private void SendMessage(Request request) {
		Message message = (Message) request.getObj();// ת��Ϊmessage����
		if (message.getMessages() != null) {
			// ��ӵ��б�
			messages.add(message);
			// �����������
			WriteMessage(message);
			// ѡ�񱣴�λ��
			if (SavePoint.File.equals(Server.getSavePlace())) {
				FileDAO fdao = new FileDAO();
				message.setFrom(fdao.SelectUser(message.getFrom().getName()));
				message.setTo(fdao.SelectUser(message.getTo().getName()));
				fdao.SaveMessage(message);
			} else if (SavePoint.Oracle.equals(Server.getSavePlace())) {
				// ��ѯ�����û���id
				Dao dao = new Dao();
				message.setFrom(dao.SelectUser(message.getFrom().getName()));
				message.setTo(dao.SelectUser(message.getTo().getName()));
				dao.SaveMessage(message);
			}
			serverFrame.txtpUserMessage.selectAll();// �����������
		}
		response = new Response(RequestType.sendMessage, true, OnlineList,
				messages);
		// ���ظ��û�
		try {
			oOutput.writeObject(response);
			oOutput.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * @param request
	 *            ��¼����
	 */
	public void Login(Request request) {
		User user = (User) request.getObj();// ת��ΪUser����
		response = new Response(RequestType.login, false, OnlineList, user);
		// �ж��û��Ƿ����(���ݿ�)���жϴ��û��Ƿ��¼
		if (IsExist(user) && !IsInList(user)) {
			response.setFlag(true);
			// �����¼�ɹ�����
			this.OnlineList.add(user);
			response.setOnLine(OnlineList);
			// ϵͳ��ʾ�û���¼��Ϣ
			WriteLogs(user.getName() + "��¼�ˣ�ʱ��:"
					+ (new Date()).toLocaleString());
		} else if (!IsExist(user)) {
			response.setInfo("�����ڸ��û�������ע��");
		} else if (OnlineList.size() >= 50) {
			response.setInfo("��¼����̫�࣬���Ժ�..");
		} else {
			response.setInfo("���û��Ѿ���¼!");
		}
		try {
			oOutput.writeObject(response);
			oInput.close();
		} catch (IOException e) {
			System.out.println("��������¼����ʧ��!");
		}
	}

	/**
	 * ��֤�û��Ƿ��ѵ�¼
	 * 
	 * @param user
	 *            ��Ҫ��֤���û�
	 * @return �Ƿ���ڸ��û�
	 */
	private boolean IsInList(User user) {
		boolean flag = false;
		for (User u : OnlineList) {
			if (u.getName().equals(user.getName())) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 *ע��
	 **/
	public void Register(Request request) {
		User user = (User) request.getObj();// ת��ΪUser����
		response = new Response(RequestType.register, false, OnlineList, user);
		// �ж��û��Ƿ����
		if (!IsExist(user)) {
			boolean isRegister = false;
			// ѡ�񱣴�λ��
			if (SavePoint.File.equals(Server.getSavePlace())) {
				isRegister = (new FileDAO()).Register(user);
			} else if (SavePoint.Oracle.equals(Server.getSavePlace())) {
				isRegister = (new Dao()).Register(user);
			}
			response.setFlag(isRegister);
			response.setInfo(isRegister ? "�ɹ�" : "ʧ��");
			// ��ʾע����־
			WriteLogs(user.getName() + "ע��" + (isRegister ? "�ɹ�" : "ʧ��")
					+ ",ʱ��:" + (new Date()).toLocaleString());
		} else {
			response.setInfo("�û��Ѵ��ڣ�������ѡ��");
		}
		try {
			oOutput.writeObject(response);
			oOutput.close();
			oInput.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * �ж����ݿ����Ƿ���ڴ��û�
	 * 
	 * @param user
	 * @return
	 */
	private boolean IsExist(User user) {
		return SavePoint.Oracle.equals(Server.getSavePlace()) ? (new Dao())
				.IsExist(user) : (new FileDAO()).IsExist(user);
	}

	/**
	 * д��־---------------------
	 * */
	public void WriteLogs(String logs) {
		serverFrame.InsertLogs(logs + "\n");
	}

	/**
	 * ��������ʾ������Ϣ
	 * */
	public void WriteMessage(Message me) {
		if (me != null) {
			// ���ô�����Ϣ����ʾ��ʽ
			serverFrame.createStyle(me.getFont_message().getSize(), me
					.getFont_message().getStyle(), 0, me.getColor(), me
					.getFont_message().getFamily());
			serverFrame.InsertMessage(me.getFrom().getName() + "("
					+ me.getDate().toLocaleString() + ")��"
					+ me.getTo().getName() + "˵:\n");
			StyledDocument sDoc = (StyledDocument) me.getMessages();
			ArrayList<Icon> icons = me.getIcons();
			int k = 0;
			if (sDoc != null) {
				for (int i = 0; i < sDoc.getLength(); i++) {
					if (sDoc.getCharacterElement(i).getName().equals("icon")) {
						serverFrame.InsertIcon(icons.get(k++));
					} else {
						try {
							serverFrame.InsertMessage(sDoc.getText(i, 1));

						} catch (BadLocationException e) {
							System.out.println("��ȡʧ��");
						}
					}
				}
			}
			serverFrame.InsertMessage("\n");
		}
	}

	/**
	 * �����û��б�
	 * */
	public void LoadOnlineUser() {
		String[] strs = new String[50];
		for (int i = 0; i < OnlineList.size(); i++) {
			strs[i] = OnlineList.get(i).getName();
		}
		ListModel listOnlineUserModel = new DefaultComboBoxModel(strs);
		serverFrame.listOnlineUser.setModel(listOnlineUserModel);
		serverFrame.txtOnline.setText(OnlineList.size() + "��");
	}
}
