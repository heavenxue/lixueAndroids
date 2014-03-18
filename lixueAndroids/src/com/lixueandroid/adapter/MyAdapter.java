package com.lixueandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixue.lixueandroid.R;

public class MyAdapter extends BaseAdapter{
	
	private List<String> list;
	private Context context;
	
	public MyAdapter(Context context , List<String> list){
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.listitem, null);
		TextView textView = (TextView)convertView.findViewById(R.id.text);
		ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
		textView.setText(list.get(position));
		return convertView;
	}
	

}
