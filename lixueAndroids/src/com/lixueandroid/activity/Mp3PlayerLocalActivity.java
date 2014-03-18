package com.lixueandroid.activity;

import java.util.List;

import me.xiaopan.easyandroid.app.BaseActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.adapter.Mp3PlayerAdapter;
import com.lixueandroid.domain.Mp3Info;
import com.lixueandroid.util.Utils;

/**
 * 本地的mp3音乐文件列表
 * 
 * @author Administrator
 * 
 */
public class Mp3PlayerLocalActivity extends BaseActivity {
	private Mp3PlayerAdapter mp3PlayerAdapter;
	private ListView mp3List;
	private List<Mp3Info> mp3Infos ;

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mp3playerremote);
		mp3List = (ListView) findViewById(R.id.list_Mp3Player);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		mp3List.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle=new Bundle();
				bundle.putSerializable("mp3payer", mp3Infos.get(position));
				startActivity(Mp3PlayerActivity.class, bundle);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		mp3Infos = Utils.getLocalMp3Files("mp3/");
		mp3PlayerAdapter = new Mp3PlayerAdapter(getBaseContext(), mp3Infos);
		mp3List.setAdapter(mp3PlayerAdapter);
	}
	
}
