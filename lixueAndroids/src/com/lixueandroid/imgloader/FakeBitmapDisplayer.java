package com.lixueandroid.imgloader;

import android.graphics.Bitmap;
import android.widget.ImageView;


/**
 * Fake displayer which doesn't display Bitmap in ImageView. Should be used in {@linkplain DisplayImageOptions display
 * options} for
 * {@link ImageLoader#loadImage(String, int, int, com.nostra13.universalimageloader.core.DisplayImageOptions, com.nostra13.universalimageloader.core.assist.ImageLoadingListener)
 * ImageLoader.loadImage()}
 * 
 * @author lixue
 * @since 1.6.0
 */
public final class FakeBitmapDisplayer implements BitmapDisplayer {
	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView) {
		// Do nothing
		return bitmap;
	}
}
