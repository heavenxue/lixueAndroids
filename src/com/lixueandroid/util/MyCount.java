package com.lixueandroid.util;

import android.os.CountDownTimer;


/**
 * 倒计时类
 * @author lixue
 *
 */
public class MyCount extends CountDownTimer {
		private OnFinishListener onFinishListener;
		private OnTickListener onTickListener;
		public MyCount(long millisInFuture, long countDownInterval) { 
		super(millisInFuture, countDownInterval); 
	} 
	@Override
	public void onFinish() { 
		onFinishListener.OnFinish();
	} 
	@Override
	public void onTick(long millisUntilFinished) { 
		onTickListener.onTick();
	} 
}