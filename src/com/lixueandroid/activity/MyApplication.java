package com.lixueandroid.activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.lixue.lixueandroid.R;
import com.lixueandroid.imgloader.DisplayImageOptions;
import com.lixueandroid.imgloader.FadeInBitmapDisplayer;
import com.lixueandroid.imgloader.ImageLoader;
import com.lixueandroid.imgloader.ImageLoaderConfiguration;
import com.lixueandroid.imgloader.ImageLoadingListener;
import com.lixueandroid.imgloader.Md5FileNameGenerator;
import com.lixueandroid.imgloader.QueueProcessingType;
import com.lixueandroid.imgloader.RoundedBitmapDisplayer;
import com.lixueandroid.imgloader.SimpleImageLoadingListener;


/**
 * 全局变量-首选项
 * @author lixue
 *
 */
public class MyApplication extends Application {
	//初始化图像加载器的一些参数设置
	protected static ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageLoadingListener animateImageLoadingListener;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		imageLoader=ImageLoader.getInstance();
		
		setAnimateImageLoadingListener(new AnimateFirstDisplayListener());
		
		initImageLoader(getApplicationContext());
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory()
		.cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		
		/* 极光推动初始化模块 */
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.enableLogging() // Not necessary in common
				.build();
		imageLoader.init(config);
	}
	
	/**
	 * 得到图像加载器
	 * @return ImageLoader
	 */
	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	/**
	 * 设置图像加载器
	 * @param void
	 */
	public static void setImageLoader(ImageLoader imageLoader) {
		MyApplication.imageLoader = imageLoader;
	}

	/**
	 * 得到图像加载器的选项设置
	 * @return DisplayImageOptions
	 */
	public DisplayImageOptions getOptions() {
		return options;
	}

	/**
	 * 设置图像加载器的选项设置
	 * @param options
	 */
	public void setOptions(DisplayImageOptions options) {
		this.options = options;
	}

	/**
	 * 得到图像开始加载时的事件
	 * @return ImageLoadingListener
	 */
	public ImageLoadingListener getAnimateImageLoadingListener() {
		return animateImageLoadingListener;
	}

	/**设置图像开始加载时的事件
	 * @param animateImageLoadingListener
	 */
	public void setAnimateImageLoadingListener(ImageLoadingListener animateImageLoadingListener) {
		this.animateImageLoadingListener = animateImageLoadingListener;
	}

	/**
	 * 图像加载时所显示的一点点渐隐渐现的动画
	 * @author lixue
	 *
	 */
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