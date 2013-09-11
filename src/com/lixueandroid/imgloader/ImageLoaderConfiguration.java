package com.lixueandroid.imgloader;

import java.util.concurrent.Executor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;


/**
 *  ImageLoader配置器
 * 
 * @author lixue
 * @since 1.0.0
 * @see ImageLoader
 * @see MemoryCacheAware
 * @see DiscCacheAware
 * @see DisplayImageOptions
 * @see ImageDownloader
 * @see FileNameGenerator
 */
public final class ImageLoaderConfiguration {

	final Context context;

	final int maxImageWidthForMemoryCache;
	final int maxImageHeightForMemoryCache;
	final int maxImageWidthForDiscCache;
	final int maxImageHeightForDiscCache;
	final CompressFormat imageCompressFormatForDiscCache;
	final int imageQualityForDiscCache;

	final Executor taskExecutor;
	final Executor taskExecutorForCachedImages;
	final boolean customExecutor;
	final boolean customExecutorForCachedImages;

	final int threadPoolSize;
	final int threadPriority;
	final QueueProcessingType tasksProcessingType;

	final MemoryCacheAware<String, Bitmap> memoryCache;
	final DiscCacheAware discCache;
	final ImageDownloader downloader;
	final ImageDecoder decoder;
	final DisplayImageOptions defaultDisplayImageOptions;
	final boolean loggingEnabled;

	final DiscCacheAware reserveDiscCache;
	final ImageDownloader networkDeniedDownloader;
	final ImageDownloader slowNetworkDownloader;

	private ImageLoaderConfiguration(final Builder builder) {
		context = builder.context;
		maxImageWidthForMemoryCache = builder.maxImageWidthForMemoryCache;
		maxImageHeightForMemoryCache = builder.maxImageHeightForMemoryCache;
		maxImageWidthForDiscCache = builder.maxImageWidthForDiscCache;
		maxImageHeightForDiscCache = builder.maxImageHeightForDiscCache;
		imageCompressFormatForDiscCache = builder.imageCompressFormatForDiscCache;
		imageQualityForDiscCache = builder.imageQualityForDiscCache;
		taskExecutor = builder.taskExecutor;
		taskExecutorForCachedImages = builder.taskExecutorForCachedImages;
		threadPoolSize = builder.threadPoolSize;
		threadPriority = builder.threadPriority;
		tasksProcessingType = builder.tasksProcessingType;
		discCache = builder.discCache;
		memoryCache = builder.memoryCache;
		defaultDisplayImageOptions = builder.defaultDisplayImageOptions;
		loggingEnabled = builder.loggingEnabled;
		downloader = builder.downloader;
		decoder = builder.decoder;

		customExecutor = builder.customExecutor;
		customExecutorForCachedImages = builder.customExecutorForCachedImages;

		networkDeniedDownloader = new NetworkDeniedImageDownloader(downloader);
		slowNetworkDownloader = new SlowNetworkImageDownloader(downloader);

		reserveDiscCache = DefaultConfigurationFactory.createReserveDiscCache(context);
	}

	/**
	 * 创建默认配置 <br />
	 * <b>默认值:</b>
	 * <ul>
	 * <li>maxImageWidthForMemoryCache =手机屏幕宽</li>
	 * <li>maxImageHeightForMemoryCache = 手机屏幕高</li>
	 * <li>maxImageWidthForDiscCache = 不限</li>
	 * <li>maxImageHeightForDiscCache = 不限</li>
	 * <li>threadPoolSize = {@link Builder#DEFAULT_THREAD_POOL_SIZE this}</li>
	 * <li>threadPriority = {@link Builder#DEFAULT_THREAD_PRIORITY this}</li>
	 * <li>允许高速缓存在存储器中的不同尺寸的图像</li>
	 * <li>memoryCache = {@link DefaultConfigurationFactory#createMemoryCache(int)}</li>
	 * <li>discCache = {@link UnlimitedDiscCache}</li>
	 * <li>imageDownloader = {@link DefaultConfigurationFactory#createImageDownloader(Context)}</li>
	 * <li>imageDecoder = {@link DefaultConfigurationFactory#createImageDecoder(boolean)}</li>
	 * <li>discCacheFileNameGenerator = {@link DefaultConfigurationFactory#createFileNameGenerator()}</li>
	 * <li>defaultDisplayImageOptions = {@link DisplayImageOptions#createSimple() Simple options}</li>
	 * <li>tasksProcessingOrder = {@link QueueProcessingType#FIFO}</li>
	 * <li>详细的日志记录被禁用</li>
	 * </ul>
	 * */
	public static ImageLoaderConfiguration createDefault(Context context) {
		return new Builder(context).build();
	}

	/**
	 * Builder for {@link ImageLoaderConfiguration}
	 * 
	 * @author lixue
	 */
	public static class Builder {

