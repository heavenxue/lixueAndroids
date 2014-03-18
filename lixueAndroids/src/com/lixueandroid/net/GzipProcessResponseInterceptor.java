package com.lixueandroid.net;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

public class GzipProcessResponseInterceptor implements HttpResponseInterceptor,GzipProcess{

	@Override
	public void process(HttpResponse response, HttpContext context)throws HttpException, IOException {
        final HttpEntity entity = response.getEntity();
        if(entity != null) {
        	final Header encoding = entity.getContentEncoding();
        	if (encoding != null) {
        		for (HeaderElement element : encoding.getElements()) {
        			if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
        				response.setEntity(new InflatingEntity(response.getEntity()));
        				break;
        			}
        		}
        	}
        }		
	}
}
