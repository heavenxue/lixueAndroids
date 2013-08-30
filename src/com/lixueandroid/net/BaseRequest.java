package com.lixueandroid.net;


import me.xiaopan.easynetwork.android.http.Host;
import me.xiaopan.easynetwork.android.http.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Host("http://192.168.2.254:8016")
public class BaseRequest implements Request{
	/**
	 * 手机类型。1：IOS；2：Android
	 */
	@Expose
	@SerializedName("mobileType")
	int mobilePhoneType = 2;
}