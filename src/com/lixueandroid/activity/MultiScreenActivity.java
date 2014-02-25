package com.lixueandroid.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;

public class MultiScreenActivity extends MyBaseActivity{
	public static  int screenWidth;
	public static  int screenHeight;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels ;
		screenHeight = metric.heightPixels;		
		System.out.println("screenWidth * scrrenHeight --->" + screenWidth + " * " +screenHeight);
		setContentView(R.layout.activity_multiscreen);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}

}
