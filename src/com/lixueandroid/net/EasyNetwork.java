package com.lixueandroid.net;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class EasyNetwork {
	public static final String CHARSET_NAME_UTF8 = "UTF-8";
    private static ThreadPoolExecutor threadPool;	//线程池

	public static ThreadPoolExecutor getThreadPool() {
		if(threadPool == null){
			threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		}
		return threadPool;
	}

	public static void setThreadPool(ThreadPoolExecutor threadPool) {
		EasyNetwork.threadPool = threadPool;
	}
}