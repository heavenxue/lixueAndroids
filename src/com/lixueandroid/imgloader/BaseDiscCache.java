package com.lixueandroid.imgloader;

import java.io.File;


/**
 *基础类-磁盘缓存. 实现常用功能。
 * 
 * @author lixue
 * @since 1.0.0
 * @see DiscCacheAware
 * @see FileNameGenerator
 */
public abstract class BaseDiscCache implements DiscCacheAware {

	protected File cacheDir;

	private FileNameGenerator fileNameGenerator;

	public BaseDiscCache(File cacheDir) {
		this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
	}

	public BaseDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
		this.cacheDir = cacheDir;
		this.fileNameGenerator = fileNameGenerator;
	}

	@Override
	public File get(String key) {
		String fileName = fileNameGenerator.generate(key);
		return new File(cacheDir, fileName);
	}

	@Override
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
	}
}