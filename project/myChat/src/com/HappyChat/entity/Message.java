package com.HappyChat.entity;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Icon;

public class Message implements Serializable {
	private User from;// ������
	private User to;// ������
	private Object messages;// ������Ϣ
	private ArrayList<Icon> icons;// ͼƬ�б�
	private Date date;// ����ʱ��
	private String serverMessage;// ϵͳ��Ϣ
	private Font font_message;// ����
	private Color color;// ������ɫ

	public ArrayList<Icon> getIcons() {
		return icons;
	}

	public void setIcons(ArrayList<Icon> icons) {
		this.icons = icons;
	}

	/**
	 * @return the font_message
	 */
	public Font getFont_message() {
		return font_message;
	}

	/**
	 * @param font_message
	 *            the font_message to set
	 */
	public void setFont_message(Font font_message) {
		this.font_message = font_message;
	}

	/**
	 * @return the serverMessage
	 */
	public String getServerMessage() {
		return serverMessage;
	}

	/**
	 * @param serverMessage
	 *            the serverMessage to set
	 */
	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}

	/**
	 * @param from
	 * @param to
	 * @param messages
	 * @param date
	 */
	public Message(User from, User to, String messages, Date date) {
		super();
		this.font_message = new Font("����", Font.PLAIN, 12);// Ĭ������
		this.color = Color.black;
		this.from = from;
		this.to = to;
		this.messages = messages;
		this.date = date;
	}

	public Message() {
		super();
		this.font_message = new Font("����", Font.PLAIN, 12);// Ĭ������
		this.color = Color.black;
	}

	/**
	 * @return the from
	 */
	public User getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(User from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public User getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(User to) {
		this.to = to;
	}

	/**
	 * @return the messages
	 */
	public Object getMessages() {
		return messages;
	}

	/**
	 * @param messages
	 *            the messages to set
	 */
	public void setMessages(Object messages) {
		this.messages = messages;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
