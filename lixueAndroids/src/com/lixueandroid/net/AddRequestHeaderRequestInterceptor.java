package com.lixueandroid.net;

import java.io.IOException;
import java.util.Map;

import me.xiaopan.easy.network.android.http.interceptor.GzipProcess;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
/**
 * 添加请求头的请求拦截器
 */
public class AddRequestHeaderRequestInterceptor implements HttpRequestInterceptor,GzipProcess{
	private Map<String, String> requestHeaderMap;
	
	public AddRequestHeaderRequestInterceptor(Map<String, String> clientHeaderMap){
		this.requestHeaderMap = clientHeaderMap;
	}
	
	@Override
	public void process(HttpRequest request, HttpContext context)throws HttpException, IOException {
		if(requestHeaderMap != null  && requestHeaderMap.size() > 0){
			for (String header : requestHeaderMap.keySet()) {
				request.addHeader(header, requestHeaderMap.get(header));
			}
		}
	}
}
