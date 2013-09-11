package com.lixueandroid.imgloader;

import android.graphics.Bitmap;
import android.widget.ImageView;


/**
 * 仅仅在 {@link ImageView}上显示图像
 * 
 * @author lixue
 * @since 1.5.6
 */
public final class SimpleBitmapDisplayer implements BitmapDisplayer {
	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView) {
		imageView.setImageBitmap(bitmap);
		return bitmap;
	}
}