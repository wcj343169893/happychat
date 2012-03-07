package com.HappyChat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.HappyChat.entity.Message;
import com.HappyChat.entity.User;

public class FileDAO {
	/**
	 * 保存聊天消息 运行时报错
	 * 
	 * @param message
	 */
	public void SaveMessage(Message message) {
		String path = "userMessage.txt";
		List<Message> messages = null;
		try {
			ObjectInputStream objInput = getInput(path);
			if (objInput != null) {
				messages = (ArrayList<Message>) objInput.readObject();
			} else {
				messages = new ArrayList<Message>();
			}
			if (objInput != null) {
				objInput.close();
			}
			messages.add(message);
			ObjectOutputStream objOutput = getOutput(path);
			objOutput.writeObject(messages);
			if (objOutput != null) {
				objOutput.close();
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
	}

	/**
	 * 验证用户是否存在
	 * 
	 * @param user
	 * @return
	 */
	public boolean IsExist(User user) {
		boolean flag = false;
		ArrayList<User> UserList = null;
		try {
			ObjectInputStream objInput = getInput("user.txt");
			UserList = (ArrayList<User>) objInput.readObject();
			if (UserList == null) {
				UserList = new ArrayList<User>();
			}
			for (User user2 : UserList) {
				if (user2.getName().equals(user.getName())) {
					flag = true;
					break;
				}
			}
			if (objInput != null) {
				objInput.close();
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return flag;
	}

	/**
	 * 只能读取user.txt 其他的文件无法读取
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FileDAO fdao = new FileDAO();
		// System.out.println(fdao.getInput("OtherWords.txt"));
		File file = new File("user.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileInputStream input = new FileInputStream(file);
			// System.out.println(input.read());
			ObjectInputStream objInput = new ObjectInputStream(input);
			System.out.println(objInput.readObject());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 * @return
	 */
	private ObjectOutputStream getOutput(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		ObjectOutputStream objOutput = null;
		try {
			objOutput = new ObjectOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return objOutput;
	}

	/**
	 * 获取输入流
	 * 
	 * @param path
	 * @return
	 */
	private ObjectInputStream getInput(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		ObjectInputStream objInput = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(path);
			objInput = new ObjectInputStream(input);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objInput;
	}

	/**
	 * 注册
	 * 
	 * @param user
	 * @return
	 */
	public boolean Register(User user) {
		boolean flag = false;
		String path = "user.txt";
		List<User> userList = null;
		try {
			ObjectInputStream objInput = getInput(path);
			userList = (ArrayList<User>) objInput.readObject();
			if (objInput != null) {
				objInput.close();
			}
			if (userList == null) {
				userList = new ArrayList<User>();
			}
			userList.add(user);
			ObjectOutputStream objOutput = getOutput(path);
			objOutput.writeObject(userList);
			if (objOutput != null) {
				objOutput.close();
			}
			flag = true;
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 查询单个用户
	 * 
	 * @param uName
	 * @return
	 */
	public User SelectUser(String uName) {
		User user = null;
		List<User> UserList = null;
		try {
			ObjectInputStream objInput = getInput("user.txt");
			UserList = (ArrayList<User>) objInput.readObject();
			if (UserList == null) {
				UserList = new ArrayList<User>();
			}
			for (User user2 : UserList) {
				if (user2.getName().equals(uName)) {
					user = user2;
					break;
				}
			}
			if (objInput != null) {
				objInput.close();
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return user;
	}

	/**
	 * 保存日志
	 * 
	 * @param str
	 */
	public void SaveLogs(String str) {
		FileOutputStream fileoutput;
		try {
			fileoutput = new FileOutputStream("HapplyChatLog.txt", true);
			fileoutput.write(str.getBytes());
			if (fileoutput != null) {
				fileoutput.close();
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
}
