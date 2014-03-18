package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;

public class DownLoadActivity extends MyBaseActivity{
	private ImageView liduohaiImageView;
	private Button downloadButton;
	
	private String downloadUrl="http://t10.baidu.com/it/u=4280773587,241532391&fm=56";
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_download);
		liduohaiImageView=(ImageView) findViewById(R.id.img_liduohai);
		downloadButton=(Button) findViewById(R.id.button_download);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		downloadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}
}
