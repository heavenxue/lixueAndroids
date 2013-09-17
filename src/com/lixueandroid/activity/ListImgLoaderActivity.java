package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.adapter.imgAdapter;
import com.lixueandroid.imgloader.DisplayImageOptions;
import com.lixueandroid.imgloader.RoundedBitmapDisplayer;
import com.lixueandroid.mydata.ImageUrls;

/**
 * listview图片加载页面
 * @author lixue
 *
 */
public class ListImgLoaderActivity extends MyBaseActivity{
	private String[] imageUrls;
	private DisplayImageOptions options;
	
	private ListView imgListView;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_listimgloader);
		imgListView=(ListView) findViewById(R.id.list_imageLoader);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		imgListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Bundle bundle=new Bundle();
				bundle.putString(ShowDetailImgActivity.PRAM_REQUESTED_STRING_URL, imageUrls[arg2]);
				startActivity(ShowDetailImgActivity.class,bundle);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		imageUrls=ImageUrls.getImageUrls();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory()
		.cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		
		imgListView.setAdapter(new imgAdapter(getBaseContext(),imageUrls,options,imageLoader));
	}

}
