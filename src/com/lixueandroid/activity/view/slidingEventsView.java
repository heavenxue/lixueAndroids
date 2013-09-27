package com.lixueandroid.activity.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public  class slidingEventsView extends View implements OnTouchListener {
	/**
	 * 手势识别
	 */
	private GestureDetector detector;
	/**
	 * 向上滑动事件
	 */
	private OnUpFlingListener onUpFlingListener;
	/**
	 * 向下滑动事件
	 */
	private OnDownFlingListener onDownFlingListener;
	
	
	public slidingEventsView(Context context) {
		super(context);
		init();
	}

	public slidingEventsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public slidingEventsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return detector.onTouchEvent(event);
//		return false;
	}
	

	/**
	 * 初始化
	 */
	public void init() {
		detector = new GestureDetector(getContext(), new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {

			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {

			}
			// 在屏幕上滑动时
			// e1:第一个ACTION_DOWN事件（手指按下的那一点）
			// e2:最后一个ACTION_MOVE事件 （手指松开的那一点）
			// velocityX:手指在x轴移动的速度 单位：像素/秒
			// velocityY:手指在y轴移动的速度 单位：像素/秒
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,float velocityX, float velocityY) {
				int y = (int) (e2.getY() - e1.getY());
				//向下滑动
				if (y > 0) {
					onDownFlingListener.onDownFling();
				}
				//向上滑动
				else {
					onUpFlingListener.onUpFling();
				}
				return true;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}
			
		});
	}
	
	/**
	 * 返回手势管理器
	 * @return GestureDetector
	 */
	public GestureDetector getDetector() {
		return detector;
	}

	/**
	 * 设置向上滑动事件
	 * @param onUpFlingListener
	 */
	public void setOnUpFlingListener(OnUpFlingListener onUpFlingListener) {
		this.onUpFlingListener = onUpFlingListener;
	}

	/**
	 * 设置向下滑动事件
	 * @param onDownFlingListener
	 */
	public void setOnDownFlingListener(OnDownFlingListener onDownFlingListener) {
		this.onDownFlingListener = onDownFlingListener;
	}
}
