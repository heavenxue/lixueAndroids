package com.lixueandroid.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Surface;

/**
 * 窗口工具箱
 * 
 * @author xiaopan
 * 
 */
public class WindowUtils {
	/**
	 * 获取当前窗口的旋转角度
	 * @param activity
	 * @return
	 */
	public static int getDisplayRotation(Activity activity) {
		switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
			case Surface.ROTATION_0 : return 0;
			case Surface.ROTATION_90 : return 90;
			case Surface.ROTATION_180 : return 180;
			case Surface.ROTATION_270 : return 270;
			default : return 0;
		}
	}
	
	/**
	 * 当前是否是横屏
	 * @param context
	 * @return
	 */
	public static final boolean isLandscape(Context context){
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}
}