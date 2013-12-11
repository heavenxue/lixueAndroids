package com.lixueandroid.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;

public class AllDrawActivity extends MyBaseActivity{
//	private Button drawCircleButton;
	private int windowWidth;
	private int windowHeight;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.acitivity_alldraw);
//		drawCircleButton=(Button) findViewById(R.id.draw_button);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
//		drawCircleButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Bitmap mbitmap=Bitmap.createBitmap(windowWidth,windowHeight, Config.ARGB_8888);
//				Canvas mCanvas=new Canvas(mbitmap);
//				mCanvas.save();
//				
//				Paint mpaint=new Paint();
//				mpaint.setColor(Color.RED);
//				mCanvas.drawColor(Color.YELLOW);
//				
//				mCanvas.drawLine(0, 20, 300, 300, mpaint);
//				mCanvas.drawCircle(100, 100, 200, mpaint);
//				mCanvas.restore();
//			}
//		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		windowWidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
		windowHeight = wm.getDefaultDisplay().getHeight();//屏幕高度
	}

}
