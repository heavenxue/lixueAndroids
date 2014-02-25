package com.lixueandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.view.PullToRefreshView;
import com.lixueandroid.view.PullToRefreshView.OnFooterRefreshListener;
import com.lixueandroid.view.PullToRefreshView.OnHeaderRefreshListener;


public class TestExpandableListViewActivity extends Activity implements OnHeaderRefreshListener, OnFooterRefreshListener {
	PullToRefreshView mPullToRefreshView;
	TextView textview;
	String text = "https://github.com/heavenxue/lixueAndroids/tree/766893edcb0fff26834a818bf3de060ea46efdc8#lixueandroids";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_scrollview);
		
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		textview = (TextView)findViewById(R.id.textview);
		textview.setText(text);
		
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				textview.setText(text+";"+text);
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 设置更新时间
				//mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				textview.setText(text+";"+text);
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);

	}
}
