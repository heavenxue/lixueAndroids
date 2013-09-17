package com.lixueandroid.activity;

import android.app.Application;
import android.content.Context;

import com.lixueandroid.imgloader.ImageLoader;
import com.lixueandroid.imgloader.ImageLoaderConfiguration;
import com.lixueandroid.imgloader.Md5FileNameGenerator;
import com.lixueandroid.imgloader.QueueProcessingType;


public class UILApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.enableLogging() // Not necessary in common
				.build();
		ImageLoader.getInstance().init(config);
	}
}