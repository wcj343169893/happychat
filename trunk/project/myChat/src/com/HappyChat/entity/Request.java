package com.HappyChat.entity;

import java.io.Serializable;

/**
 * 请求类
 * */
public class Request implements Serializable{
	private RequestType rt;// 请求类型
	private Object obj;// 请求数据


	public Request() {

	}

	public Request(Object obj, RequestType rt) {
		this.obj = obj;
		this.rt = rt;
	}

	/**
	 * @return the rt
	 */
	public RequestType getRt() {
		return rt;
	}

	/**
	 * @return the obj
	 */
	public Object getObj() {
		return obj;
	}

	/**
	 * @param rt the rt to set
	 */
	public void setRt(RequestType rt) {
		this.rt = rt;
	}

	/**
	 * @param obj the obj to set
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}

}
