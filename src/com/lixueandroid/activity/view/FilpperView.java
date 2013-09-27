package com.lixueandroid.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import com.lixue.lixueandroid.R;

/**
 * 上下滑屏控件
 * 
 * @author lixue
 *
 */
public class FilpperView extends ViewFlipper{
	/**
	 * 切屏控件
	 */
	private ViewFlipper viewFlipper;
	/**
	 * 手势识别
	 */
	private GestureDetector detector; 

	public FilpperView(Context context) {
		super(context);
		Init();
	}
	
	public FilpperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}

	/**
	 * 初始化视图
	 */
	private void Init(){
		viewFlipper=(ViewFlipper) inflate(getContext(), R.layout.flipper, null);
		addView(viewFlipper);
		viewFlipper.setLongClickable(true); 
		
		viewFlipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				detector.onTouchEvent(event); 
				return false;
			}
		});
		
		detector=new GestureDetector(getContext(),new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				
			}
			 //在屏幕上滑动时  
            //e1:第一个ACTION_DOWN事件（手指按下的那一点）  
            //e2:最后一个ACTION_MOVE事件 （手指松开的那一点）  
            //velocityX:手指在x轴移动的速度 单位：像素/秒  
            //velocityY:手指在y轴移动的速度 单位：像素/秒  
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
				 int y=(int)(e2.getY()-e1.getY());
                 if(y>0){ 
                	 viewFlipper.setInAnimation(getContext(),R.anim.in_up_down ); 
                	 viewFlipper.setOutAnimation(getContext(),R.anim.out_up_down ); 
                	 viewFlipper.showPrevious();                      
                 }else{ 
                	 viewFlipper.setInAnimation(getContext(),R.anim.in_down_up ); 
                	 viewFlipper.setOutAnimation(getContext(),R.anim.out_down_up ); 
                	 viewFlipper.showNext();                  
                 } 
                 return true; 
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}
}
