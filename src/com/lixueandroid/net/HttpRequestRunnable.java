package com.lixueandroid.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

/**
 *	请求线程
 */
public class HttpRequestRunnable implements Runnable{
	private HttpApacheFac httpApacheFac;
	private AbstractHttpClient httpClient;
	private HttpContext httpContext;
	private HttpResponseHandler responseHandler;
	private HttpUriRequest uriRequest;
	
	public HttpRequestRunnable(HttpApacheFac httpApacheFac,AbstractHttpClient abstractHttpClient,HttpContext httpContext,HttpResponseHandler handler,HttpUriRequest request){
		this.httpApacheFac=httpApacheFac;
		this.httpClient=abstractHttpClient;
		this.httpContext=httpContext;
		this.responseHandler=handler;
		this.uriRequest=request;
	}
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		if(!Thread.currentThread().interrupted()){
			responseHandler.start();
		}
		try{
			if(!Thread.currentThread().interrupted()){
				httpApacheFac.Log("请求地址URL："+uriRequest.getURI().toString());
			}
			if(!Thread.currentThread().interrupted()&&responseHandler!=null){
				HttpResponse thisHttpResponse= httpClient.execute(uriRequest,httpContext);
				try {
					responseHandler.handleResponse(thisHttpResponse);
				} catch (Throwable e) {
					responseHandler.exception(e);
				}
			}
		}catch(Exception e){
			if(uriRequest!=null){
				uriRequest.abort();
			}
			if(!Thread.interrupted()){
				responseHandler.exception(e);
			}
		}
		if(!Thread.currentThread().isInterrupted()&&responseHandler!=null){
			responseHandler.end();
		}
	}
}
