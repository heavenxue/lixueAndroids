package com.lixueandroid.activity;

import me.xiaopan.easyjava.util.StringUtils;
import android.os.Bundle;
import android.widget.ImageView;

import com.lixue.lixueandroid.R;

public class ShowDetailImgActivity extends MyBaseActivity{
	public static final String PRAM_REQUESTED_STRING_URL="PRAM_REQUESTED_STRING_URL";
	private ImageView imgview;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.grid_detail_img);
		imgview=(ImageView) findViewById(R.id.img_detail);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
		if(StringUtils.isNotNullAndEmpty(getIntent().getStringExtra(PRAM_REQUESTED_STRING_URL))){
			//imageLoader.displayImage(getIntent().getStringExtra(PRAM_REQUESTED_STRING_URL),imgview);
			getMyApplication().getImageLoader().displayImage(getIntent().getStringExtra(PRAM_REQUESTED_STRING_URL),imgview);
		}
	}

}
