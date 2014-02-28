package com.lixueandroid.imgloader;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 被一些参数限制的抽象缓存类，如果缓存超过指定的限制，那么最历史最悠久的最后文件使用日期将被删除。
 * 
 * @author lixue
 * @since 1.0.0
 * @see BaseDiscCache
 * @see FileNameGenerator
 */
public abstract class LimitedDiscCache extends BaseDiscCache {

	private final AtomicInteger cacheSize;

	private final int sizeLimit;

	private final Map<File, Long> lastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());

	/**
	 * @param cacheDir Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's
	 *            needed for right cache limit work.
	 * @param sizeLimit Cache limit value. If cache exceeds this limit then file with the most oldest last usage date
	 *            will be deleted.
	 */
	public LimitedDiscCache(File cacheDir, int sizeLimit) {
		this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator(), sizeLimit);
	}

	/**
	 * @param cacheDir Directory for file caching. <b>Important:</b> Specify separate folder for cached files. It's
	 *            needed for right cache limit work.
	 * @param fileNameGenerator Name generator for cached files
	 * @param sizeLimit Cache limit value. If cache exceeds this limit then file with the most oldest last usage date
	 *            will be deleted.
	 */
	public LimitedDiscCache(File cacheDir, FileNameGenerator fileNameGenerator, int sizeLimit) {
		super(cacheDir, fileNameGenerator);
		this.sizeLimit = sizeLimit;
		cacheSize = new AtomicInteger();
		calculateCacheSizeAndFillUsageMap();
	}

	private void calculateCacheSizeAndFillUsageMap() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int size = 0;
				File[] cachedFiles = cacheDir.listFiles();
				if (cachedFiles != null) { // rarely but it can happen, don't know why
					for (File cachedFile : cachedFiles) {
						size += getSize(cachedFile);
						lastUsageDates.put(cachedFile, cachedFile.lastModified());
					}
					cacheSize.set(size);
				}
			}
		}).start();
	}

	@Override
	public void put(String key, File file) {
		int valueSize = getSize(file);
		int curCacheSize = cacheSize.get();
		while (curCacheSize + valueSize > sizeLimit) {
			int freedSize = removeNext();
			if (freedSize == 0) break; // cache is empty (have nothing to delete)
			curCacheSize = cacheSize.addAndGet(-freedSize);
		}
		cacheSize.addAndGet(valueSize);

		Long currentTime = System.currentTimeMillis();
		file.setLastModified(currentTime);
		lastUsageDates.put(file, currentTime);
	}

	@Override
	public File get(String key) {
		File file = super.get(key);

		Long currentTime = System.currentTimeMillis();
		file.setLastModified(currentTime);
		lastUsageDates.put(file, currentTime);

		return file;
	}

	@Override
	public void clear() {
		lastUsageDates.clear();
		cacheSize.set(0);
		super.clear();
	}

	/** Remove next file and returns it's size */
	private int removeNext() {
		if (lastUsageDates.isEmpty()) {
			return 0;
		}

		Long oldestUsage = null;
		File mostLongUsedFile = null;
		Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
		synchronized (lastUsageDates) {
			for (Entry<File, Long> entry : entries) {
				if (mostLongUsedFile == null) {
					mostLongUsedFile = entry.getKey();
					oldestUsage = entry.getValue();
				} else {
					Long lastValueUsage = entry.getValue();
					if (lastValueUsage < oldestUsage) {
						oldestUsage = lastValueUsage;
						mostLongUsedFile = entry.getKey();
					}
				}
			}
		}

		int fileSize = getSize(mostLongUsedFile);
		if (mostLongUsedFile.delete()) {
			lastUsageDates.remove(mostLongUsedFile);
		}
		return fileSize;
	}

	protected abstract int getSize(File file);
}
