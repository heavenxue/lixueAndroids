package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lixue.lixueandroid.R;
import com.lixueandroid.adapter.DataAdapter;
import com.lixueandroid.view.PullToRefreshView;
import com.lixueandroid.view.PullToRefreshView.OnFooterRefreshListener;
import com.lixueandroid.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 实现OnHeaderRefreshListener,OnFooterRefreshListener接口
 * @author Administrator
 *
 */
public class TestListViewActivity extends ListActivity implements OnHeaderRefreshListener,OnFooterRefreshListener{
	PullToRefreshView mPullToRefreshView;
	private List<Integer> listDrawable = new ArrayList<Integer>();
	private DataAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_listview);
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		listDrawable.add(R.drawable.animation1);
		listDrawable.add(R.drawable.animation2);
		adapter = new DataAdapter(getBaseContext(),listDrawable);
		getListView().setAdapter(adapter);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(this, "positon = "+position, 1000).show();
	}
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				listDrawable.add(R.drawable.animation2);
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
				//设置更新时间
				listDrawable.add(R.drawable.animation2);
				adapter.notifyDataSetChanged();
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		},1000);
	}
}
