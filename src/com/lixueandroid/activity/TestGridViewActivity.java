package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.adapter.DataAdapter;
import com.lixueandroid.activity.view.PullToRefreshView;
import com.lixueandroid.activity.view.PullToRefreshView.OnFooterRefreshListener;
import com.lixueandroid.activity.view.PullToRefreshView.OnHeaderRefreshListener;

public class TestGridViewActivity extends Activity implements OnHeaderRefreshListener, OnFooterRefreshListener {
	PullToRefreshView mPullToRefreshView;
	GridView mGridView;
	private List<Integer> listDrawable = new ArrayList<Integer>();
	private DataAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_gridview);
		
		listDrawable.add(R.drawable.animation1);
		listDrawable.add(R.drawable.animation2);
		
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mGridView = (GridView) findViewById(R.id.gridview);
		adapter = new DataAdapter(getBaseContext(),listDrawable);
		mGridView.setAdapter(adapter);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				System.out.println("上拉加载");
				listDrawable.add(R.drawable.animation3);
				adapter.notifyDataSetChanged();
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
				// mPullToRefreshView.onHeaderRefreshComplete("最近更新:01-23 12:01");
				System.out.println("下拉更新");
				listDrawable.add(R.drawable.animation4);
				adapter.notifyDataSetChanged();
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);

	}
}
