package com.lixueandroid.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;

public class ShowView extends MyBaseActivity{
	private Button mybutton;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_showview);
		mybutton=(Button) findViewById(R.id.mybutton);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		mybutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				File file= FileUtils.getFileFromDynamicCacheDir(getBaseContext(), "BusinessCardCache.jpeg");
				View dialogView = LayoutInflater.from(ShowView.this).inflate(R.layout.view_dialog, null);
//				ImageView showImageView=(ImageView) dialogView.findViewById(R.id.imageview_show);
//				showImageView.setImageURI(Uri.fromFile(file));
				AlertDialog alertDialog=new AlertDialog.Builder(ShowView.this).create();
				alertDialog.setView(dialogView, 0, 0, 0, 0);
				 //设置大小  
//		        WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();  
//		        layoutParams.width = 200;  
//		        layoutParams.height = LayoutParams.WRAP_CONTENT;  
//		        alertDialog.getWindow().setAttributes(layoutParams);  
		        alertDialog.show();	
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}

}
