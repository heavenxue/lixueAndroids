package com.lixueandroid.imgloader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;


/**
 * 处理显示图像任务.
 * 
 * @author lixue
 * @since 1.8.0
 */
class ProcessAndDisplayImageTask implements Runnable {

	private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";

	private final ImageLoaderEngine engine;
	private final Bitmap bitmap;
	private final ImageLoadingInfo imageLoadingInfo;
	private final Handler handler;

	public ProcessAndDisplayImageTask(ImageLoaderEngine engine, Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, Handler handler) {
		this.engine = engine;
		this.bitmap = bitmap;
		this.imageLoadingInfo = imageLoadingInfo;
		this.handler = handler;
	}

	@Override
	public void run() {
		if (engine.configuration.loggingEnabled) Log.i(LOG_POSTPROCESS_IMAGE, imageLoadingInfo.memoryCacheKey);
		BitmapProcessor processor = imageLoadingInfo.options.getPostProcessor();
		final Bitmap processedBitmap = processor.process(bitmap);
		if (processedBitmap != bitmap) {
			bitmap.recycle();
		}
		handler.post(new DisplayBitmapTask(processedBitmap, imageLoadingInfo, engine));
	}
}