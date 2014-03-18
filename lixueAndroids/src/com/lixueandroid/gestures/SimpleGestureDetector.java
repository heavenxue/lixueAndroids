package com.lixueandroid.gestures;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class SimpleGestureDetector extends SimpleOnGestureListener{
	private View view;
	private SimpleGestureListener simpleGestureListener;
	
	public SimpleGestureDetector(View view,SimpleGestureListener simpleGestureListener){
		this.view=view;
		this.simpleGestureListener=simpleGestureListener;
	}
	public interface SimpleGestureListener{
		public void onDown(MotionEvent motionEvent);
		public void onDoubleTab(MotionEvent motionEvent);
		public void onLongPress(MotionEvent motionEvent);
		public void onSingleTapUp(MotionEvent motionEvent);
		public void onUp(MotionEvent motionEvent);
	}
	/**
	 * 检查矩阵边界，防止滑动时超出边界
	 */
    public void checkMatrixBounds(){
//    		RectF rect = view.getDisplayRect();
//    		final float height = rect.height(), width = rect.width();
//    		float deltaX = 0, deltaY = 0;
//    		
//    		final int viewHeight = view.getAvailableHeight();
//    		if (height <= viewHeight) {
//    			deltaY = (viewHeight - height) / 2 - rect.top;
//    		} else if (rect.top > 0) {
//    			deltaY = -rect.top;
//    		} else if (rect.bottom < viewHeight) {
//    			deltaY = viewHeight - rect.bottom;
//    		}
//    		
//    		final int viewWidth = view.getAvailableWidth();
//    		if (width <= viewWidth) {
//    			deltaX = (viewWidth - width) / 2 - rect.left;
//    		} else if (rect.left > 0) {
//    			deltaX = -rect.left;
//    		} else if (rect.right < viewWidth) {
//    			deltaX = viewWidth - rect.right;
//    		} else {
//    		}
//    		
//    		view.getDrawMatrix().postTranslate(deltaX, deltaY);
	}
}
