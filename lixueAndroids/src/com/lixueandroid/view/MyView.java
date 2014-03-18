package com.lixueandroid.view;

import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.lixue.lixueandroid.R;

public class MyView extends View implements OnGestureListener{
	int nGameState = 0;
	private Paint mPaint;
	String mTime = null;
	String mDate = null;
	Date now;
	private Bitmap mSlide;
	private Bitmap mFrame;
	private Bitmap mSlide_demo;
	private Bitmap mFrame_demo;
	int SgreenWidth = 0;
	int SgreenHeight = 0;
	int movex = 0;
	int movex_demo = 0;
	int movex_demo_1 = 0; 
	private GestureDetector detector;
	boolean isIn = false;
	boolean first= true;
	////    public boolean isTouch = false;

@SuppressWarnings("deprecation")
public MyView(Context context) {
		super(context);
		init();
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	@SuppressWarnings("deprecation")
	private void init(){
		mPaint = new Paint();
		now = new Date();
		//Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon); 
		// 绘图 
		//canvas.drawBitmap(bitmap, 10, 10, paint); 
		mSlide = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.animation1)).getBitmap();
		mFrame = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.animation2)).getBitmap();
		mSlide_demo = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.animation3)).getBitmap();
		mFrame_demo = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.animation4)).getBitmap();
		detector = new GestureDetector(this);	
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		SgreenWidth = this.getWidth();
		SgreenHeight = this.getHeight();
		movex_demo_1 = this.getWidth()*17/200;
		Log.d("LL", "SgreenWidth = " + SgreenWidth);
		Log.d("LL", "SgreenHeight = " + SgreenHeight);
		mTime=System.currentTimeMillis()+"";
		//mTime=DateFormat.getTimeFormat(getContext()).format(now);
		mDate=DateFormat.getDateFormat(getContext()).format(now);
		//mPaint.setARGB(255, 255, 255, 255);
		mPaint.setTextSize(36); 
		canvas.drawText(mTime, 120, 40, mPaint);
		mPaint.setTextSize(18);
		canvas.drawText(mDate, 120, 70, mPaint);
		mPaint.setColor(Color.GREEN);
		mPaint.setTextSize(24); 
		canvas.drawText("Slide to Unlock", 120, 422, mPaint);
		
		if(first){
		    this.DrawImage(canvas, mFrame_demo, 0, (SgreenHeight*(200-31)/200)+1, mFrame_demo.getWidth(), mFrame_demo.getHeight(), SgreenWidth, SgreenHeight*31/200);
				
		    this.DrawImage(canvas, mSlide_demo, movex_demo_1, SgreenHeight*(400-37)/400, 
		            mSlide_demo.getWidth(), mSlide_demo.getHeight(), SgreenWidth*11/60, SgreenHeight*29/400);    
		}else{
		    this.DrawImage(canvas, mFrame_demo, 0, (SgreenHeight*(200-31)/200)+1, 
		            mFrame_demo.getWidth(), mFrame_demo.getHeight(), SgreenWidth, SgreenHeight*31/200);
				
		    this.DrawImage(canvas, mSlide_demo, movex_demo, SgreenHeight*(400-37)/400, 
		            mSlide_demo.getWidth(), mSlide_demo.getHeight(), SgreenWidth*11/60, SgreenHeight*29/400);
		}
				
		if(movex_demo >= SgreenWidth){
		    movex_demo = SgreenWidth*17/240;
		}
				
		first = false;
		super.onDraw(canvas);
	}
	
	 public void refreshTimePaint(){
		Log.d("LL", "movex_demo = " + movex_demo);
		this.postInvalidate(); //使用postInvalidate();刷新 
		System.out.println(System.currentTimeMillis());
		System.out.println(nGameState);
	 }
	 
		/**
   *x,y 起始坐标 ;w,h 图片剪裁宽、高;bx,by目标区域;
   * @param canvas
   * @param mBitmap
   * @param x
   * @param y
   * @param w
   * @param h
   * @param bx
   * @param by
   */
	public void DrawImage(Canvas canvas, Bitmap mBitmap, int x, int y, int w,int h, int bx, int by) {
		Rect src = new Rect();// 图片裁剪区域
		Rect dst = new Rect();// 屏幕裁剪区域
		src.left = 0;
		src.top = 0;
		src.right = w;
		src.bottom = h;
		dst.left = x;
		dst.top = y;
		dst.right = x+bx;
		dst.bottom = y+by;
		canvas.drawBitmap(mBitmap, src, dst, mPaint);
		src = null;
		dst = null;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		 Log.d("LL", "onFling e1.getX() = " + e1.getX());
		 Log.d("LL", "onFling e2.getX() = " + e2.getX());
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.d("LL", "onLongPress(MotionEvent e) " );
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		Log.d("LL", "onScroll e1.getX() = " + e1.getX());
		Log.d("LL", "onScroll e2.getX() = " + e2.getX());
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.d("LL", "onShowPress(MotionEvent e) " );
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d("LL", "onSingleTapUp(MotionEvent e" );
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		 int action = event.getAction();
		 Log.i("LL","x1: "+event.getX()+"y1: "+event.getY());
		 Log.d("LL", "isIn = " + isIn);
		 if (action == MotionEvent.ACTION_MOVE) {
			 Log.i("LL","x2: "+event.getX()+"y2: "+event.getY());
			 
			 if(isIn /*&& isTouch*/){
				  movex_demo = (int)event.getX();
				  refreshTimePaint();
			 }
		 }
		
		 else if (action == MotionEvent.ACTION_DOWN) {
			 if(event.getX() > SgreenWidth*17/240 &&event.getX() < SgreenWidth*61/240){
				 isIn = true;
			 }else{
				 isIn = false;
			 }
		 }
		 else if (action == MotionEvent.ACTION_UP) {
			 if (movex_demo <= this.getWidth()/2) 
			 {
				  movex_demo = SgreenWidth*17/240;
				  refreshTimePaint();
			 }
			 
			 else if(movex_demo > this.getWidth()/2){
				  movex_demo = this.getWidth()*179/240;
				  refreshTimePaint(); 
			 }
		 }
		
		 this.detector.onTouchEvent(event);
		 return true; //super.onTouchEvent(event);
	}

}
