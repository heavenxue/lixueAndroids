package com.lixueandroid.imgloader;

import android.graphics.BitmapFactory.Options;
import android.os.Build;


/**
 *将图像解码为位图所应包含的信息
 * 
 * @author lixue
 * @since 1.8.3
 */
public class ImageDecodingInfo {

	private final String imageKey;
	private final String imageUri;
	private final ImageSize targetSize;

	private final ImageScaleType imageScaleType;
	private final ViewScaleType viewScaleType;

	private final ImageDownloader downloader;
	private final Object extraForDownloader;

	private final Options decodingOptions;

	public ImageDecodingInfo(String imageKey, String imageUri, ImageSize targetSize, ViewScaleType viewScaleType, ImageDownloader downloader, DisplayImageOptions displayOptions) {
		this.imageKey = imageKey;
		this.imageUri = imageUri;
		this.targetSize = targetSize;

		this.imageScaleType = displayOptions.getImageScaleType();
		this.viewScaleType = viewScaleType;

		this.downloader = downloader;
		this.extraForDownloader = displayOptions.getExtraForDownloader();

		decodingOptions = new Options();
		copyOptions(displayOptions.getDecodingOptions(), decodingOptions);
	}

	private void copyOptions(Options srcOptions, Options destOptions) {
		destOptions.inDensity = srcOptions.inDensity;
		destOptions.inDither = srcOptions.inDither;
		destOptions.inInputShareable = srcOptions.inInputShareable;
		destOptions.inJustDecodeBounds = srcOptions.inJustDecodeBounds;
		destOptions.inPreferredConfig = srcOptions.inPreferredConfig;
		destOptions.inPurgeable = srcOptions.inPurgeable;
		destOptions.inSampleSize = srcOptions.inSampleSize;
		destOptions.inScaled = srcOptions.inScaled;
		destOptions.inScreenDensity = srcOptions.inScreenDensity;
		destOptions.inTargetDensity = srcOptions.inTargetDensity;
		destOptions.inTempStorage = srcOptions.inTempStorage;
		if (Build.VERSION.SDK_INT >= 10) copyOptions10(srcOptions, destOptions);
		if (Build.VERSION.SDK_INT >= 11) copyOptions11(srcOptions, destOptions);
	}

	private void copyOptions10(Options srcOptions, Options destOptions) {
		destOptions.inPreferQualityOverSpeed = srcOptions.inPreferQualityOverSpeed;
	}

	private void copyOptions11(Options srcOptions, Options destOptions) {
		destOptions.inBitmap = srcOptions.inBitmap;
		destOptions.inMutable = srcOptions.inMutable;
	}

	/**
	 * @return Original {@linkplain MemoryCacheUtil#generateKey(String, ImageSize) image key} (used in memory cache).
	 */
	public String getImageKey() {
		return imageKey;
	}

	/**
	 * @return 图像解码的URI（通常从磁盘高速缓存的图像）
	 */
	public String getImageUri() {
		return imageUri;
	}

	/**
	 * @return 目标大小的图像。解码位图应该接近这种规模根据{@ linkplain ImageScaleType的图像缩放类型}和{@ linkplain ViewScaleType的视图比例类型}。
	 */
	public ImageSize getTargetSize() {
		return targetSize;
	}

	/**
	 * @return {@linkplain ImageScaleType Scale type for image sampling and scaling}. 此参数会影响结果的大小
	 */
	public ImageScaleType getImageScaleType() {
		return imageScaleType;
	}

	/**
	 * @return {@linkplain ViewScaleType View scale type}.此参数影响图像编码的效果
	 */
	public ViewScaleType getViewScaleType() {
		return viewScaleType;
	}

	/**
	 * @return Downloader图像加载器
	 */
	public ImageDownloader getDownloader() {
		return downloader;
	}

	/**
	 * @return 图像下载器的辅助对象
	 */
	public Object getExtraForDownloader() {
		return extraForDownloader;
	}

	/**
	 * @return 编码选项
	 */
	public Options getDecodingOptions() {
		return decodingOptions;
	}
}