/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.lixueandroid.runnable;

import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

public class ScrollRunnable implements java.lang.Runnable{

	private Scroller mScroller;
	
	public ScrollRunnable(Context context, int xOffset, int yOffset){
		mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
		mScroller.startScroll(0, 0, xOffset, yOffset, 300);
	}
	
	@Override
	public void run() {
	}
} 