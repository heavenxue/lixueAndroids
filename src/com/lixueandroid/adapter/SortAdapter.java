package com.lixueandroid.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.domain.SortModel;

public class SortAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> modes;
	private Context context;
	
	public SortAdapter(Context context,List<SortModel> modelsList){
		this.modes=modelsList;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return modes.size();
	}

	@Override
	public Object getItem(int position) {
		return modes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if(convertView==null){
			holder=new viewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_list_sort, null);
			holder.text_name=(TextView) convertView.findViewById(R.id.text_item_list_sort_name);
			holder.text_letter=(TextView) convertView.findViewById(R.id.text_item_list_sort_letter);
			convertView.setTag(holder);
		}else{
			holder=(viewHolder) convertView.getTag();
		}
		//根据position获取分类的首字母的char ascii值  
        int section = getSectionForPosition(position);  
          
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现  
        if(position == getPositionForSection(section)){  
        	holder.text_letter.setVisibility(View.VISIBLE);  
        	holder.text_letter.setText(modes.get(position).getSortLetters());  
        }else{  
        	holder.text_letter.setVisibility(View.GONE);  
        }  
		holder.text_name.setText(modes.get(position).getName());
		return convertView;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int getPositionForSection(int sectionIndex) {
		  for (int i = 0; i < getCount(); i++) {  
	            String sortStr = modes.get(i).getSortLetters();  
	            char firstChar = sortStr.toUpperCase().charAt(0);  
	            if (firstChar == sectionIndex) {  
	                return i;  
	            }  
	        }  
	          
	        return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		 return modes.get(position).getSortLetters().charAt(0);  
	}
	class viewHolder{
		TextView text_name;
		TextView text_letter;
	}

}
