package com.lixueandroid.net;

import java.lang.reflect.Type;

import me.xiaopan.easy.network.android.EasyNetworkUtils;
import me.xiaopan.easy.network.android.http.EasyHttpClient;
import me.xiaopan.easy.network.android.http.HttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyJsonReponseHandler extends Handler implements HttpResponseHandler{
	private static final int MESSAGE_START = 0;	//开始
	private static final int MESSAGE_SUCCESS = 1;	//成功
	private static final int MESSAGE_ERROR = 5;	//错误
	private static final int MESSAGE_EXCEPTION = 3;	//异常
	private static final int MESSAGE_END = 4;	//结束
	private static final String STATUS = "status";
	private static final String STATUS_VALUE_SUCCESS = "success";
	private static final String RESULT = "result";
	private Context context;
	private Class<?> responseClass;
	private Type responseType;
	private AccessNetworkListener<?> accessNetworkListener;
	
	public MyJsonReponseHandler(Context context, Class<?> responseClass, AccessNetworkListener<?> accessNetworkListener){
		this.context = context;
		this.responseClass = responseClass;
		this.accessNetworkListener = accessNetworkListener;
	}
	
	public MyJsonReponseHandler(Context context, Type responseType, AccessNetworkListener<?> accessNetworkListener){
		this.context = context;
		this.responseType = responseType;
		this.accessNetworkListener = accessNetworkListener;
	}
	
	@Override
	public final void handleMessage(Message msg) {
		switch(msg.what) {
			case MESSAGE_START: if(accessNetworkListener != null) accessNetworkListener.onStart(); break;
			case MESSAGE_SUCCESS: if(accessNetworkListener != null) accessNetworkListener.handleSuccess(msg.obj); break;
			case MESSAGE_ERROR: if(accessNetworkListener != null) accessNetworkListener.onFailure((ErrorInfo) msg.obj); break;
			case MESSAGE_EXCEPTION: if(accessNetworkListener != null) accessNetworkListener.handleException((Throwable) msg.obj, context); break;
			case MESSAGE_END: if(accessNetworkListener != null) accessNetworkListener.onEnd(); break;
		}
	}
	
	@Override
	public final void start() {
		sendEmptyMessage(MESSAGE_START);
	}

	@Override
	public final void handleResponse(HttpResponse httpResponse) throws Throwable {
		if(httpResponse.getStatusLine().getStatusCode() < 300 ){
			HttpEntity httpEntity = httpResponse.getEntity();
			if(httpEntity != null){
				/* 读取返回的JSON字符串并转换成对象 */
				String jsonString = EntityUtils.toString(new BufferedHttpEntity(httpEntity), EasyNetworkUtils.getResponseCharset(httpResponse));
				EasyHttpClient.log("响应内容："+jsonString);
				if(jsonString != null && !"".equals(jsonString)){
					JSONObject responseJsonObject = new JSONObject(jsonString);
					//如果状态是成功
					if(STATUS_VALUE_SUCCESS.equals(responseJsonObject.getString(STATUS))){
						if(responseClass != null){
							sendMessage(obtainMessage(MESSAGE_SUCCESS, new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(responseJsonObject.getString(RESULT), responseClass)));
						}else if(responseType != null){
							sendMessage(obtainMessage(MESSAGE_SUCCESS, new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(responseJsonObject.getString(RESULT), responseType)));
						}else{
							sendMessage(obtainMessage(MESSAGE_EXCEPTION, new Exception("responseClass和responseType至少有一个不能为null")));
						}
					}else{
						sendMessage(obtainMessage(MESSAGE_ERROR, new Gson().fromJson(responseJsonObject.getString(RESULT), ErrorInfo.class)));
					}
				}else{
					sendMessage(obtainMessage(MESSAGE_EXCEPTION, new Exception("响应内容为空")));
				}
			}else{
				sendMessage(obtainMessage(MESSAGE_EXCEPTION, new Exception("没有响应实体")));
			}
		}else{
			sendMessage(obtainMessage(MESSAGE_EXCEPTION, new Exception("请求失败，状态码："+httpResponse.getStatusLine().getStatusCode())));
		}
	}

	@Override
	public final void exception(Throwable e) {
		sendMessage(obtainMessage(MESSAGE_EXCEPTION, e));
	}

	@Override
	public final void end() {
		sendEmptyMessage(MESSAGE_END);
	}
}