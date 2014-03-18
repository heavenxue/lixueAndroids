package com.lixueandroid.activity;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.view.ScaleImageView;

public class GuidActivity extends MyBaseActivity{
	private ScaleImageView guidImageView;
	private ViewTreeObserver viewTreeObserver;
	private int state_height;
	private int window_height;
	private int window_width;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();
		guidImageView=(ScaleImageView) findViewById(R.id.img_myimg);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		Bitmap bmp = ReadBitmapById(this,R.drawable.hall1,window_width, window_height);
		 Canvas canvas = new Canvas(bmp);
		  Paint paint = new Paint();
		  paint.setAntiAlias(true);//设置画笔无锯齿(如果不设置可以看到效果很差)
		  /*设置paint的　style　为STROKE：空心*/
		  paint.setStyle(Paint.Style.FILL);
		  paint.setColor(Color.RED);
		  paint.setAlpha(125);
		  canvas.drawRect(100, 100, 50, 50, paint);
		  
		  canvas.save(Canvas.ALL_SAVE_FLAG);
		  // 存储新合成的图片
		  canvas.restore();
		// 设置图像
		guidImageView.setImageBitmap(bmp);
		guidImageView.setmActivity(GuidActivity.this);//设置activity.
		/** 测量状态栏高度 **/  
       viewTreeObserver = guidImageView.getViewTreeObserver();  
       viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
 
           @Override  
           public void onGlobalLayout() {  
               if (state_height == 0) {  
                   // 获取状态栏高度  
                   Rect frame = new Rect();  
                   getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
                   state_height = frame.top;  
                   guidImageView.setScreen_H(window_height-state_height);  
                   guidImageView.setScreen_W(window_width);  
               }  
           }  
       });  
	}
	
	public static Bitmap ReadBitmapById(Context context, int drawableId,int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inInputShareable = true;
		options.inPurgeable = true;
		InputStream stream = context.getResources().openRawResource(drawableId);
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		return getBitmap(bitmap, screenWidth, screenHight);
	}
	
	public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Log.e("jj", "图像原宽：" + w + ",screenWidth=" + screenWidth);
		Matrix matrix = new Matrix();
		float scale = (float) screenWidth / w;
		float scale2 = (float) screenHight / h;
		// scale = scale < scale2 ? scale : scale2;
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}
}
