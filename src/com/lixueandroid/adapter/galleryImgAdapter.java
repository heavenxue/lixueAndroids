package com.lixueandroid.adapter;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.imgloader.DisplayImageOptions;
import com.lixueandroid.imgloader.FadeInBitmapDisplayer;
import com.lixueandroid.imgloader.ImageLoader;
import com.lixueandroid.imgloader.ImageLoadingListener;
import com.lixueandroid.imgloader.SimpleImageLoadingListener;

public class galleryImgAdapter extends BaseAdapter {
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private String[] imgurls;
	private Context context;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	
	public galleryImgAdapter(Context context,String[] imgurls,DisplayImageOptions optionss,ImageLoader imageLoader){
		this.context=context;
		this.imgurls=imgurls;
		this.options=optionss;
		this.imageLoader=imageLoader;
	}
	private class ViewHolder {
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
			view=LayoutInflater.from(context).inflate(R.layout.gallery_item_list_image, null);
			holder = new ViewHolder();
			holder.image = (ImageView) view.findViewById(R.id.image_gallery);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		imageLoader.displayImage(imgurls[position], holder.image, options, animateFirstListener);

		return view;
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}

