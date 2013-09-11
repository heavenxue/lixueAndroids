package com.lixueandroid.activity;

import java.lang.reflect.Type;

import me.xiaopan.easyandroid.app.BaseActivity;
import me.xiaopan.easynetwork.android.http.EasyHttpClient;
import me.xiaopan.easynetwork.android.http.Request;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.imgloader.ImageLoader;
import com.lixueandroid.net.AccessNetworkListener;
import com.lixueandroid.net.MyJsonReponseHandler;

public abstract class MyBaseActivity extends BaseActivity{
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				imageLoader.clearMemoryCache();
				return true;
			case R.id.item_clear_disc_cache:
				imageLoader.clearDiscCache();
				return true;
			default:
				return false;
		}
	}

	@Override
	public void onPreInit(Bundle savedInstanceState) {
	}

	@Override
	public boolean isRemoveTitleBar() {
		return true;
	}
	

	/**
	 * 显示消息对话框
	 * @param message 显示的消息
	 */
	protected void showMessageDialog(String message) {
		showMessageDialog(message, getString(R.string.base_confirm));
	}

	/**
	 * 显示消息对话框
	 * @param message 显示的消息的ID
	 */
	protected void showMessageDialog(int messageId) {
		showMessageDialog(getString(messageId));
	}

	
	/**
	 * 访问网络
	 * @param request 请求对象
	 * @param responseClass 响应对象
	 * @param accessNetworkListener 访问监听器
	 */
	public void accessNetwork(Request request, Class<?> responseClass, final AccessNetworkListener<?> accessNetworkListener){
		EasyHttpClient.getInstance().sendRequest(request, new MyJsonReponseHandler(this, responseClass, accessNetworkListener));
	}
	
	/**
	 * 访问网络
	 * @param request 请求对象
	 * @param responseClass 响应对象
	 * @param accessNetworkListener 访问监听器
	 */
	public void accessNetwork(Request request, Type responseType, final AccessNetworkListener<?> accessNetworkListener){
		EasyHttpClient.getInstance().sendRequest(request, new MyJsonReponseHandler(this, responseType, accessNetworkListener));
	}

	/**
	 * 设置Activity标题
	 * @param title 标题
	 * @return
	 */
	public boolean setActivityTitle(String title){
		boolean result = false;
		View view = findViewById(R.id.text_activityTitle);
		if(view != null && view instanceof TextView){
			((TextView) view).setText(title);
			result = true;
		}
		return result;
	}
	
	/**
	 * 设置Activity标题
	 * @param titleResId
	 * @return
	 */
	public boolean setActivityTitle(int titleResId){
		return setActivityTitle(getString(titleResId));
	}
}