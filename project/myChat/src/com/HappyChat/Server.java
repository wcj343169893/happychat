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
	 * 服务器
	 */
	private ServerSocket serverSocket;
	/**
	 * 存放在线的用户
	 */
	private static List<User> OnlineList = new ArrayList<User>();
	/**
	 * 存放所有的临时聊天信息
	 */
	public static List<Message> Messages = new ArrayList<Message>();
	/**
	 * 输入流
	 */
	ObjectInputStream oInput;
	/**
	 * 输出流
	 */
	ObjectOutputStream oOutput;
	/**
	 * 服务器面板
	 */
	ServerFrame serverFrame;
	/**
	 * 服务器地址
	 */
	InetAddress address;
	/**
	 * 保存位置, 默认为File
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
			JOptionPane.showMessageDialog(null, "启动服务器失败!");
			System.exit(0);
		}
		LoadServer(address, port);
		this.start();
	}

	/**
	 * 加载服务器
	 * */
	private void LoadServer(InetAddress address, Integer port) {
		serverFrame = new ServerFrame();
		serverFrame.txtServerIP.setText(address.getHostAddress());
		serverFrame.txtPort.setText(port.toString());
		serverFrame.txtServiceName.setText(address.getHostName());
		serverFrame.txtState.setText("已启动....");
		// 发送系统信息
		serverFrame.btnSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String serverMessage = serverFrame.txtpEditerServerMessage
						.getText();
				if ("".equals(serverMessage)) {
					JOptionPane.showMessageDialog(null, "请不要发送空消息!");
					return;
				}
				Message m = new Message(new User("系统", null, 0, null),
						new User("所有用户", null, 0, null), null, new Date());
				m.setServerMessage(serverMessage);
				Messages.add(m);
				// 输出到服务器
				WriteMessage(m);
				// 选择保存位置
				if (SavePoint.File.equals(SavePlace)) {
					FileDAO fdao = new FileDAO();
					m.setFrom(fdao.SelectUser(m.getFrom().getName()));
					m.setTo(fdao.SelectUser(m.getTo().getName()));
					fdao.SaveMessage(m);
				} else if (SavePoint.Oracle.equals(SavePlace)) {
					// 查询两个用户的id
					Dao dao = new Dao();
					m.setFrom(dao.SelectUser(m.getFrom().getName()));
					m.setTo(dao.SelectUser(m.getTo().getName()));
					dao.SaveMessage(m);
				}
				serverFrame.txtpEditerServerMessage.setText("");
			}

			/**
			 * 显示系统消息
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
		// 踢人
		serverFrame.btnTI.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (serverFrame.listOnlineUser.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "请选择用户");
					return;
				}
				TiUser(new User(serverFrame.listOnlineUser.getSelectedValue()
						.toString(), null, 0, null));
			}
		});
		// 保存日志
		serverFrame.btnSaveLog.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					String str = serverFrame.txtpLogs.getText();
					(new FileDAO()).SaveLogs(str);
					JOptionPane.showMessageDialog(null,
							"日志保存在HapplyChatLog.txt");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "日志保存失败!");
				}
			}
		});
		// 关闭服务器
		serverFrame.btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		// 选择保存位置
		// 如果有用户登录则不能再选择
		serverFrame.cmbSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (OnlineList.size() != 0) {
					JOptionPane.showMessageDialog(null, "有用户已经登录，无法更换此选项!");
					serverFrame.cmbSave.setSelectedItem(SavePlace.toString());
					return;
				}
				Object obj = serverFrame.cmbSave.getSelectedItem();// 获取选中的值
				if (obj.equals("File")) {
					SavePlace = SavePoint.File;
				} else if (obj.equals("Oracle")) {
					// 测试连接 如果连接成功则选择Oracle否则选择File
					if ((new Dao()).getConnection() != null) {
						JOptionPane.showMessageDialog(null, "Oracle连接成功!");
						SavePlace = SavePoint.Oracle;
					} else {
						JOptionPane.showMessageDialog(null, "Oracle连接失败!");
						SavePlace = SavePoint.File;
						serverFrame.cmbSave.setSelectedIndex(0);
					}
				}
			}
		});
	}

	/**
	 * 监听客户的请求，当有用户请求时创建 ServerThread线程
	 */
	public void run() {
		try {
			while (true) {
				Socket socket = serverSocket.accept();// 等待连接
				new ServerThread(serverFrame, socket, OnlineList, Messages);
			}
		} catch (IOException e) {
			System.out.println("一个用户退出");
		}
	}

	/**
	 * @param user
	 *            踢用户
	 */
	public void TiUser(User user) {
		for (User user_on : OnlineList) {
			if (user_on.getName().equals(user.getName())) {
				OnlineList.remove(user_on);
				WriteLogs(user.getName() + "被踢了");
				break;
			}
		}
		serverFrame.txtOnline.setText(OnlineList.size() + "人");
	}

	/**
	 * @param string
	 *            日志信息--------------
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
