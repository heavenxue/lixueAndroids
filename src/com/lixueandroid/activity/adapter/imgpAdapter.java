package com.lixueandroid.activity.adapter;


import me.xiaopan.easynetwork.android.image.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixue.lixueandroid.R;

public class imgpAdapter extends BaseAdapter {
	private String[] imgurls;
	private Context context;
	
	public imgpAdapter(Context context,String[] imgurls){
		this.context=context;
		this.imgurls=imgurls;
	}
	private class ViewHolder {
		public TextView text;
		public ImageView image;
	}
	@Override
	public int getCount() {
		return imgurls.length;
	}

	@Override
	public Object getItem(int position) {
		return imgurls[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view=LayoutInflater.from(context).inflate(R.layout.item_list_image, null);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.text);
			holder.image = (ImageView) view.findViewById(R.id.image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.text.setText("Item " + (position + 1));
		ImageLoader.getInstance().load(imgurls[position], holder.image);
		return view;
	}
}