		private static final String WARNING_OVERLAP_DISC_CACHE_PARAMS = "discCache(), discCacheSize() and discCacheFileCount calls overlap each other";
		private static final String WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR = "discCache() and discCacheFileNameGenerator() calls overlap each other";
		private static final String WARNING_OVERLAP_MEMORY_CACHE = "memoryCache() and memoryCacheSize() calls overlap each other";
		private static final String WARNING_OVERLAP_EXECUTOR = "threadPoolSize(), threadPriority() and tasksProcessingOrder() calls "+ "can overlap taskExecutor() and taskExecutorForCachedImages() calls.";

		/** {@value} */
		public static final int DEFAULT_THREAD_POOL_SIZE = 3;
		/** {@value} */
		public static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY - 1;
		/** {@value} */
		public static final QueueProcessingType DEFAULT_TASK_PROCESSING_TYPE = QueueProcessingType.FIFO;

		private Context context;

		private int maxImageWidthForMemoryCache = 0;
		private int maxImageHeightForMemoryCache = 0;
		private int maxImageWidthForDiscCache = 0;
		private int maxImageHeightForDiscCache = 0;
		private CompressFormat imageCompressFormatForDiscCache = null;
		private int imageQualityForDiscCache = 0;

		private Executor taskExecutor = null;
		private Executor taskExecutorForCachedImages = null;
		private boolean customExecutor = false;
		private boolean customExecutorForCachedImages = false;

		private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
		private int threadPriority = DEFAULT_THREAD_PRIORITY;
		private boolean denyCacheImageMultipleSizesInMemory = false;
		private QueueProcessingType tasksProcessingType = DEFAULT_TASK_PROCESSING_TYPE;

		private int memoryCacheSize = 0;
		private int discCacheSize = 0;
		private int discCacheFileCount = 0;

		private MemoryCacheAware<String, Bitmap> memoryCache = null;
		private DiscCacheAware discCache = null;
		private FileNameGenerator discCacheFileNameGenerator = null;
		private ImageDownloader downloader = null;
		private ImageDecoder decoder;
		private DisplayImageOptions defaultDisplayImageOptions = null;

		private boolean loggingEnabled = false;

		public Builder(Context context) {
			this.context = context.getApplicationContext();
		}

		/**
		 * 缓存设置选项
		 * 
		 * @param maxImageWidthForMemoryCache 为了节省内存，图像的最大宽度应该是设备屏幕的宽度 <b>默认值 -屏幕宽</b>
		 * @param maxImageHeightForMemoryCache 为了节省内存，图像的最大宽度应该是设备屏幕的高度 <b>默认值 -屏幕高</b>
		 */
		public Builder memoryCacheExtraOptions(int maxImageWidthForMemoryCache, int maxImageHeightForMemoryCache) {
			this.maxImageWidthForMemoryCache = maxImageWidthForMemoryCache;
			this.maxImageHeightForMemoryCache = maxImageHeightForMemoryCache;
			return this;
		}

		/**
		 * 在保存到光盘高速缓存之前，设置选项调整大小/压缩下载的图像。
		 * <b>注意: 只有当你有适当的需求时才使用此选项。它可以使ImageLoader的慢.</b>
		 * 
		 * @param maxImageWidthForDiscCache  最大宽度
		 * @param maxImageHeightForDiscCache 最大高度
		 * @param compressFormat {@link android.graphics.Bitmap.CompressFormat Compress format} 图像格式（JPEG，PNG,WEBP) 
		 * @param compressQuality 提示压缩程度，在0-100之间。0意压缩体积小，100意压缩的最大质量。有些格式，如PNG无损，忽视质量设置
		 */
		public Builder discCacheExtraOptions(int maxImageWidthForDiscCache, int maxImageHeightForDiscCache, CompressFormat compressFormat, int compressQuality) {
			this.maxImageWidthForDiscCache = maxImageWidthForDiscCache;
			this.maxImageHeightForDiscCache = maxImageHeightForDiscCache;
			this.imageCompressFormatForDiscCache = compressFormat;
			this.imageQualityForDiscCache = compressQuality;
			return this;
		}

		/**
		 * Sets custom {@linkplain Executor executor} for tasks of loading and displaying images.<br />
		 * <br />
		 * <b>NOTE:</b> If you set custom executor then following configuration options will not be considered for this
		 * executor:
		 * <ul>
		 * <li>{@link #threadPoolSize(int)}</li>
		 * <li>{@link #threadPriority(int)}</li>
		 * <li>{@link #tasksProcessingOrder(QueueProcessingType)}</li>
		 * </ul>
		 * 
		 * @see #taskExecutorForCachedImages(Executor)
		 */
		public Builder taskExecutor(Executor executor) {
			if (threadPoolSize != DEFAULT_THREAD_POOL_SIZE || threadPriority != DEFAULT_THREAD_PRIORITY || tasksProcessingType != DEFAULT_TASK_PROCESSING_TYPE) {
				Log.w("", WARNING_OVERLAP_EXECUTOR);
			}

			this.taskExecutor = executor;
			return this;
		}

