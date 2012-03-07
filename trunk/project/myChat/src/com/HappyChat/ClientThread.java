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
 * 客户端写入
 * */
public class ClientThread extends Thread {
	private ObjectOutputStream oOutput;
	private ObjectInputStream oInput;//
	private Response response;// 来自服务器端的响应
	private Request request;// 请求
	private Socket socket;// 客户端对象
	private User user;// 当前用户
	List<User> onLine = new ArrayList<User>();
	List<Message> messages = new ArrayList<Message>();
	Object obj;// 存放中间变量

	private StyledDocument document = null;// 文档对象
	private SimpleAttributeSet attributes = new SimpleAttributeSet();// 设置的样式

	private Register register;// 注册界面
	private ChatRoom chatRoom;// 聊天界面
	private Login login;// 登录界面

	String ipAddress = "127.0.0.1";// 默认服务器ip
	Integer port = 5200;// 默认端口

	/**
	 * 读取数据次数
	 * */
	int readID = -1;

	public ClientThread() {
		// 显示登录窗体
		login = new Login();
		login.txtIPAddress.setText(ipAddress);
		login.txtPort.setText(port.toString());
		login.setTitle("登录");
		login.setResizable(false);
		login.txtName.requestFocus();// 设置为焦点 要放在：show(),setVisible(true)方法的后面
		LoadLoginFrame();// 加载登录事件
	}

