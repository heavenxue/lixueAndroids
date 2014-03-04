package com.lixueandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixue.lixueandroid.R;

public class mytabAdapter extends BaseAdapter{
	private Context context;
	private List<String> mytext;
	public mytabAdapter(Context context,List<String> mytext){
		this.context=context;
		this.mytext=mytext;
	}
	@Override
	public int getCount() {
		return mytext.size();
	}

	@Override
	public Object getItem(int position) {
		return mytext.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.list_item_mytab, null);
			holder.mytab_textview=(TextView) convertView.findViewById(R.id.text_mytab);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
//		System.out.println(mytext.get(position));
		holder.mytab_textview.setText(mytext.get(position));
		return convertView;
	}
	
	class ViewHolder{
		TextView mytab_textview;
	}
}
