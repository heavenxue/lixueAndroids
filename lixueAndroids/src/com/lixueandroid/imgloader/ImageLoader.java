package com.lixueandroid.imgloader;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


/**
 * 显示和加载图像的图像加载器<br />
 * <b>注意:</b> {@link #init(ImageLoaderConfiguration)} 这个方法必须在其它方法前被调用.
 * 
 * @author lixue
 * @since 1.0.0
 */
public class ImageLoader {

	public static final String TAG = ImageLoader.class.getSimpleName();

	static final String LOG_INIT_CONFIG = "初始化ImageLoader的配置";
	static final String LOG_DESTROY = "摧毁ImageLoader";
	static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "从内存缓存中加载图像[%s]";

	private static final String WARNING_RE_INIT_CONFIG = "在ImageLoader已经初始化之前尝试初始化，"
			+ "为了重新初始化新的配置ImageLoader，首先要呼叫ImageLoader.destroy().";
	private static final String ERROR_WRONG_ARGUMENTS = "错误参数被传递给displayImage()方法（ImageView的引用不能为空）";
	private static final String ERROR_NOT_INIT = "ImageLoader的配置使用前必须初始化";
	private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader的配置不能被初始化为null";

	private ImageLoaderConfiguration configuration;
	private ImageLoaderEngine engine;

	private final ImageLoadingListener emptyListener = new SimpleImageLoadingListener();
	private final BitmapDisplayer fakeBitmapDisplayer = new FakeBitmapDisplayer();

	private volatile static ImageLoader instance;

	/**返回 ImageLoader单例模式 */
	public static ImageLoader getInstance() {
		if (instance == null) {
			synchronized (ImageLoader.class) {
				if (instance == null) {
					instance = new ImageLoader();
				}
			}
		}
		return instance;
	}

	protected ImageLoader() {
	}

