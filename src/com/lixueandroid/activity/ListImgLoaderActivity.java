package com.lixueandroid.activity;

import android.os.Bundle;
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
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		UILApplication.initImageLoader(getBaseContext());
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
