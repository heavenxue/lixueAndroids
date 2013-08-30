package com.lixueandroid.net;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.conn.ConnectTimeoutException;

import android.content.Context;

import com.lixue.lixueandroid.R;

@SuppressWarnings("unchecked")
public abstract class AccessNetworkListener<T>{
	/**
	 * 处理成功
	 * @param responseObject
	 */
	public void handleSuccess(Object responseObject){
		onSuccess((T) responseObject);
	}
	
	/**
	 * 处理异常
	 * @param e 异常
	 * @param context
	 */
	public final void handleException(Throwable e, Context context){
		e.printStackTrace();
		if(e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException){
			onFailure(new ErrorInfo(ErrorInfo.CODE_EXCEPTION, context.getString(R.string.toast_network_connectionTimeout)));
		}else if(e instanceof UnknownHostException){
			onFailure(new ErrorInfo(ErrorInfo.CODE_EXCEPTION, context.getString(R.string.toast_network_connectException)));
		}else if(e instanceof ConnectException){
			onFailure(new ErrorInfo(ErrorInfo.CODE_EXCEPTION, context.getString(R.string.toast_network_connectException)));
		}else if(e instanceof FileNotFoundException){
			onFailure(new ErrorInfo(ErrorInfo.CODE_EXCEPTION, context.getString(R.string.toast_network_fileNotFoundException)));
		}else{
			onFailure(new ErrorInfo(ErrorInfo.CODE_EXCEPTION, context.getString(R.string.toast_network_unknownException)));
		}
	}
	
	/**
	 * 当开始访问
	 */
	public abstract void onStart();
	
	/**
	 * 当请求成功
	 * @param responseObject 响应对象
	 */
	public abstract void onSuccess(T responseObject);
	
	/**
	 * 当请求失败
	 * @param errorInfo 错误信息
	 */
	public abstract void onFailure(ErrorInfo errorInfo);
	
	/**
	 * 当结束访问
	 */
	public abstract void onEnd();
}