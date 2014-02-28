package com.lixueandroid.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import me.xiaopan.easy.network.android.http.False;
import me.xiaopan.easy.network.android.http.Host;
import me.xiaopan.easy.network.android.http.HttpHeaderUtils;
import me.xiaopan.easy.network.android.http.Path;
import me.xiaopan.easy.network.android.http.Request;
import me.xiaopan.easy.network.android.http.RequestParams;
import me.xiaopan.easy.network.android.http.True;
import me.xiaopan.easy.network.android.http.Url;
import me.xiaopan.easy.network.android.http.headers.ContentType;
import me.xiaopan.easyjava.util.AnnotationUtils;
import me.xiaopan.easyjava.util.ClassUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lixueandroid.net.EasyNetwork;
import com.lixueandroid.net.HttpApacheFac;

public class HttpUtils {
	/**
	 * 获取响应编码，首先会尝试从响应体的Content-Type中获取，如果获取不到的话就返回默认的UTF-8
	 * @param httpResponse
	 * @return
	 */
	public static final String getResponseCharset(HttpResponse httpResponse){
		ContentType contentType = HttpHeaderUtils.getContentType(httpResponse);
		if(contentType != null){
			return contentType.getCharset(EasyNetwork.CHARSET_NAME_UTF8);
		}else{
			return EasyNetwork.CHARSET_NAME_UTF8;
		}
	}
	
	/**
	 * 将一个请求对象转换为RequestParams对象
	 * @param request 请求对象
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static RequestParams requestToRequestParams(Request request){
		if(request != null){
			RequestParams requestParams = new RequestParams();
			
			//循环处理所有字段
			String paramValue;
			Object paramValueObject;
			for(Field field : ClassUtils.getFields(request.getClass(), true, true, true)){
				if(field.getAnnotation(Expose.class) != null){	//如果当前字段被标记为需要序列化
					try {
						field.setAccessible(true);
						if((paramValueObject = field.get(request)) != null){
							if(paramValueObject instanceof Map){	//如果当前字段是一个MAP，就取出其中的每一项添加到请求参数集中
								Map<Object, Object> map = (Map<Object, Object>)paramValueObject;
								for(java.util.Map.Entry<Object, Object> entry : map.entrySet()){
									if(entry.getKey() != null && entry.getValue() != null && StringUtils.isNotEmpty(entry.getKey().toString(), entry.getValue().toString())){
										requestParams.put(entry.getKey().toString(), entry.getValue().toString());	
									}
								}
							}else if(paramValueObject instanceof File){	//如果当前字段是一个文件，就将其作为一个文件添加到请求参数中
								requestParams.put(getParamKey(field), (File) paramValueObject);
							}else if(paramValueObject instanceof ArrayList){	//如果当前字段是ArrayList，就将其作为一个ArrayList添加到请求参水集中
								requestParams.put(getParamKey(field), (ArrayList<String>) paramValueObject);
							}else if(paramValueObject instanceof Boolean){	//如果当前字段是boolean
								if((Boolean) paramValueObject){
									True true1 = field.getAnnotation(True.class);
									if(true1 != null && StringUtils.isNotEmpty(true1.value())){
										requestParams.put(getParamKey(field), true1.value());
									}else{
										requestParams.put(getParamKey(field), paramValueObject.toString());
									}
								}else{
									False false1 = field.getAnnotation(False.class);
									if(false1 != null && StringUtils.isNotEmpty(false1.value())){
										requestParams.put(getParamKey(field), false1.value());
									}else{
										requestParams.put(getParamKey(field), paramValueObject.toString());
									}
								}
							}else if(paramValueObject instanceof Enum){	//如果当前字段是枚举
								Enum<?> enumObject = (Enum<?>) paramValueObject;
								SerializedName serializedName = AnnotationUtils.getAnnotationFromEnum(enumObject, SerializedName.class);
								if(serializedName != null && StringUtils.isNotEmpty(serializedName.value())){
									requestParams.put(getParamKey(field), serializedName.value());
								}else{
									requestParams.put(getParamKey(field), enumObject.name());
								}
							}else{	//如果以上几种情况都不是就直接转为字符串添加到请求参数集中
								paramValue = paramValueObject.toString();
								if(StringUtils.isNotEmpty(paramValue)){
									requestParams.put(getParamKey(field), paramValue);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			return requestParams;
		}else{
			return null;
		}
	}
	
	/**
	 * 获取参数名
	 * @param field
	 * @return
	 */
	public static final String getParamKey(Field field){
		SerializedName serializedName = field.getAnnotation(SerializedName.class);
		if(serializedName != null && StringUtils.isNotEmpty(serializedName.value())){
			return serializedName.value();
		}else{
			return field.getName();
		}
	}
	
