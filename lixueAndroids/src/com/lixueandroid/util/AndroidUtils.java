package com.lixueandroid.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Android工具箱
 * @author panpf
 *
 */
public class AndroidUtils {
	public static final String SMS_BODY = "sms_body";
	
	/**
	 * 吐出一个显示时间较长的提示
	 * @param context 上下文对象
	 * @param resId 显示内容资源ID
	 */
	public static final void toastL(Context context, int resId){
		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 吐出一个显示时间较短的提示
	 * @param context 上下文对象
	 * @param resId 显示内容资源ID
	 */
	public static final void toastS(Context context, int resId){
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 吐出一个显示时间较长的提示
	 * @param context 上下文对象
	 * @param content 显示内容
	 */
	public static final void toastL(Context context, String content){
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 吐出一个显示时间较短的提示
	 * @param context 上下文对象
	 * @param content 显示内容
	 */
	public static final void toastS(Context context, String content){
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 吐出一个显示时间较长的提示
	 * @param context 上下文对象
	 * @param formatResId 被格式化的字符串资源的ID
	 * @param args 参数数组
	 */
	public static final void toastL(Context context, int formatResId, Object... args){
		Toast.makeText(context, String.format(context.getString(formatResId), args), Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 吐出一个显示时间较短的提示
	 * @param context 上下文对象
	 * @param formatResId 被格式化的字符串资源的ID
	 * @param args 参数数组
	 */
	public static final void toastS(Context context, int formatResId, Object... args){
		Toast.makeText(context, String.format(context.getString(formatResId), args), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 吐出一个显示时间较长的提示
	 * @param context 上下文对象
	 * @param format 被格式化的字符串
	 * @param args 参数数组
	 */
	public static final void toastL(Context context, String format, Object... args){
		Toast.makeText(context, String.format(format, args), Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 吐出一个显示时间较短的提示
	 * @param context 上下文对象
	 * @param format 被格式化的字符串
	 * @param args 参数数组
	 */
	public static final void toastS(Context context, String format, Object... args){
		Toast.makeText(context, String.format(format, args), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 * @param isClose fromActivity在跳转完成后是否关闭
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int flag, Bundle bundle, boolean isClose, int inAnimation, int outAnimation){
		Intent intent = new Intent(fromActivity, targetActivity);
		if(flag != -5){
			intent.setFlags(flag);
		}
		if(bundle != null){
			intent.putExtras(bundle);
		}
		fromActivity.startActivity(intent);
		if(inAnimation >0 && outAnimation >0){
			fromActivity.overridePendingTransition(inAnimation, outAnimation);
		}
		if(isClose){
			fromActivity.finish();
		}
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 * @param isClose fromActivity在跳转完成后是否关闭
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int flag, Bundle bundle, boolean isClose){
		startActivity(fromActivity, targetActivity, flag, bundle, isClose, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int flag, Bundle bundle, int inAnimation, int outAnimation){
		startActivity(fromActivity, targetActivity, flag, bundle, false, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 * @param isClose fromActivity在跳转完成后是否关闭
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int flag, boolean isClose, int inAnimation, int outAnimation){
		startActivity(fromActivity, targetActivity, flag, null, isClose, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 * @param isClose fromActivity在跳转完成后是否关闭
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, Bundle bundle, boolean isClose, int inAnimation, int outAnimation){
		startActivity(fromActivity, targetActivity, -5, bundle, isClose, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int flag, Bundle bundle){
		startActivity(fromActivity, targetActivity, flag, bundle, false, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 * @param isClose fromActivity在跳转完成后是否关闭
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int flag, boolean isClose){
		startActivity(fromActivity, targetActivity, flag, null, isClose, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 * @param isClose fromActivity在跳转完成后是否关闭
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, Bundle bundle, boolean isClose){
		startActivity(fromActivity, targetActivity, -5, bundle, isClose, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int flag, int inAnimation, int outAnimation){
		startActivity(fromActivity, targetActivity, flag, null, false, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, Bundle bundle, int inAnimation, int outAnimation){
		startActivity(fromActivity, targetActivity, -5, bundle, false, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param isClose fromActivity在跳转完成后是否关闭
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, boolean isClose, int inAnimation, int outAnimation){
		startActivity(fromActivity, targetActivity, -5, null, isClose, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int inAnimation, int outAnimation){
		startActivity(fromActivity, targetActivity, -5, null, false, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param isClose fromActivity在跳转完成后是否关闭
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, boolean isClose){
		startActivity(fromActivity, targetActivity, -5, null, isClose, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, Bundle bundle){
		startActivity(fromActivity, targetActivity, -5, bundle, false, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity, int flag){
		startActivity(fromActivity, targetActivity, flag, null, false, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 */
	public static void startActivity(Activity fromActivity, Class<?> targetActivity){
		startActivity(fromActivity, targetActivity, -5, null, false, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param context 上下文
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 */
	public static void startActivity(Context context, Class<?> targetActivity, int flag, Bundle bundle){
		Intent intent = new Intent(context, targetActivity);
		if(flag != -5){
			intent.setFlags(flag);
		}
		if(bundle != null){
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}
	
	/**
	 * 启动Activity
	 * @param context 上下文
	 * @param targetActivity 目标Activity
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 */
	public static void startActivity(Context context, Class<?> targetActivity, Bundle bundle){
		startActivity(context, targetActivity, -5, bundle);
	}
	
	/**
	 * 启动Activity
	 * @param context 上下文
	 * @param targetActivity 目标Activity
	 * @param flag Intent标记。-5：不添加标记
	 */
	public static void startActivity(Context context, Class<?> targetActivity, int flag){
		startActivity(context, targetActivity, flag, null);
	}
	
	/**
	 * 启动Activity
	 * @param context 上下文
	 * @param targetActivity 目标Activity
	 */
	public static void startActivity(Context context, Class<?> targetActivity){
		startActivity(context, targetActivity, -5, null);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode 请求码
	 * @param flag Intent标记。-5：不添加标记
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivityForResult(Activity fromActivity, Class<?> targetActivity, int requestCode, int flag, Bundle bundle, int inAnimation, int outAnimation){
		Intent intent = new Intent(fromActivity, targetActivity);
		if(flag != -5){
			intent.setFlags(flag);
		}
		if(bundle != null){
			intent.putExtras(bundle);
		}
		fromActivity.startActivityForResult(intent, requestCode);
		if(inAnimation >0 && outAnimation >0){
			fromActivity.overridePendingTransition(inAnimation, outAnimation);
		}
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode 请求码
	 * @param flag Intent标记。-5：不添加标记
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 */
	public static void startActivityForResult(Activity fromActivity, Class<?> targetActivity, int requestCode, int flag, Bundle bundle){
		startActivityForResult(fromActivity, targetActivity, requestCode, flag, bundle, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode 请求码
	 * @param flag Intent标记。-5：不添加标记
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivityForResult(Activity fromActivity, Class<?> targetActivity, int requestCode, int flag, int inAnimation, int outAnimation){
		startActivityForResult(fromActivity, targetActivity, requestCode, flag, null, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode 请求码
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivityForResult(Activity fromActivity, Class<?> targetActivity, int requestCode, Bundle bundle, int inAnimation, int outAnimation){
		startActivityForResult(fromActivity, targetActivity, requestCode, -5, bundle, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode 请求码
	 * @param flag Intent标记。-5：不添加标记
	 */
	public static void startActivityForResult(Activity fromActivity, Class<?> targetActivity, int requestCode, int flag){
		startActivityForResult(fromActivity, targetActivity, requestCode, flag, null, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode 请求码
	 * @param bundle 在跳的过程中要传的数据，为null的话不传
	 */
	public static void startActivityForResult(Activity fromActivity, Class<?> targetActivity, int requestCode, Bundle bundle){
		startActivityForResult(fromActivity, targetActivity, requestCode, -5, bundle, -5, -5);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode 请求码
	 * @param inAnimation targetActivity的进入动画。inAnimation和fromActivity都大于0才会使用动画
	 * @param outAnimation fromActivity的出去动画。inAnimation和fromActivity都大于0才会使用动画
	 */
	public static void startActivityForResult(Activity fromActivity, Class<?> targetActivity, int requestCode, int inAnimation, int outAnimation){
		startActivityForResult(fromActivity, targetActivity, requestCode, -5, null, inAnimation, outAnimation);
	}
	
	/**
	 * 启动Activity
	 * @param fromActivity 来源Activity
	 * @param targetActivity 目标Activity
	 * @param requestCode 请求码
	 */
	public static void startActivityForResult(Activity fromActivity, Class<?> targetActivity, int requestCode){
		startActivityForResult(fromActivity, targetActivity, requestCode, -5, null, -5, -5);
	}
	
	/**
	 * 获取当前屏幕的尺寸
	 * @param context
	 * @return
	 */
	public static Size getScreenSize(Context context){
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Size size = new Size(display.getWidth(), display.getHeight());
		return size;
	}
	
	/**
	 * 打开拨号界面，需要CALL_PHONE权限
	 * @param phoneNumber 要呼叫的电话号码
	 * @param activity Activity对象，需要依托于Activity所在的主线程才能打开拨号界面
	 */
	public static void openDialingInterface(Activity activity, String phoneNumber){
		activity.startActivity(new Intent(Intent.ACTION_DIAL, UriUtils.getCallUri(phoneNumber)));
	}
	
	/**
	 * 呼叫给定的电话号码，需要CALL_PHONE权限
	 * @param activity Activity对象，需要依托于Activity所在的主线程才能呼叫给定的电话
	 * @param phoneNumber 要呼叫的电话号码
	 */
	public static void call(Activity activity, String phoneNumber){
		activity.startActivity(new Intent(Intent.ACTION_CALL, UriUtils.getCallUri(phoneNumber)));
	}
	
	/**
	 * 打开给定的页面
	 * @param activity Activity对象，需要依托于Activity所在的主线程才能打开给定的页面
	 * @param url 要打开的web页面的地址
	 */
	public static void openWebBrowser(Activity activity, String url){
		activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}
	
	/**
	 * 打开发送短信界面
	 * @param activity Activity对象，需要依托于Activity所在的主线程才能打开给定的页面
	 * @param mobileNumber 目标手机号
	 * @param messageConten 短信内容
	 */
	public static void openSmsInterface(Activity activity, String mobileNumber, String messageConten){
		Intent intent = new Intent(Intent.ACTION_SENDTO, UriUtils.getSmsUri(mobileNumber));
		intent.putExtra(SMS_BODY, messageConten);
		activity.startActivity(intent);
	}
	
	/**
	 * 发送短信，需要SEND_SMS权限
	 * @param context 上下文
	 * @param number 电话号码
	 * @param messageContent 短信内容，如果长度过长将会发多条发送
	 */
	public static void sendSms(Context context, String number, String messageContent){
		SmsManager smsManager = SmsManager.getDefault();
		List<String> contentList = smsManager.divideMessage(messageContent);
		for(String content : contentList){
			smsManager.sendTextMessage(number, null, content, null, null);
		}
	}
	
	/**
	 * 获取所有联系人的姓名和电话号码，需要READ_CONTACTS权限
	 * @param context 上下文
	 * @return Cursor。姓名：CommonDataKinds.Phone.DISPLAY_NAME；号码：CommonDataKinds.Phone.NUMBER
	 */
	public static Cursor getContactsNameAndNumber(Context context){
		return context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {
				CommonDataKinds.Phone.DISPLAY_NAME, CommonDataKinds.Phone.NUMBER}, null, null, CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
	}
	
	/**
	 * 为给定的编辑器开启软键盘
	 * @param context 
	 * @param editText 给定的编辑器
	 */
	public static void openSoftKeyboard(Context context, EditText editText){
		editText.requestFocus();
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
		ViewUtils.setSelectionToEnd(editText);
	}
	
	/**
	 * 关闭软键盘
	 * @param context
	 */
	public static void closeSoftKeyboard(Activity activity){
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		//如果软键盘已经开启
		if(inputMethodManager.isActive()){
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 切换软键盘的状态
	 * @param context
	 */
	public static void toggleSoftKeyboardState(Context context){
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 获取设备ID
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context){
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}
	
	/**
	 * dp单位转换为px
	 * @param context 上下文，需要通过上下文获取到当前屏幕的像素密度
	 * @param dpValue dp值
	 * @return
	 */
	public static int dp2px(Context context, float dpValue){
		return (int)(dpValue * (context.getResources().getDisplayMetrics().density) + 0.5f);
	}
	
	/**
	 * px单位转换为dp
	 * @param context 上下文，需要通过上下文获取到当前屏幕的像素密度
	 * @param pxValue px值
	 * @return
	 */
	public static int px2dp(Context context, float pxValue){
		return (int)(pxValue / (context.getResources().getDisplayMetrics().density) + 0.5f);
	}
}