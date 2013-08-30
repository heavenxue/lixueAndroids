package com.lixueandroid.net;

import com.google.gson.annotations.SerializedName;

public class ErrorInfo {
	/**
	 * 错误码 - 异常
	 */
	public static final int CODE_EXCEPTION = 123142;
	/**
	 * 错误码
	 */
	@SerializedName("errorCode")
	private int code;
	/**
	 * 错误提示消息
	 */
	@SerializedName("errorMessage")
	private String message;
	
	public ErrorInfo(int code, String message){
		setCode(code);
		setMessage(message);
	}
	
	public ErrorInfo(){
		
	}
	
	/**
	 * 获取错误码
	 * @return 错误码
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * 设置错误码
	 * @param code 错误码
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * 获取错误提示消息
	 * @return 错误提示消息
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * 设置错误提示消息
	 * @param message 错误提示消息
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString(){
		return "code="+getCode()+"; message="+getMessage();
	}
}