	/**
	 * 通过解析一个请求对象来获取请求地址
	 * @param priorUrl 默认的请求地址，如果priorUrl不为null也不为空将直接返回priorUrl
	 * @param requestObject 请求对象
	 * @return 请求地址
	 * @throws Exception 请求对象上既没有Url注解（或者值为空）也没有Host注解（或者值为空）
	 */
	public static final String getUrlFromRequestObject(String priorUrl, Object requestObject) throws Exception{
		if(StringUtils.isNotEmpty(priorUrl)){
			return priorUrl;
		}else{
			Class<?> requestClass = requestObject.getClass();
			
			/* 优先使用Url注解的值作为请求地址，如果没有Url注解再去用Host和Path注解来组合请求地址 */
			Url url = requestClass.getAnnotation(Url.class);
			String urlValue = url != null ? url.value() : null;
			if(StringUtils.isNotEmpty(urlValue)){
				return urlValue;
			}else{
				/* 如果有Host注解就继续，否则抛异常 */
				Host host = requestClass.getAnnotation(Host.class);
				String hostValue = host != null ? host.value() : null;
				if(StringUtils.isNotEmpty(hostValue)){
					/* 如果有Path注解就用Host注解的值拼接上Path注解的值作为请求地址，否则就只使用Host注解的值来作为请求地址 */
					Path path = requestClass.getAnnotation(Path.class);
					String pathValue = path != null ? path.value() : null;
					if(StringUtils.isNotEmpty(pathValue)){
						return hostValue + "/" + pathValue;
					}else{
						return hostValue;
					}
				}else{
					throw new Exception(requestClass.getName()+"上既没有Url注解（或者值为空）也没有Host注解（或者值为空）");
				}
			}
		}
	}
	
	/**
	 * 通过解析一个请求对象来获取请求地址
	 * @param requestObject 请求对象
	 * @return 请求地址，例如：http://www.baidu.com/index.html
	 * @throws Exception 请求对象上既没有Url注解（或者值为空）也没有Host注解（或者值为空）
	 */
	public static final String getUrlFromRequestObject(Object requestObject) throws Exception{
		return getUrlFromRequestObject(null, requestObject);
	}
	
	public static final String getUrlWithQueryString(String url, RequestParams params) {
        if(params != null) {
            String paramString = params.getParamString();
            if(StringUtils.isNotEmpty(paramString)){
            	if (url.indexOf("?") == -1) {
            		url += "?" + paramString;
            	} else {
            		url += "&" + paramString;
            	}
            }
        }
        return url;
    }
    
	public static final HttpEntity paramsToEntity(HttpApacheFac httpApacheFac, RequestParams params) {
		httpApacheFac.Log("请求参数："+params.getParamString());
		return params != null?params.getEntity():null;
    }

	public static final HttpEntityEnclosingRequestBase setEntity(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity, Header[] headers) {
        if(entity != null){
            requestBase.setEntity(entity);
        }
        if(headers != null){
        	requestBase.setHeaders(headers);
        }
        return requestBase;
    }

	public static final HttpEntityEnclosingRequestBase setEntity(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
        if(entity != null){
            requestBase.setEntity(entity);
        }
        return requestBase;
    }
	
	public static final HttpRequestBase setHeaders(HttpRequestBase httpRequest, Header[] headers){
		if(headers != null){
			httpRequest.setHeaders(headers);
		}
		return httpRequest;
	}
}