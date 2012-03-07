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
 * 服务器 输出到服务器 对每一个客户端的信息进行处理
 * */
/**
 * @author Administrator
 * 
 */
public class ServerThread extends Thread {
	/**
	 * 与客户端通信Socket
	 * */
	private Socket socket;
	/**
	 * 客户端到服务器的输入流
	 * */
	private ObjectInputStream oInput;
	/**
	 * 服务器到客户端的输出流
	 * */
	private ObjectOutputStream oOutput;
	/**
	 * 当前用户
	 * */
	private User user;
	/**
	 * 所有在线用户
	 * */
	List<User> OnlineList;
	/**
	 * 存放所有用户聊天的临时信息
	 * */
	List<Message> messages;
	/**
	 * 响应类型，用来存放返回给用户的信息
	 * */
	private Response response;
	/**
	 * 服务器窗体
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
	 * 处理客户端请求的线程
	 * */
	public void run() {
		try {
			this.oInput = new ObjectInputStream(socket.getInputStream());
			this.oOutput = new ObjectOutputStream(socket.getOutputStream());
			Object o = oInput.readObject();
			GetRequest(o);// 判断请求类型
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
			System.out.println("读取对象错误");
		}
	}

	/**
	 * 根据obj的类型，决定该执行什么操作 操作完成后返回一个response
	 * */
	public void GetRequest(Object obj) {
		// obj中只有一种类型:Request
		Request request = (Request) obj;
		if (request.getRt().equals(RequestType.login)) {
			// 如果obj是login则执行登录验证
			Login(request);
		} else if (request.getRt().equals(RequestType.register)) {
			// 注册
			Register(request);
		} else if (request.getRt().equals(RequestType.sendMessage)) {
			// 发送信息
			SendMessage(request);
		} else if (request.getRt().equals(RequestType.exit)) {
			// 退出
			ClientExit(request);
		} else {
			// 读取数据失败
			response = new Response(RequestType.sendMessage, false, OnlineList,
					null);
		}
		LoadOnlineUser();
	}

	/**
	 * 用户正常退出 ok
	 * */
	private void ClientExit(Request request) {
		// 发送信息到其他用户 显示该用户走了
		response = new Response(RequestType.exit, true, OnlineList, request
				.getObj());

		// 重OnlineList中删除此用户
		for (int i = 0; i < Server.getOnlineList().size(); i++) {
			if (Server.getOnlineList().get(i).getName().equals(
					((User) request.getObj()).getName())) {
				Server.getOnlineList().remove(i);
				break;
			}
		}
		WriteLogs(((User) (request.getObj())).getName() + "退出了");
		serverFrame.txtOnline.setText(Server.getOnlineList().size() + "人");
		// 返回给用户
		try {
			oOutput.writeObject(response);
			oOutput.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 *发送信息 ok
	 **/
	private void SendMessage(Request request) {
		Message message = (Message) request.getObj();// 转换为message类型
		if (message.getMessages() != null) {
			// 添加到列表
			messages.add(message);
			// 输出到服务器
			WriteMessage(message);
			// 选择保存位置
			if (SavePoint.File.equals(Server.getSavePlace())) {
				FileDAO fdao = new FileDAO();
				message.setFrom(fdao.SelectUser(message.getFrom().getName()));
				message.setTo(fdao.SelectUser(message.getTo().getName()));
				fdao.SaveMessage(message);
			} else if (SavePoint.Oracle.equals(Server.getSavePlace())) {
				// 查询两个用户的id
				Dao dao = new Dao();
				message.setFrom(dao.SelectUser(message.getFrom().getName()));
				message.setTo(dao.SelectUser(message.getTo().getName()));
				dao.SaveMessage(message);
			}
			serverFrame.txtpUserMessage.selectAll();// 滚动条到最后
		}
		response = new Response(RequestType.sendMessage, true, OnlineList,
				messages);
		// 返回给用户
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
	 *            登录请求
	 */
	public void Login(Request request) {
		User user = (User) request.getObj();// 转换为User类型
		response = new Response(RequestType.login, false, OnlineList, user);
		// 判断用户是否存在(数据库)和判断此用户是否登录
		if (IsExist(user) && !IsInList(user)) {
			response.setFlag(true);
			// 如果登录成功，则
			this.OnlineList.add(user);
			response.setOnLine(OnlineList);
			// 系统显示用户登录信息
			WriteLogs(user.getName() + "登录了，时间:"
					+ (new Date()).toLocaleString());
		} else if (!IsExist(user)) {
			response.setInfo("不存在该用户！请先注册");
		} else if (OnlineList.size() >= 50) {
			response.setInfo("登录人数太多，请稍后..");
		} else {
			response.setInfo("该用户已经登录!");
		}
		try {
			oOutput.writeObject(response);
			oInput.close();
		} catch (IOException e) {
			System.out.println("服务器登录处理失败!");
		}
	}

	/**
	 * 验证用户是否已登录
	 * 
	 * @param user
	 *            需要验证的用户
	 * @return 是否存在该用户
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
	 *注册
	 **/
	public void Register(Request request) {
		User user = (User) request.getObj();// 转换为User类型
		response = new Response(RequestType.register, false, OnlineList, user);
		// 判断用户是否存在
		if (!IsExist(user)) {
			boolean isRegister = false;
			// 选择保存位置
			if (SavePoint.File.equals(Server.getSavePlace())) {
				isRegister = (new FileDAO()).Register(user);
			} else if (SavePoint.Oracle.equals(Server.getSavePlace())) {
				isRegister = (new Dao()).Register(user);
			}
			response.setFlag(isRegister);
			response.setInfo(isRegister ? "成功" : "失败");
			// 显示注册日志
			WriteLogs(user.getName() + "注册" + (isRegister ? "成功" : "失败")
					+ ",时间:" + (new Date()).toLocaleString());
		} else {
			response.setInfo("用户已存在，请另外选择");
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
	 * 判断数据库中是否存在此用户
	 * 
	 * @param user
	 * @return
	 */
	private boolean IsExist(User user) {
		return SavePoint.Oracle.equals(Server.getSavePlace()) ? (new Dao())
				.IsExist(user) : (new FileDAO()).IsExist(user);
	}

	/**
	 * 写日志---------------------
	 * */
	public void WriteLogs(String logs) {
		serverFrame.InsertLogs(logs + "\n");
	}

	/**
	 * 服务器显示聊天信息
	 * */
	public void WriteMessage(Message me) {
		if (me != null) {
			// 设置此条信息的显示样式
			serverFrame.createStyle(me.getFont_message().getSize(), me
					.getFont_message().getStyle(), 0, me.getColor(), me
					.getFont_message().getFamily());
			serverFrame.InsertMessage(me.getFrom().getName() + "("
					+ me.getDate().toLocaleString() + ")对"
					+ me.getTo().getName() + "说:\n");
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
							System.out.println("读取失败");
						}
					}
				}
			}
			serverFrame.InsertMessage("\n");
		}
	}

	/**
	 * 加载用户列表
	 * */
	public void LoadOnlineUser() {
		String[] strs = new String[50];
		for (int i = 0; i < OnlineList.size(); i++) {
			strs[i] = OnlineList.get(i).getName();
		}
		ListModel listOnlineUserModel = new DefaultComboBoxModel(strs);
		serverFrame.listOnlineUser.setModel(listOnlineUserModel);
		serverFrame.txtOnline.setText(OnlineList.size() + "人");
	}
}
