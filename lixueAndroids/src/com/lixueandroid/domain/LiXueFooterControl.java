package com.lixueandroid.domain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LiXueFooterControl  extends LinearLayout {
	private TextView textView;
	private ProgressBar progressBar;
	private onloadListener onListener;
	public LiXueFooterControl(Context context) {
		super(context);
		//初始化控件
		LoadControl();
	}
	public LiXueFooterControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		//初始化控件
		LoadControl();
	}
	//加载控件
	public void LoadControl(){
		textView =new TextView(getContext());
		textView.setText("加载更多");
		textView.setVisibility(VISIBLE);
//		textView.setVisibility(GONE);
		progressBar=new ProgressBar(getContext());
//		progressBar.setVisibility(GONE);
		progressBar.setVisibility(View.VISIBLE);
		addView(progressBar);
		addView(textView);
		setGravity(Gravity.CENTER);
		System.out.println("加载更多出现过");
	}
	//开始加载
	public void Startload(){
		textView.setVisibility(VISIBLE);
		textView.setText("开始加载");
		progressBar.setVisibility(VISIBLE);
		if(onListener!=null){
			onListener.onStartload(this);
		}
	}
	//设置加载更多的字样
	public void SetLoadMoreText(String text) {
		textView.setVisibility(VISIBLE);
		textView.setText(text);
		setGravity(Gravity.CENTER);
	}
	public onloadListener getOnListener() {
		return onListener;
	}
	public void setOnListener(onloadListener onListener) {
		this.onListener = onListener;
	}
	//加載完成
	public void Endload(){
		textView.setVisibility(GONE);
		progressBar.setVisibility(GONE);
	}
	
	public interface onloadListener{
		public void onStartload(LiXueFooterControl liXueFooterControl);
	}
}