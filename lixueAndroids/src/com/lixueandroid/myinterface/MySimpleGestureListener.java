package com.lixueandroid.myinterface;

import android.view.MotionEvent;

public interface MySimpleGestureListener {
	public void onDown(MotionEvent e);
	public void onSingleUp(MotionEvent e);
	public void onUp(MotionEvent e);
}
