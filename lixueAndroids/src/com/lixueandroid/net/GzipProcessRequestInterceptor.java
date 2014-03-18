package com.lixueandroid.net;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

public class GzipProcessRequestInterceptor implements HttpRequestInterceptor,GzipProcess{

	@Override
	public void process(HttpRequest request, HttpContext context)throws HttpException, IOException {
		//如果请求头中没有HEADER_ACCEPT_ENCODING属性就添加进去
    	if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
            request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
        }
	}

}
