package com.lixueandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

public class RegularHexagonView extends ImageView implements OnClickListener
{
	private final String TAG = "RegularHexagonView";
	private Context ctx;
	private float radiusSquare = 0;//半径
	private float edgeLength = 0;//边的长度
	private float centerX,centerY;//中心点的坐标
	private Paint mPaint;//画笔
	private Path mPath;//路径
	private int defaultColor = Color.argb(0x40, 0xff, 0, 0);//默认颜色
	private int highColor = Color.argb(0x60, 0, 0, 0xff);//高亮颜色
	
	public RegularHexagonView(Context context) {
		super(context);
		init(context);
	}
	
	public RegularHexagonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context)
	{
		ctx = context;
		setClickable(true);
		setOnClickListener(this);
		mPath = new Path();
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				edgeLength=((float)getWidth())/2;
				radiusSquare=edgeLength*edgeLength*3/4;
				edgeLength = ((float)getWidth())/2;
				radiusSquare = edgeLength*edgeLength*3/4;
				centerX = edgeLength;
				centerY = ((float)getHeight())/2;
				
				mPath.moveTo(getWidth(), getHeight()/2);
				mPath.lineTo(getWidth()*3/4, 0);
				mPath.lineTo(getWidth()/4, 0);
				mPath.lineTo(0, getHeight()/2);
				mPath.lineTo(getWidth()/4, getHeight());
				mPath.lineTo(getWidth()*3/4, getHeight());
				mPath.close();
			}
		});
//		getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
//			public boolean onPreDraw() {
//				return true;
//			}
//		});
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);//消除锯齿，但不影响内部的结构
		mPaint.setStyle(Style.FILL);//充满
		mPaint.setColor(defaultColor);//
	}
	
	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick:tag="+getTag().toString());
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			mPaint.setColor(highColor);
			invalidate();
		}
		else if(event.getAction()==MotionEvent.ACTION_UP)
		{
			mPaint.setColor(defaultColor);
			invalidate();
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		float dist = (event.getX()-centerX)*(event.getX()-centerX)+(event.getY()-centerY)*(event.getY()-centerY);
		if(dist > radiusSquare)
		{
			return false;
		}
		return super.dispatchTouchEvent(event);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath(mPath, mPaint);
		canvas.save();
		super.onDraw(canvas);
	}
}