package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.adapter.SortAdapter;
import com.lixueandroid.domain.SortModel;
import com.lixueandroid.util.CharActerParser;
import com.lixueandroid.view.PinYinComparator;
import com.lixueandroid.view.SideBar;
import com.lixueandroid.view.SideBar.onTouchingLetterChangedListener;

public class LettersOrderListViewActivity extends MyBaseActivity {
	private ListView letterOrderListView;
	private List<SortModel> sortMoelList;
	private SideBar sideBar;
	private SortAdapter sortAdapter;
	private PinYinComparator pinyinComparator;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_lettersorderlist);
		letterOrderListView=(ListView) findViewById(R.id.list_letters_order);
		sideBar=(SideBar) findViewById(R.id.sidebar);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		letterOrderListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				/**点击每项时，会弹出相应数据的首字母拼音**/
				toastL(sortMoelList.get(position).getSortLetters());
			}
			
		});
		sideBar.setOnChangedListener(new onTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String letter) {
				//该字母首次出现的位置
				int pos=	sortAdapter.getPositionForSection(letter.charAt(0));
				letterOrderListView.setSelection(pos);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		sortMoelList=new ArrayList<SortModel>();
		pinyinComparator=new PinYinComparator();
		String[] data=getResources().getStringArray(R.array.sortData);
		
		for(int i=0;i<data.length;i++){
			SortModel sm=new SortModel();
			sm.setName(data[i]);
			String pinyin=CharActerParser.getInstance().getSelling(data[i]);
			String sortStr=pinyin.substring(0, 1).toUpperCase();
			if(sortStr.matches("[A-Z]")){
				sm.setSortLetters(sortStr);
			}else{
				sm.setSortLetters("#");
			}
			sortMoelList.add(sm);
		}
		 // 根据a-z进行排序源数据  
        Collections.sort(sortMoelList, pinyinComparator);  
		if(sortAdapter==null){
			sortAdapter=new SortAdapter(getBaseContext(), sortMoelList);
		}
		letterOrderListView.setAdapter(sortAdapter);
	}
}