		/**
		 * 设置自定义在sd卡上显示图像的显示任务(执行这些任务的很快，所以喜欢使用单独的执行任务)
		 *	如果你设置了相同的任务并且这些任务在相同的线程池，那么时间短的任务要等待轮到自己很长一段时间
		 * <b>注意:</b> 如果你设置自定义的执行任务，那么以下的配置选项不会被认为是这个执行中的
		 * 
		 * <ul>
		 * <li>{@link #threadPoolSize(int)}</li>
		 * <li>{@link #threadPriority(int)}</li>
		 * <li>{@link #tasksProcessingOrder(QueueProcessingType)}</li>
		 * </ul>
		 * 
		 * @see #taskExecutor(Executor)
		 */
		public Builder taskExecutorForCachedImages(Executor executorForCachedImages) {
			if (threadPoolSize != DEFAULT_THREAD_POOL_SIZE || threadPriority != DEFAULT_THREAD_PRIORITY || tasksProcessingType != DEFAULT_TASK_PROCESSING_TYPE) {
				Log.w("", WARNING_OVERLAP_EXECUTOR);
			}

			this.taskExecutorForCachedImages = executorForCachedImages;
			return this;
		}

		/**
		 * 为图像显示任务设置线程池<br />
		 * Default value - {@link #DEFAULT_THREAD_POOL_SIZE this}
		 * */
		public Builder threadPoolSize(int threadPoolSize) {
			if (taskExecutor != null || taskExecutorForCachedImages != null) {
				Log.w("", WARNING_OVERLAP_EXECUTOR);
			}

			this.threadPoolSize = threadPoolSize;
			return this;
		}

		/**
		 * 设置图像加载线程的优先级别
		 * 不能在 {@link Thread#MAX_PRIORITY} 到{@link Thread#MIN_PRIORITY}这个范围之外
		 * 默认值 - {@link #DEFAULT_THREAD_PRIORITY this}
		 * */
		public Builder threadPriority(int threadPriority) {
			if (taskExecutor != null || taskExecutorForCachedImages != null) {
				Log.w("", WARNING_OVERLAP_EXECUTOR);
			}

			if (threadPriority < Thread.MIN_PRIORITY) {
				this.threadPriority = Thread.MIN_PRIORITY;
			} else {
				if (threadPriority > Thread.MAX_PRIORITY) {
					threadPriority = Thread.MAX_PRIORITY;
				} else {
					this.threadPriority = threadPriority;
				}
			}
			return this;
		}

		/**
		 * 当你在一个小的ImageView上尝试显示此图像在一个较大的ImageView的解码图像（从相同的URI）更大的规模将是先前解码图像尺寸较小的缓存在内存中。
		 * 默认行为是允许缓存在内存多个大小不同的图像。因此，当一些图像将被缓存在内存中，然后以前高速缓存大小的图像（如果存在的话）将被删除之前从内存中缓存。
		 * */
		public Builder denyCacheImageMultipleSizesInMemory() {
			this.denyCacheImageMultipleSizesInMemory = true;
			return this;
		}

		/**
		 * 设置任务处理的序列类型
		 * 默认值 - {@link QueueProcessingType#FIFO}
		 */
		public Builder tasksProcessingOrder(QueueProcessingType tasksProcessingType) {
			if (taskExecutor != null || taskExecutorForCachedImages != null) {
				Log.w("", WARNING_OVERLAP_EXECUTOR);
			}

			this.tasksProcessingType = tasksProcessingType;
			return this;
		}

		/**
		 * 设置最大的缓存大小
		 * 默认值 - 占有效应用缓存的1/8.<br />
		 */
		public Builder memoryCacheSize(int memoryCacheSize) {
			if (memoryCacheSize <= 0) throw new IllegalArgumentException("memoryCacheSize must be a positive number");

			if (memoryCache != null) {
				Log.w("", WARNING_OVERLAP_MEMORY_CACHE);
			}

			this.memoryCacheSize = memoryCacheSize;
			return this;
		}

		/**
		 * 设置图像缓存
		 *默认值 - {@link com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache LruMemoryCache}
		 *  (size = 1/8 of available app memory)<br />
		 * <br />
		 * <b>注意:</b> 如果你自定义了缓存的大小，那么接下来的配置选项应当考虑
		 * <ul>
		 * <li>{@link #memoryCacheSize(int)}</li>
		 * </ul>
		 */
		public Builder memoryCache(MemoryCacheAware<String, Bitmap> memoryCache) {
			if (memoryCacheSize != 0) {
				Log.w("", WARNING_OVERLAP_MEMORY_CACHE);
			}

			this.memoryCache = memoryCache;
			return this;
		}

