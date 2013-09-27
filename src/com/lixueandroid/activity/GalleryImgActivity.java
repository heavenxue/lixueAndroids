package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.adapter.galleryImgAdapter;
import com.lixueandroid.imgloader.DisplayImageOptions;
import com.lixueandroid.imgloader.RoundedBitmapDisplayer;
import com.lixueandroid.mydata.ImageUrls;

/**
 * gallery加载图片页面
 * @author lixue
 * 
 */
public class GalleryImgActivity extends MyBaseActivity {
	private Gallery galleryImgLoader;
	private String[] imageUrls;
	private DisplayImageOptions options;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_galleryimg);
		galleryImgLoader=(Gallery) findViewById(R.id.gallery_img);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		galleryImgLoader.setOnItemClickListener(new OnItemClickListener() {

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
		galleryImgLoader.setAdapter(new galleryImgAdapter(getBaseContext(), imageUrls, options, getMyApplication().getImageLoader()));
		
	}
}
