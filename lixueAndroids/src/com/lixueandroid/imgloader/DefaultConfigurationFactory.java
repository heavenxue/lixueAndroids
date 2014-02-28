package com.lixueandroid.imgloader;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;


/**
 * 提供默认图像选项的工厂
 * 
 * @author lixue
 * @since 1.5.6
 */
public class DefaultConfigurationFactory {

	/** 创建默认执行任务执行者 */
	public static Executor createExecutor(int threadPoolSize, int threadPriority, QueueProcessingType tasksProcessingType) {
		boolean lifo = tasksProcessingType == QueueProcessingType.LIFO;
		BlockingQueue<Runnable> taskQueue = lifo ? new LIFOLinkedBlockingDeque<Runnable>() : new LinkedBlockingQueue<Runnable>();
		return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, taskQueue, createThreadFactory(threadPriority));
	}

	/** 初始化文件名生成器 */
	public static FileNameGenerator createFileNameGenerator() {
		return new HashCodeFileNameGenerator();
	}

	/** 创建默认的实现，{@链接DisckCacheAware}取决于传入的参数 */
	public static DiscCacheAware createDiscCache(Context context, FileNameGenerator discCacheFileNameGenerator, int discCacheSize, int discCacheFileCount) {
		if (discCacheSize > 0) {
			File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
			return new TotalSizeLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheSize);
		} else if (discCacheFileCount > 0) {
			File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
			return new FileCountLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheFileCount);
		} else {
			File cacheDir = StorageUtils.getCacheDirectory(context);
			return new UnlimitedDiscCache(cacheDir, discCacheFileNameGenerator);
		}
	}

	/**如果主盘缓存变得不可用，将用于建立储备光盘缓存 */
	public static DiscCacheAware createReserveDiscCache(Context context) {
		File cacheDir = context.getCacheDir();
		File individualDir = new File(cacheDir, "uil-images");
		if (individualDir.exists() || individualDir.mkdir()) {
			cacheDir = individualDir;
		}
		return new TotalSizeLimitedDiscCache(cacheDir, 2 * 1024 * 1024); // limit - 2 Mb
	}

	/**
	 *创建默认的实现，{@链接MemoryCacheAware}取决于传入的参数: <br />
	 * {@link LruMemoryCache} (for API >= 9) or {@link LRULimitedMemoryCache} (for API < 9).<br />
	 * Default cache size = 1/8 of available app memory.
	 */
	public static MemoryCacheAware<String, Bitmap> createMemoryCache(int memoryCacheSize) {
		if (memoryCacheSize == 0) {
			memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
		}
		MemoryCacheAware<String, Bitmap> memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}
		return memoryCache;
	}

	/** 创建默认的实现- {@link BaseImageDownloader} */
	public static ImageDownloader createImageDownloader(Context context) {
		return new BaseImageDownloader(context);
	}

	/**创建默认的实现 - {@link BaseImageDecoder} */
	public static ImageDecoder createImageDecoder(boolean loggingEnabled) {
		return new BaseImageDecoder(loggingEnabled);
	}

	/** 创建默认的实现 - {@link SimpleBitmapDisplayer} */
	public static BitmapDisplayer createBitmapDisplayer() {
		return new SimpleBitmapDisplayer();
	}

	/**创建默认的实现 {@linkplain ThreadFactory thread factory} 对于任务的执行 */
	private static ThreadFactory createThreadFactory(int threadPriority) {
		return new DefaultThreadFactory(threadPriority);
	}

	private static class DefaultThreadFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);

		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;
		private final int threadPriority;

		DefaultThreadFactory(int threadPriority) {
			this.threadPriority = threadPriority;
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon()) t.setDaemon(false);
			t.setPriority(threadPriority);
			return t;
		}
	}
}
