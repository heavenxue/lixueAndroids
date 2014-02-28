package com.lixueandroid.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.lixue.lixueandroid.R;

/**
 * Mp3播放器主界面
 * @author Administrator
 *
 */
public class Mp3PlayerMainActivity extends TabActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3playermain);
		//得到TabHost对象
		TabHost tabHost=getTabHost();
		//为tabHost中的内容作准备
		Intent intent=new Intent();
		intent.setClass(getBaseContext(), Mp3PlayerRemoteActivity.class);
		//TabHost的标签
		TabSpec tabspec=tabHost.newTabSpec("Remote");
		tabspec.setContent(intent);
		Resources rsc=getResources();
		tabspec.setIndicator("Remote", rsc.getDrawable(android.R.drawable.stat_sys_download));
		//添加一个Tab(远程的音乐)
		tabHost.addTab(tabspec);
		
		//添加另外一个tab(本地的音乐)
		Intent LocalInternt=new Intent();
		LocalInternt.setClass(getBaseContext(), Mp3PlayerLocalActivity.class);
		TabSpec tabSpec2=tabHost.newTabSpec("Local");
		tabSpec2.setContent(LocalInternt);
		tabSpec2.setIndicator("Local", rsc.getDrawable(android.R.drawable.stat_sys_upload_done));
		tabHost.addTab(tabSpec2);
	}
}
