package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.lixue.lixueandroid.R;

public class LiuBianXingActivity extends MyBaseActivity{
	private Boolean flag=true;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_liubianxing);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}
	
	@Override
	public void onAttachedToWindow() {
		 if(flag) {  
	            this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);  
	        }  
		super.onAttachedToWindow();
	}
//屏蔽home键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode == KeyEvent.KEYCODE_HOME){  
	            return true;  
	        }  
		return super.onKeyDown(keyCode, event);
	}
}
