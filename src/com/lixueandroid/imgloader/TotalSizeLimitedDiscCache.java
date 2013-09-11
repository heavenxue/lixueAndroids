package com.lixueandroid.imgloader;

import java.io.File;

import android.util.Log;


/**
 * 被缓存总大小限制的磁盘缓存类。如果高速缓存大小超过指定的限制，那么最历史最悠久的最后文件使用日期将被删除。
 * 
 * @author lixue
 * @since 1.0.0
 * @see LimitedDiscCache
 */
public class TotalSizeLimitedDiscCache extends LimitedDiscCache {

	private static final int MIN_NORMAL_CACHE_SIZE_IN_MB = 2;
	private static final int MIN_NORMAL_CACHE_SIZE = MIN_NORMAL_CACHE_SIZE_IN_MB * 1024 * 1024;

	/**
	 * @param cacheDir Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's
	 *            needed for right cache limit work.
	 * @param maxCacheSize Maximum cache directory size (in bytes). If cache size exceeds this limit then file with the
	 *            most oldest last usage date will be deleted.
	 */
	public TotalSizeLimitedDiscCache(File cacheDir, int maxCacheSize) {
		this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator(), maxCacheSize);
	}

	/**
	 * @param cacheDir Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's
	 *            needed for right cache limit work.
	 * @param fileNameGenerator Name generator for cached files
	 * @param maxCacheSize Maximum cache directory size (in bytes). If cache size exceeds this limit then file with the
	 *            most oldest last usage date will be deleted.
	 */
	public TotalSizeLimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator, int maxCacheSize) {
		super(cacheDir, fileNameGenerator, maxCacheSize);
		if (maxCacheSize < MIN_NORMAL_CACHE_SIZE) {
			Log.w("You set too small disc cache size (less than %1$d Mb)", MIN_NORMAL_CACHE_SIZE_IN_MB+"");
		}
	}

	@Override
	protected int getSize(File file) {
		return (int) file.length();
	}
}