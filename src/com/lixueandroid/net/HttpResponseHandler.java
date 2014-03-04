package com.lixueandroid.net;

import org.apache.http.HttpResponse;

public interface HttpResponseHandler {
	/**
	 * 开始
	 */
	public void start();
	/**
	 * 处理响应
	 * @param httpResponse
	 */
	public void handleResponse(HttpResponse httpResponse) throws Throwable;
	/**
	 * 异常
	 */
	public void exception(Throwable t);
	/**
	 * 结束
	 */
	public void end();
	
}
