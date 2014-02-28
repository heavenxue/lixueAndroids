package com.lixueandroid.activity;

import android.os.Bundle;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.view.Countdown;

public class CountDownActivity extends MyBaseActivity{
	private Button cButton;
	private Countdown countDown;
	
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_countdown);
		cButton=(Button) findViewById(R.id.button_countdown_countdown);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		countDown=new Countdown(cButton,"倒计时按钮","倒计时%s秒");
		countDown.start();
	}
}
