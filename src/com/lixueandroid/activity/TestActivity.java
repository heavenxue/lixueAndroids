package com.lixueandroid.activity;

import me.xiaopan.easyandroid.app.BaseListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.lixue.lixueandroid.R;

public class TestActivity extends BaseListActivity{
	private View headerview;
	private ImageView img;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_test);
		headerview=LayoutInflater.from(this).inflate(R.layout.table_title, null);
		img=(ImageView) headerview.findViewById(R.id.img_header);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
//		headerview.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				toastL("我的头部被点击了");
//			}
//		});
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toastS("我的头部被点击了");
			}
		});
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				toastS("点击了第"+arg2+"个");
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
//		getListView().addHeaderView(headerview);
//		List<Integer> listdata=new ArrayList<Integer>();
//		listdata.add(0);
//		listdata.add(1);
//		listdata.add(2);
//		listdata.add(3);
//		listdata.add(4);
//		listdata.add(5);
//		listdata.add(6);
//		listdata.add(7);
//		listdata.add(8);
//		listdata.add(9);
//		listdata.add(10);
//		listdata.add(11);
//		listdata.add(12);
//		listdata.add(13);
//		listdata.add(14);
//		getListView().setAdapter(new DataAdapter(getBaseContext(),listdata));
//		getListView().addHeaderView(LayoutInflater.from(this).inflate(R.layout.table_title, null));
		getListView().addHeaderView(headerview);
		getListView().setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, new String[] {"a", "b", "c", "d", "e", "f", "g" }));
	}

}