	/**
	 *	ImageLoader的实例初始化配置<br />
	 * 如果配置之前设置（isInited()==true），那么此方法什么都不做。<br/>
	 * 要强制初始化,首先你应该销毁ImageLoader使用新的配置。
	 * 
	 * @param configuration {@linkplain ImageLoaderConfiguration ImageLoader configuration}
	 * @throws IllegalArgumentException if <b>configuration</b> 参数为 null
	 */
	public synchronized void init(ImageLoaderConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
		}
		if (this.configuration == null) {
			if (configuration.loggingEnabled) Log.d("",LOG_INIT_CONFIG);
			engine = new ImageLoaderEngine(configuration);
			this.configuration = configuration;
		} else {
			Log.w("", WARNING_RE_INIT_CONFIG);
		}
	}

	/**
	 * 如果ImageLoader初始化配置则返回true,否则返回false.
	 */
	public boolean isInited() {
		return configuration != null;
	}

	/**
	 * 增加显示图像任务执行池，当它打开时，图像就会以ImageView控件上显示出来。
	 * 默认情况下，来自ImageLoaderConfiguration的DisplayImageOptions必须被用
	 * <b>注意:</b> 在此方法被调用前，init(ImageLoaderConfiguration)方法必须被调用
	 * 
	 * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageView {@link ImageView} which should display image
	 * 
	 * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 * @throws IllegalArgumentException if passed <b>imageView</b> is null
	 */
	public void displayImage(String uri, ImageView imageView) {
		displayImage(uri, imageView, null, null);
	}

	/**
	 * 增加显示图像任务执行池，当它打开时，图像就会以ImageView控件上显示出来
	 * <b>注意:</b> init(ImageLoaderConfiguration)方法必须被调用
	 * 
	 * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageView  用来显示图像
	 * @param options 如果为null,默认显示图像选项 ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
	 * 
	 * @throws IllegalStateException 如果init(ImageLoaderConfiguration)方法之前没调用就会抛此异常
	 * @throws IllegalArgumentException 如果imageView为null就会抛此异常
	 */
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
		displayImage(uri, imageView, options, null);
	}

	/**
	 * 增加显示图像任务执行池，当它打开时，图像就会以ImageView控件上显示出来.<br />
	 * 默认情况下，来自ImageLoaderConfiguration的DisplayImageOptions必须被用
	 * <b>注意:</b> 在此方法被调用前，init(ImageLoaderConfiguration)方法必须被调用
	 * 
	 * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageView  用来显示图像
	 * @param listener {@linkplain ImageLoadingListener Listener} for image loading process.监听UI线程上触发事件。
	 * 
	 * @throws IllegalStateException 如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 * @throws IllegalArgumentException 如果imageView为null就会抛此异常
	 */
	public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
		displayImage(uri, imageView, null, listener);
	}

	/**
	 * 增加显示图像任务执行池，当它打开时，图像就会以ImageView控件上显示出来
	 * <b>注意:</b> init(ImageLoaderConfiguration)方法必须被调用
	 * 
	 * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageView  用来显示图像
	 * @param options 如果为null,默认显示图像选项 ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
	 * @param listener {@linkplain ImageLoadingListener Listener} for image loading process.监听UI线程上触发事件。
	 * 
	 * @throws IllegalStateException 如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 * @throws IllegalArgumentException 如果imageView为null就会抛此异常
	 * 
	 */
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
		checkConfiguration();
		if (imageView == null) {
			throw new IllegalArgumentException(ERROR_WRONG_ARGUMENTS);
		}
		if (listener == null) {
			listener = emptyListener;
		}
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}

		if (uri == null || uri.length() == 0) {
			engine.cancelDisplayTaskFor(imageView);
			listener.onLoadingStarted(uri, imageView);
			if (options.shouldShowImageForEmptyUri()) {
				imageView.setImageResource(options.getImageForEmptyUri());
			} else {
				imageView.setImageBitmap(null);
			}
			listener.onLoadingComplete(uri, imageView, null);
			return;
		}

		ImageSize targetSize = ImageSizeUtils.defineTargetSizeForView(imageView, configuration.maxImageWidthForMemoryCache,configuration.maxImageHeightForMemoryCache);
		String memoryCacheKey = MemoryCacheUtil.generateKey(uri, targetSize);
		engine.prepareDisplayTaskFor(imageView, memoryCacheKey);

		listener.onLoadingStarted(uri, imageView);
		Bitmap bmp = configuration.memoryCache.get(memoryCacheKey);
		if (bmp != null && !bmp.isRecycled()) {
			if (configuration.loggingEnabled) Log.i(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, memoryCacheKey);

			if (options.shouldPostProcess()) {
				ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo(uri, imageView, targetSize, memoryCacheKey, options, listener,
						engine.getLockForUri(uri));
				ProcessAndDisplayImageTask displayTask = new ProcessAndDisplayImageTask(engine, bmp, imageLoadingInfo, options.getHandler());
				engine.submit(displayTask);
			} else {
				options.getDisplayer().display(bmp, imageView);
				listener.onLoadingComplete(uri, imageView, bmp);
			}
		} else {
			if (options.shouldShowStubImage()) {
				imageView.setImageResource(options.getStubImage());
			} else {
				if (options.isResetViewBeforeLoading()) {
					imageView.setImageBitmap(null);
				}
			}

			ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo(uri, imageView, targetSize, memoryCacheKey, options, listener, engine.getLockForUri(uri));
			LoadAndDisplayImageTask displayTask = new LoadAndDisplayImageTask(engine, imageLoadingInfo, options.getHandler());
			engine.submit(displayTask);
		}
	}

	/**
	 * 增加显示图像任务执行池，可以调用ImageLoadingListener#onLoadingComplete(Bitmap) 回调来显示图像
	 * <b>注意:</b>init(ImageLoaderConfiguration)方法必须被调用
	 * 
	 * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param listener {@linkplain ImageLoadingListener Listener} for image loading process. 监听UI线程上触发事件。
	 * 
	 * @throws IllegalStateException如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 */
	public void loadImage(String uri, ImageLoadingListener listener) {
		loadImage(uri, null, null, listener);
	}

	/**
	 *  增加显示图像任务执行池，可以调用ImageLoadingListener#onLoadingComplete(Bitmap) 回调来显示图像
	 * <b>注意:</b>init(ImageLoaderConfiguration)方法必须被调用
	 * 
	 * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param minImageSize 设置最小的尺寸
	 * 
	 * @param listener {@linkplain ImageLoadingListener Listener} for image loading process. 监听UI线程上触发事件。
	 * 
	 * @throws IllegalStateException 如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 */
	public void loadImage(String uri, ImageSize minImageSize, ImageLoadingListener listener) {
		loadImage(uri, minImageSize, null, listener);
	}

	/**
	 * 增加显示图像任务执行池，可以调用ImageLoadingListener#onLoadingComplete(Bitmap) 回调来显示图像
	 * <b>注意:</b>init(ImageLoaderConfiguration)方法必须被调用
	 * 
	 * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param options 如果为null,默认显示图像选项 ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
	 * @param listener {@linkplain ImageLoadingListener Listener} for image loading process.监听UI线程上触发事件。
	 * 
	 * @throws IllegalStateException 如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 */
	public void loadImage(String uri, DisplayImageOptions options, ImageLoadingListener listener) {
		loadImage(uri, null, options, listener);
	}

	/**
	 * 增加显示图像任务执行池，可以调用ImageLoadingListener#onLoadingComplete(Bitmap) 回调来显示图像
	 * <b>注意:</b>init(ImageLoaderConfiguration)方法必须被调用
	 * 
	 * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param targetImageSize 设置最小的尺寸
	 * 
	 * @param options 如果为null,默认显示图像选项 ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
	 * @param listener {@linkplain ImageLoadingListener Listener} for image loading process. 监听UI线程上触发事件。
	 * 
	 * @throws IllegalStateException 如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 */
	public void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options, ImageLoadingListener listener) {
		checkConfiguration();
		if (targetImageSize == null) {
			targetImageSize = new ImageSize(configuration.maxImageWidthForMemoryCache, configuration.maxImageHeightForMemoryCache);
		}
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}

		DisplayImageOptions optionsWithFakeDisplayer;
		if (options.getDisplayer() instanceof FakeBitmapDisplayer) {
			optionsWithFakeDisplayer = options;
		} else {
			optionsWithFakeDisplayer = new DisplayImageOptions.Builder().cloneFrom(options).displayer(fakeBitmapDisplayer).build();
		}

		ImageView fakeImage = new ImageView(configuration.context);
		fakeImage.setLayoutParams(new LayoutParams(targetImageSize.getWidth(), targetImageSize.getHeight()));
		fakeImage.setScaleType(ScaleType.CENTER_CROP);

		displayImage(uri, fakeImage, optionsWithFakeDisplayer, listener);
	}

	/**
	 * 验证ImageLoader的配置是否初始化
	 * 
	 * @throws IllegalStateException 未被初始化就抛异常
	 */
	private void checkConfiguration() {
		if (configuration == null) {
			throw new IllegalStateException(ERROR_NOT_INIT);
		}
	}

	/**
	 * 返回内存缓存
	 * 
	 * @throws IllegalStateException 未返回则抛异常
	 */
	public MemoryCacheAware<String, Bitmap> getMemoryCache() {
		checkConfiguration();
		return configuration.memoryCache;
	}

	/**
	 * 清除内存缓存
	 * 
	 * @throws IllegalStateException  如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 */
	public void clearMemoryCache() {
		checkConfiguration();
		configuration.memoryCache.clear();
	}

	/**
	 * 返回磁盘缓存
	 * 
	 * @throws IllegalStateException  如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 */
	public DiscCacheAware getDiscCache() {
		checkConfiguration();
		return configuration.discCache;
	}

	/**
	 * 清除磁盘缓存
	 * 
	 * @throws IllegalStateException  如果init(ImageLoaderConfiguration)方法没被调用会抛此异常
	 */
	public void clearDiscCache() {
		checkConfiguration();
		configuration.discCache.clear();
	}

	/** 返回URI */
	public String getLoadingUriForView(ImageView imageView) {
		return engine.getLoadingUriForView(imageView);
	}

	/**
	 * 取消加载显示图像处理的任务
	 * Cancel the task of loading and displaying image for passed {@link ImageView}.
	 * 
	 * @param imageView 显示图像的控件
	 */
	public void cancelDisplayTask(ImageView imageView) {
		engine.cancelDisplayTaskFor(imageView);
	}

	/**
	 * 允许或拒绝ImageLoader的从网络上下载图片.<br />
	 * <br />
	 * 如果下载拒绝则图像不被缓存，则可以回调 ImageLoadingListener#onLoadingFailed(String, View, FailReason)事件
	 * 
	 * @param denyNetworkDownloads pass <b>true</b> - 拒绝; <b>false</b> -允许
	 */
	public void denyNetworkDownloads(boolean denyNetworkDownloads) {
		engine.denyNetworkDownloads(denyNetworkDownloads);
	}

	/**
	 * ImageLoader是否用FlushedInputStream从网络下载来处理问题
	 * 
	 * 设置ImageLoader的选项，是否将网络下载使用FlushedInputStream处理<A HREF =“http://code.google.com/p/android/issues/detail?id=6066”>这个已知的问题</ A>。
	 * 
	 * @param handleSlowNetwork pass <b>true</b> - 用; <b>false</b>不用
	 */
	public void handleSlowNetwork(boolean handleSlowNetwork) {
		engine.handleSlowNetwork(handleSlowNetwork);
	}

	/**
	 * 暂停ImageLoader,直到ImageLoader的可执行任务恢复<br />
	 */
	public void pause() {
		engine.pause();
	}

	/** 继续等待“负载显示”任务*/
	public void resume() {
		engine.resume();
	}

	/**
	 * 取消所有正在运行和预定的显示图像任务.<br />
	 */
	public void stop() {
		engine.stop();
	}

	/**
	 * 销毁ImageLoader并且清除当前配置
	 */
	public void destroy() {
		if (configuration != null && configuration.loggingEnabled) Log.d("",LOG_DESTROY);
		stop();
		engine = null;
		configuration = null;
	}
}