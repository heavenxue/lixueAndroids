package com.lixueandroid.view;

import android.os.Handler;
import android.widget.TextView;

/**
 * 倒计时器
 */
public class Countdown implements Runnable {
	private int remainingSeconds;
	private int currentRemainingSeconds;
	private String defaultText; 
	private String countdownText;
	private TextView showTextView;
	private Handler handler;
	private CountdownListener countdownListener;
	
	/**
	 * 创建一个倒计时器
	 * @param showTextView 显示倒计时的文本视图
	 * @param defaultText 没有倒计时时显示的内容，例如："获取验证码"
	 * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
	 * @param remainingSeconds 倒计时秒数，例如：60，就是从60开始倒计时一直到0结束
	 * @param handler 消息处理器
	 */
	public Countdown(TextView showTextView, String defaultText, String countdownText, int remainingSeconds, Handler handler){
		this.showTextView = showTextView;
		this.defaultText = defaultText;
		this.countdownText = countdownText;
		this.remainingSeconds = remainingSeconds;
		this.handler = handler;
	}
	
	/**
	 * 创建一个倒计时器
	 * @param showTextView 显示倒计时的文本视图
	 * @param defaultText 没有倒计时时显示的内容，例如："获取验证码"
	 * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
	 * @param remainingSeconds 倒计时秒数，例如：60，就是从60开始倒计时一直到0结束
	 */
	public Countdown(TextView showTextView, String defaultText, String countdownText, int remainingSeconds){
		this(showTextView, defaultText, countdownText, remainingSeconds, new Handler());
	}
	
	/**
	 * 创建一个倒计时器，默认60秒
	 * @param showTextView 显示倒计时的文本视图
	 * @param defaultText 没有倒计时时显示的内容，例如："获取验证码"
	 * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
	 * @param handler 消息处理器
	 */
	public Countdown(TextView showTextView, String defaultText, String countdownText, Handler handler){
		this(showTextView, defaultText, countdownText, 60, handler);
	}
	
	/**
	 * 创建一个倒计时器，默认60秒
	 * @param showTextView 显示倒计时的文本视图
	 * @param defaultText 没有倒计时时显示的内容，例如："获取验证码"
	 * @param countdownText 倒计时中显示的内容，例如："%s秒后重新获取验证码"，在倒计时的过程中会用剩余描述替换%s
	 */
	public Countdown(TextView showTextView, String defaultText, String countdownText){
		this(showTextView, defaultText, countdownText, 60, new Handler());
	}
	
	@Override
	public void run() {
		if(currentRemainingSeconds > 0){
			showTextView.setText(String.format(countdownText, currentRemainingSeconds--));
			handler.postDelayed(this, 1000);
		}else{
			stop();
		}
	}
	
	public void start(){
		currentRemainingSeconds = remainingSeconds;
		showTextView.setEnabled(false);
		handler.removeCallbacks(this);
		handler.post(this);
		if(countdownListener != null){
			countdownListener.onStart();
		}
	}
	
	public void stop(){
		showTextView.setEnabled(true);
		showTextView.setText(defaultText);
		handler.removeCallbacks(this);
		if(countdownListener != null){
			countdownListener.onStop();
		}
	}
	
	public int getRemainingSeconds() {
		return remainingSeconds;
	}

	public void setRemainingSeconds(int remainingSeconds) {
		this.remainingSeconds = remainingSeconds;
	}

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public String getCountdownText() {
		return countdownText;
	}

	public void setCountdownText(String countdownText) {
		this.countdownText = countdownText;
	}

	public TextView getShowView() {
		return showTextView;
	}

	public void setShowView(TextView showView) {
		this.showTextView = showView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public int getCurrentRemainingSeconds() {
		return currentRemainingSeconds;
	}

	public void setCurrentRemainingSeconds(int currentRemainingSeconds) {
		this.currentRemainingSeconds = currentRemainingSeconds;
	}

	public CountdownListener getCountdownListener() {
		return countdownListener;
	}

	public void setCountdownListener(CountdownListener countdownListener) {
		this.countdownListener = countdownListener;
	}

	/**
	 * 倒计时监听器
	 */
	public interface CountdownListener{
		/**
		 * 当倒计时开始
		 */
		public void onStart();
		/**
		 * 当倒计时结束
		 */
		public void onStop();
	}
}
