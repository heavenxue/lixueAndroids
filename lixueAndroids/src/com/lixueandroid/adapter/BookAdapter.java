package com.lixueandroid.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lixue.lixueandroid.R;

/**
 * 翻页适配器
 * 
 * @author Administrator
 *
 */
public class BookAdapter extends BaseAdapter {
	private List<String> strList = new ArrayList<String>();
	private Context mContext;
	
	public BookAdapter(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(mContext);
		textView.setText(strList.get(position));
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(32);
		textView.setBackgroundColor(Color.WHITE);
		textView.setBackgroundResource(R.drawable.ly);
		textView.setPadding(10, 10, 10, 10);
		textView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		return textView;
	}

	@Override
	public int getCount() {
		return strList.size();
	}

	@Override
	public Object getItem(int position) {
		return strList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addItem(List<String> list){
		strList.addAll(list);
	}
}
