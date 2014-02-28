package com.lixueandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class RectangleView extends View{
	private Path mpath;
	private Paint mpaint;
	public RectangleView(Context context) {
		super(context);
		init();
	}

	public RectangleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RectangleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	/**
	 *初始化 
	 */
	private void init(){
		mpath=new Path();
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				mpath.moveTo(getWidth(), getHeight());
				mpath.lineTo(getWidth(), 0);
				mpath.lineTo(0, 0);
				mpath.lineTo(0, getHeight());
				mpath.close();
			}
		});
		mpaint=new Paint();
		mpaint.setColor(Color.BLUE);
		mpaint.setAntiAlias(true);//消除锯齿
		mpaint.setStyle(Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath(mpath, mpaint);
		super.onDraw(canvas);
	}
	
}
