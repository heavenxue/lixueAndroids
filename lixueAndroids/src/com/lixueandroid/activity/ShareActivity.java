package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;

/**
 * 分享
 * @author lixue
 *
 */
public class ShareActivity extends MyBaseActivity{
	private Button shareButton;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_share);
		shareButton=(Button) findViewById(R.id.button_share);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
	}
}
