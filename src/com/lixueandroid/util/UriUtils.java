package com.lixueandroid.util;

import android.net.Uri;

/**
 * URI工具箱
 * @author xiaopan
 *
 */
public class UriUtils {
	public static final String URI_TEL = "tel:";
	public static final String URI_SMS = "smsto:";
	/**
	 * 获取呼叫给定的电话号码时用的Uri
	 * @param phoneNumber 给定的电话号码
	 * @return 呼叫给定的电话号码时用的Uri
	 */
	public static Uri getCallUri(String phoneNumber){
		return Uri.parse(URI_TEL+(phoneNumber!=null?phoneNumber:""));
	}
	
	/**
	 * 获取短信Uri
	 * @param mobileNumber 目标手机号
	 * @return
	 */
	public static Uri getSmsUri(String mobileNumber){
		return Uri.parse(URI_SMS+(mobileNumber!=null?mobileNumber:""));
	}
}