/**
 * 
 */
package com.HappyChat.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import com.HappyChat.entity.Message;
import com.HappyChat.entity.User;

public class Dao {
	/**
	 * 连接数据库
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		CProperties cproperties = new CProperties("dbHelper.properties");

		Connection conn = null;
		try {
			Class.forName(cproperties.getResource("class_name"));
			conn = DriverManager.getConnection(cproperties.getResource("url"),
					cproperties.getResource("user_name"), cproperties
							.getResource("user_password"));
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		return conn;
	}

	/**
	 * 关闭
	 * 
	 * @param conn
	 * @param statement
	 * @param rs
	 */
	private void Close(Connection conn, Statement statement, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 保存消息
	 * 
	 * @param message
	 * @return
	 */
	public boolean SaveMessage(Message message) {
		boolean flag = false;
		String sqlString = "insert into messages(mto,mfrom,mmessage,mdate,mservermessage)values(?,?,?,sysdate,?)";
		PreparedStatement ps = null;
		Connection conn = null;
		StyledDocument sDoc = (StyledDocument) message.getMessages();
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sqlString);
			ps.setString(1, message.getTo().getUserid());
			ps.setString(2, message.getFrom().getUserid());
			if (sDoc == null) {
				ps.setObject(3, message.getServerMessage());
			} else {
				ps.setObject(3, sDoc.getText(0, sDoc.getLength()));
			}
			ps.setString(4, message.getServerMessage());
			if (ps.executeUpdate() > 0) {
				flag = true;
			}
		} catch (SQLException e) {
		} catch (BadLocationException e) {
		}
		Close(conn, ps, null);
		return flag;
	}

	/**
	 * 注册
	 * 
	 * @param user
	 *            注册用户
	 * @return 是否注册成功 TRUE 成功，FALSE 不成功
	 */
	public boolean Register(User user) {
		boolean flag = false;
		PreparedStatement ps = null;
		Connection conn = null;
		String sqlString = "insert into users (uname,upassword,uage,uemail)values(?,?,?,?)";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sqlString);
			ps.setString(1, user.getName());
			ps.setString(2, user.getPassword());
			ps.setInt(3, user.getAge());
			ps.setString(4, user.getEmail());
			if (ps.executeUpdate() > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			System.out.println("注册失败！");
		}
		Close(conn, ps, null);
		return flag;
	}

	/**
	 * 验证用户是否存在
	 * 
	 * @param user
	 *            验证用户
	 * @return 返回是否存在 TRUE 存在，FALSE 不存在
	 */
	public boolean IsExist(User user) {
		boolean flag = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = null;
		String sqlString = "select count(*) from users where uname=? and upassword=?";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sqlString);
			ps.setString(1, user.getName());
			ps.setString(2, user.getPassword());
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					flag = true;
				} else {
					flag = false;
				}
			}
		} catch (SQLException e) {
		}
		Close(conn, ps, rs);
		return flag;
	}

	/**
	 * 查询单个 用户
	 * 
	 * @param name
	 * @return null 没有查到
	 */
	public User SelectUser(String name) {
		User user = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = null;
		String sqlString = "select * from users where uname=?";
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sqlString);
			ps.setString(1, name);
			rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setUserid(rs.getString("userId"));
				user.setName(rs.getString("uname"));
				user.setPassword(rs.getString("upassword"));
				user.setAge(rs.getInt("uage"));
				user.setEmail(rs.getString("uemail"));
			}
		} catch (SQLException e) {
		}
		Close(conn, ps, rs);
		return user;
	}
}
