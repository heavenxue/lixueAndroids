package com.lixueandroid.imgloader;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 一个用来扩展的很方便的类，你只想要监听所有图像加载事件中的一个子集
 * 实现方法在{@ ImageLoadingListener中}，但什么都不做。
 * 
 * @author lixue
 * @since 1.4.0
 */
public class SimpleImageLoadingListener implements ImageLoadingListener {
	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// Empty implementation
	}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
		// Empty implementation
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// Empty implementation
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// Empty implementation
	}
}