		/**
		 * 设置最大的高速缓存的大小（字节数）
		 */
		public Builder discCacheSize(int maxCacheSize) {
			if (maxCacheSize <= 0) throw new IllegalArgumentException("maxCacheSize must be a positive number");

			if (discCache != null || discCacheFileCount > 0) {
				Log.w("", WARNING_OVERLAP_DISC_CACHE_PARAMS);
			}

			this.discCacheSize = maxCacheSize;
			return this;
		}
		/**
		 * 设置缓存目录中最大文件数
		 * 默认值: 直到高速缓存容量被限制.<br />
		 */
		public Builder discCacheFileCount(int maxFileCount) {
			if (maxFileCount <= 0) throw new IllegalArgumentException("maxFileCount must be a positive number");

			if (discCache != null || discCacheSize > 0) {
				Log.w("", WARNING_OVERLAP_DISC_CACHE_PARAMS);
			}

			this.discCacheSize = 0;
			this.discCacheFileCount = maxFileCount;
			return this;
		}

		/**
		 * 设置生成.光盘缓存的缓存文件名<br />
		 */
		public Builder discCacheFileNameGenerator(FileNameGenerator fileNameGenerator) {
			if (discCache != null) {
				Log.w("", WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR);
			}

			this.discCacheFileNameGenerator = fileNameGenerator;
			return this;
		}

		/**
		 * 集实用，将负责图像下载.<br />
		 * */
		public Builder imageDownloader(ImageDownloader imageDownloader) {
			this.downloader = imageDownloader;
			return this;
		}

		/**
		 * 集实用，这将是负责解码的图像流.<br />
		 * */
		public Builder imageDecoder(ImageDecoder imageDecoder) {
			this.decoder = imageDecoder;
			return this;
		}

		/**
		 * 设置图像高速缓存
		 * <b>注意:</b> 如果你自定义了，那么下面的配置不用考虑
		 * <ul>
		 * <li>{@link #discCacheSize(int)}</li>
		 * <li>{@link #discCacheFileCount(int)}</li>
		 * <li>{@link #discCacheFileNameGenerator(FileNameGenerator)}</li>
		 * </ul>
		 */
		public Builder discCache(DiscCacheAware discCache) {
			if (discCacheSize > 0 || discCacheFileCount > 0) {
				Log.w("","WARNING_OVERLAP_DISC_CACHE_PARAMS");
			}
			if (discCacheFileNameGenerator != null) {
				Log.w("","WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR");
			}

			this.discCache = discCache;
			return this;
		}

		/**
		 * 设置默认的图像显示选项
		 */
		public Builder defaultDisplayImageOptions(DisplayImageOptions defaultDisplayImageOptions) {
			this.defaultDisplayImageOptions = defaultDisplayImageOptions;
			return this;
		}

		/** 详细日志配置 */
		public Builder enableLogging() {
			this.loggingEnabled = true;
			return this;
		}

		/** 建立配置对象 */
		public ImageLoaderConfiguration build() {
			initEmptyFiledsWithDefaultValues();
			return new ImageLoaderConfiguration(this);
		}

		private void initEmptyFiledsWithDefaultValues() {
			if (taskExecutor == null) {
				taskExecutor = DefaultConfigurationFactory.createExecutor(threadPoolSize, threadPriority, tasksProcessingType);
			} else {
				customExecutor = true;
			}
			if (taskExecutorForCachedImages == null) {
				taskExecutorForCachedImages = DefaultConfigurationFactory.createExecutor(threadPoolSize, threadPriority, tasksProcessingType);
			} else {
				customExecutorForCachedImages = true;
			}
			if (discCache == null) {
				if (discCacheFileNameGenerator == null) {
					discCacheFileNameGenerator = DefaultConfigurationFactory.createFileNameGenerator();
				}
				discCache = DefaultConfigurationFactory.createDiscCache(context, discCacheFileNameGenerator, discCacheSize, discCacheFileCount);
			}
			if (memoryCache == null) {
				memoryCache = DefaultConfigurationFactory.createMemoryCache(memoryCacheSize);
			}
			if (denyCacheImageMultipleSizesInMemory) {
				memoryCache = new FuzzyKeyMemoryCache<String, Bitmap>(memoryCache, MemoryCacheUtil.createFuzzyKeyComparator());
			}
			if (downloader == null) {
				downloader = DefaultConfigurationFactory.createImageDownloader(context);
			}
			if (decoder == null) {
				decoder = DefaultConfigurationFactory.createImageDecoder(loggingEnabled);
			}
			if (defaultDisplayImageOptions == null) {
				defaultDisplayImageOptions = DisplayImageOptions.createSimple();
			}
		}
	}
}

