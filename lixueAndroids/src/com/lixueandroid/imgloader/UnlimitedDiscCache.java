package com.lixueandroid.imgloader;

import java.io.File;


/**
 * Default implementation of {@linkplain DiscCacheAware disc cache}. Cache size is unlimited.
 * 
 * @author lixue
 * @since 1.0.0
 * @see BaseDiscCache
 */
public class UnlimitedDiscCache extends BaseDiscCache {

	/** @param cacheDir Directory for file caching */
	public UnlimitedDiscCache(File cacheDir) {
		this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
	}

	/**
	 * @param cacheDir Directory for file caching
	 * @param fileNameGenerator Name generator for cached files
	 */
	public UnlimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
		super(cacheDir, fileNameGenerator);
	}

	@Override
	public void put(String key, File file) {
		// Do nothing
	}
}