	// ////////////////////////////////加载客户端事件//////////////////////////////////////////////////////
	/**
	 * 处理Login窗体 重新加载事件
	 * */
	public void LoadLoginFrame() {
		// Login的登录事件
		this.login.btnLogin.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断输入是否合法
				String Name = login.txtName.getText();
				String Password = login.pdfPassword.getText();
				if (login.txtIPAddress.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入服务器ip");
					login.txtIPAddress.requestFocus();
				} else if (login.txtPort.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入服务器port");
					login.txtPort.requestFocus();
				} else if (Name.equals("")) {
					JOptionPane.showMessageDialog(null, "请输入用户名");
					login.txtName.requestFocus();
				} else if (Password.equals("")) {
					JOptionPane.showMessageDialog(null, "请输入密码");
					login.pdfPassword.requestFocus();
				} else {
					// 登录验证
					user = new User(Name, Password, 0, null);
					if (Login(user)) {
						// 验证通过
						chatRoom = new ChatRoom();//
						// 加载登录用户列表
						ShowOnlineUserToClient();
						chatRoom.txtpEditerMessage.requestFocus();
						LoadChatRoom();// 加载登录界面
						chatRoom.setTitle("欢乐聊天室[用户:" + Name + "]");
						login.setVisible(false);// 隐藏登录界面
						// 启动监听接收数据
						start();
					}
				}
			}

		});

		// 打开注册界面
		this.login.btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ipAddress = login.txtIPAddress.getText().trim();// 重新获取ip
				register = new Register();
				LoadRegister();// 加载注册界面事件
			}
		});
	}

	/**
	 * 处理ChatRoom窗体 重新加载事件
	 * */
	private void LoadChatRoom() {
		document = (StyledDocument) chatRoom.txtPMessage.getDocument();// 得到对像
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
		 * 退出
		 * */
		this.chatRoom.btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Exit(user);
			}
		});
		// 发送消息
		chatRoom.btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (user.getName().equals(
						chatRoom.combUser.getSelectedItem().toString())) {
					JOptionPane.showMessageDialog(null, "请不要自言自语");
					chatRoom.txtpEditerMessage.setText("");
					chatRoom.txtpEditerMessage.requestFocus();
					return;
				}
				if (chatRoom.txtpEditerMessage.getText().length() > 250) {
					JOptionPane.showMessageDialog(null, "您输入的字符超过250个，请截断");
					chatRoom.txtpEditerMessage.setText("");
					chatRoom.txtpEditerMessage.requestFocus();
					return;
				}
				if (chatRoom.txtpEditerMessage.getText().length() < 1) {
					JOptionPane.showMessageDialog(null, "请不要发送空信息");
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
				me.setMessages(chatRoom.txtpEditerMessage.getStyledDocument());// 把编辑区的document传到message中
				me.setIcons(icons);
				SendMessage(me);
				chatRoom.txtpEditerMessage.setText(null);
				chatRoom.txtpEditerMessage.requestFocus();
			}
		});

		// 抓屏
		chatRoom.btnCatchScreen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				File tempFile = new File("d:", (new Date()).getHours() + ""
						+ (new Date()).getMinutes() + ""
						+ (new Date()).getSeconds() + "_message.jpg");
				ScreenCapture capture = ScreenCapture.getInstance();// ScreenCapture类似于键盘上的PrintScreen键的功能
				capture.captureImage();
				chatRoom.txtpEditerMessage.insertIcon(capture.getPickedIcon());
				try {
					capture.saveToFile(tempFile);
				} catch (IOException e1) {

				}
				capture.captureImage();

			}
		});
		// 把选中的在线用户添加到下面的下拉菜单中
		// 在用户前面可以添加用户的头像
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
						// 添加
						if (!flag) {
							chatRoom.combUser.addItem(obj_u);
						}
						chatRoom.combUser.setSelectedItem(obj_u);
					}
				});
	}

	/**
	 * 处理Register窗体 重新加载事件
	 * */
	private void LoadRegister() {
		// 取消注册
		this.register.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				register.setVisible(false);
			}
		});
		// 注册
		this.register.btnRegister.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent event) {
				// 验证数据的合法性
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
					errorMessage = "请输入用户名";
				} else if (password.equals("")) {
					flag = true;
					errorMessage = "请输入密码";
				} else if (rePassword.equals("")) {
					flag = true;
					errorMessage = "请输入确认密码";
				} else if (!rePassword.equals(password)) {
					flag = true;
					errorMessage = "您输入的密码和确认密码不一致 ！";
				} else if (age.equals("")) {
					flag = true;
					errorMessage = "请输入年龄";
				} else if (email.equals("")) {
					flag = true;
					errorMessage = "请输入电子邮件，有利于找回密码";
				} else if (!email
						.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
					flag = true;
					errorMessage = "电子邮件格式不正确!";
				} else {
					flag = false;
				}
				if (flag) {
					JOptionPane.showMessageDialog(register, errorMessage);
				} else {
					try {
						ages = Integer.parseInt(age);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(register, "您输入的年龄格式不正确");
						return;
					}
					if (ages > 220 || ages < 0) {
						JOptionPane.showMessageDialog(register, "您输入的年龄不正确");
						return;
					}

					Register(new User(name, password, ages, email));
				}
			}
		});
	}

	/**
	 * 刷新在线用户列表
	 */
	private void ShowOnlineUserToClient() {
		String[] strs = new String[50];
		for (int i = 0; i < onLine.size(); i++) {
			strs[i] = onLine.get(i).getName();
		}
		ListModel listm = new DefaultComboBoxModel(strs);
		chatRoom.listOnlineUser.setModel(listm);
	}

	// ///////////////////////////////服务器代码/////////////////////////////////////////////////
	public void run() {// 新建一个Socket连接 向服务器传送一个空的message 读取服务器上的OnlineList 和
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
	 * 显示聊天信息
	 */
	private void ShowMessage(Message me) {
		int end = chatRoom.txtPMessage.getSelectionEnd();// 获取到textArea的末尾
		if (!"".equals(me)) {
			String message = me.getFrom().getName() + "("
					+ me.getDate().toLocaleString() + ")对"
					+ me.getTo().getName() + "说:\n" + me.getMessages() + "\n";
			chatRoom.txtPMessage.setText(message);
		}
	}

	/**
	 * 显示系统信息
	 */
	private void ShowMessage(String str) {
		int end = chatRoom.txtPMessage.getSelectionEnd();// 获取到textArea的末尾
		if (!"".equals(str)) {
			chatRoom.InsertMessage(str + "\n");
		}
	}

	/**
	 * 注册事件 注册 ok
	 * */
	public void Register(User user) {
		request = new Request(user, RequestType.register);
		try {
			Socket socket_r = new Socket(ipAddress, port);
			ObjectOutputStream output = new ObjectOutputStream(socket_r
					.getOutputStream());
			output.writeObject(request);// 发送注册信息

			ObjectInputStream input = new ObjectInputStream(socket_r
					.getInputStream());
			response = (Response) input.readObject();// 接收信息

			JOptionPane.showMessageDialog(null, (response.isFlag() ? "注册成功!"
					: response.getInfo()));

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "连接服务器失败");
			return;
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "连接服务器失败");
			return;
		}
	}

	/**
	 * 登录 事件 ok
	 * */
	public boolean Login(User user) {
		boolean isPass = false;
		port = Integer.parseInt(login.txtPort.getText().trim());
		ipAddress = login.txtIPAddress.getText().trim();
		request = new Request(user, RequestType.login);
		try {
			socket = new Socket(ipAddress, port);// 获取一个新连接

			oOutput = new ObjectOutputStream(socket.getOutputStream());
			oOutput.writeObject(request);// 发送用户名和密码

			oInput = new ObjectInputStream(socket.getInputStream());
			response = (Response) oInput.readObject();// 接收响应

		} catch (NumberFormatException e1) {
		} catch (UnknownHostException e1) {
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "连接服务器失败");
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		isPass = response.isFlag();// 是否通过
		if (!isPass) {
			JOptionPane.showMessageDialog(null, response.getInfo());// 没有通过，显示相应的错误信息
		} else {
			onLine = (List) response.getOnLine();// 获取所有用户
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
	 * 发送信息 事件 把信息存放到服务器 获取所有的聊天信息 并判断
	 * */
	public void SendMessage(Message message) {
		request = new Request(message, RequestType.sendMessage);
		try {
			Socket socket_m = new Socket(ipAddress, port);

			ObjectOutputStream output = new ObjectOutputStream(socket_m
					.getOutputStream());

			output.writeObject(request);// 发送信息

			ObjectInputStream input = new ObjectInputStream(socket_m
					.getInputStream());
			Response response = (Response) input.readObject();// 获取服务器的响应
			boolean isConnect = false;// 用户列表中是否有这个用户
			for (User online_u : response.getOnLine()) {
				if (online_u.getName().equals(user.getName())) {
					isConnect = true;
					break;
				}
			}
			if (!isConnect) {
				JOptionPane.showMessageDialog(null, "你被管理员踢了");
				System.exit(0);
			}
			// 判断是否有用户退出或登入
			for (User u_Server : response.getOnLine()) {
				boolean isFind = false;
				for (User u_client : onLine) {
					if (u_Server.getName().equals(u_client.getName())) {
						isFind = true;
						break;
					}
				}
				if (!isFind) {
					// 提示User来了
					ShowMessage(u_Server.getName() + "来了");
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
					// 提示User走了
					ShowMessage(u_client.getName() + "走了");
				}
			}
			this.onLine = response.getOnLine();

			// 刷新登录用户列表
			ShowOnlineUserToClient();
			// 获取消息
			messages = (List<Message>) response.getInfo();
			// 如果是第一次读取信息 把其他用户读取的信息屏蔽掉
			if (readID == -1) {
				readID = messages.size();
			}
			// 读取信息
			for (int i = readID; i < messages.size(); i++) {
				// 设置此条消息的显示样式
				chatRoom.createStyle(messages.get(i).getFont_message()
						.getSize(), messages.get(i).getFont_message()
						.getStyle(), 0, messages.get(i).getColor(), messages
						.get(i).getFont_message().getFamily());
				// 判断是否是系统消息
				if (messages.get(i).getServerMessage() != null) {
					chatRoom.InsertMessage("系统消息:"
							+ messages.get(i).getServerMessage() + "\n");
				} else {
					StyledDocument sDoc = (StyledDocument) messages.get(i)
							.getMessages();// 把消息强制转换
					ArrayList<Icon> icons = messages.get(i).getIcons();
					int k = 0;
					chatRoom
							.InsertMessage(messages.get(i).getFrom().getName()
									+ "("
									+ messages.get(i).getDate()
											.toLocaleString() + ")对"
									+ messages.get(i).getTo().getName()
									+ "说:\n");// 
					if (sDoc != null) {
						for (int j = 0; j < sDoc.getLength(); j++) {
							// 判断是否是icon类型
							if ("icon".equals(sDoc.getCharacterElement(j)
									.getName())) {// 显示图片
								chatRoom.InsertIcon(icons.get(k++));
							} else {// 显示信息
								chatRoom.InsertMessage(sDoc.getText(j, 1));
							}
						}
					}
					chatRoom.InsertMessage("\n");// 换行
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
			JOptionPane.showMessageDialog(null, "连接服务器失败！");
			System.exit(0);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "连接服务器失败！");
			System.exit(0);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(null, "接收数据失败！");
			System.exit(0);
		}
	}

	/**
	 * 退出
	 * 
	 * @param user
	 *            退出用户
	 * @return 是否退出
	 */
	public boolean Exit(User user) {
		this.suspend();// 线程休眠
		int number = JOptionPane.showConfirmDialog(null, "是否退出", "退出提示",
				JOptionPane.YES_NO_OPTION);
		if (number == 1) {
			this.resume();// 线程启动
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
				JOptionPane.showMessageDialog(null, "退出成功！");
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(null, "退出失败！");
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
