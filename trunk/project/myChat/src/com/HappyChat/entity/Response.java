package com.HappyChat.entity;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
	private RequestType rt;// 请求类型
	private List<User> onLine;// 在线用户列表
	private boolean isSuccess;// 返回请求是否成功
	private Object info;// 消息

	/**
	 * @return the rt
	 */
	public RequestType getRt() {
		return rt;
	}

	/**
	 * @param rt
	 *            the rt to set
	 */
	public void setRt(RequestType rt) {
		this.rt = rt;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return isSuccess;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(boolean flag) {
		this.isSuccess = flag;
	}

	/**
	 * @return the info
	 */
	public Object getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(Object info) {
		this.info = info;
	}

	/**
	 * @return the onLine
	 */
	public List<User> getOnLine() {
		return onLine;
	}

	/**
	 * @param onLine
	 *            the onLine to set
	 */
	public void setOnLine(List<User> onLine) {
		this.onLine = onLine;
	}

	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * @param isSuccess
	 *            the isSuccess to set
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * @param rt
	 * @param flag
	 * @param info
	 */
	public Response(RequestType rt, boolean flag, List<User> users, Object info) {
		super();
		this.onLine = users;
		this.rt = rt;
		this.isSuccess = flag;
		this.info = info;
	}

	public Response() {
		super();
	}

}
