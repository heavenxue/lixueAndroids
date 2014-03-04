package com.lixueandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lixue.lixueandroid.R;

public class DataAdapter extends BaseAdapter{
		Context mContext;
		List<Integer> listdata;
		public DataAdapter(Context context,List<Integer> listdata){
			mContext = context;
			this.listdata=listdata;
		}
		@Override
		public int getCount() {
			return listdata.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.grid_item, null);		
			}
			return convertView;
		}
	}