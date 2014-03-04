package com.lixueandroid.activity;

import android.R;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.view.WaterWaveView;

/**
 * 水波动纹的效果
 * 
 * @author lixue
 *
 */
public class WaterWaveActivity extends MyBaseActivity{

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Black_NoTitleBar_Fullscreen);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(new WaterWaveView(getBaseContext()));
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}
}
