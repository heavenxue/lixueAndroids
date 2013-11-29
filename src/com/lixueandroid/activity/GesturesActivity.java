package com.lixueandroid.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lixue.lixueandroid.R;

public class GesturesActivity extends MyBaseActivity{
	private Button showimgButton;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	
	private Bitmap bitmap;
	private ScaleGestureDetector scaleGestureDetector;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.acitivity_gestures);
		showimgButton=(Button) findViewById(R.id.button_gestures_showimg);
		surfaceView=(SurfaceView) findViewById(R.id.surfaceview_gestures);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		showimgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bitmap=BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.liduohai1);
				Canvas canvas=surfaceHolder.lockCanvas();
				canvas.drawBitmap(bitmap, 0f, 300f, null);
				surfaceHolder.unlockCanvasAndPost(canvas);
				//再重新锁一次(一会再重新注释掉看看，到底有什么不同)
				surfaceHolder.lockCanvas(new Rect(0,0,0,0));
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		surfaceHolder=surfaceView.getHolder();
		Matrix matrix=new Matrix();
		scaleGestureDetector=new ScaleGestureDetector(this, new myScaleGestureListener());
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		return super.onTouchEvent(event);
		return scaleGestureDetector.onTouchEvent(event);
	}


	private class myScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener{

		/* 
		 * 缩放
		 */
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			Matrix matrix=new Matrix();
			float factor=detector.getScaleFactor()/3;
//			matrix.setScale(factor, factor);
			//如果缩放系数比1.0f还小，就让其为0.5f
//			if(currentFactor * newScaleFactor < factors[0]/2){
//				newScaleFactor = factors[0]/2/currentFactor;
//				currentFactor = factors[0]/2;
//			}else if(currentFactor * newScaleFactor > factors[factors.length - 1]){//如果缩放系统比3.0f还大，那么就就让其为3.0f
//				newScaleFactor = factors[factors.length - 1]/currentFactor;
//				currentFactor = factors[factors.length - 1];
//			}else{//否则主为它本身自己的缩放系数
//				currentFactor *= newScaleFactor;
//			}
			matrix.postScale(factor,factor, detector.getFocusX(), detector.getFocusY());
			Canvas mCanvas=surfaceHolder.lockCanvas();
			//清屏
			mCanvas.drawColor(Color.BLACK);
			//画缩放后的图
			mCanvas.drawBitmap(bitmap, matrix, null);
			//绘制完成，提交修改
			surfaceHolder.unlockCanvasAndPost(mCanvas);
			//重新锁一次？？？
			surfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
			surfaceHolder.unlockCanvasAndPost(mCanvas);
			return false;
		}

		/* 
		 * 缩放之前
		 */
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		/* 
		 * 缩放结束
		 */
		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			
		}
	}
}
