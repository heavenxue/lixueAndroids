package com.lixueandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.domain.Mp3Info;

public class Mp3PlayerAdapter extends BaseAdapter{
	private List<Mp3Info> Mp3Player;
	private Context context;
	
	public 	Mp3PlayerAdapter(Context context,List<Mp3Info> mp3Player){
		this.Mp3Player=mp3Player;
		this.context=context;
	}

	@Override
	public Object getItem(int position) {
		return Mp3Player.get(position);
	}

	@Override
	public int getCount() {
		return Mp3Player.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.list_item_mp3player, null);
			holder.textview_filename=(TextView) convertView.findViewById(R.id.text_mp3player_filename);
			holder.textview_size=(TextView) convertView.findViewById(R.id.text_mp3player_size);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Mp3Info mp3info= Mp3Player.get(position);
		holder.textview_filename.setText(mp3info.getMp3Name());
		holder.textview_size.setText(mp3info.getMp3Size());
		return convertView;
	}
}

class ViewHolder {
	TextView textview_filename;
	TextView textview_size;
}

