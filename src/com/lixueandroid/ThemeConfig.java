package com.lixueandroid;

import com.lixueandroid.ThemeConfig;
import android.app.Activity;
import android.content.Intent;

public class ThemeConfig {
	public static final int NIGHT=android.R.style.Theme_Black;
	public static final int DAY=android.R.style.Theme_Light;
	public static int mode=DAY;
	public static int getMode()
	{
		System.out.println("Get"+mode);
		return mode;
		
	}
	public static void setMode(int mode) {
		
		ThemeConfig.mode = mode;
	}
	
	/**
	 * 改变主题
	 */
	public static void changeMode()
	{
		if (mode==NIGHT) {
			mode=DAY;
		}else {
			mode=NIGHT;
		}
	}
	
	/**
	 * 重新加载activity
	 * @param activity
	 */
	public static void reload(Activity activity) {
		Intent intent = activity.getIntent();
		activity.overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		activity.finish();
		activity.overridePendingTransition(0, 0);
		activity.startActivity(intent);
	}
}