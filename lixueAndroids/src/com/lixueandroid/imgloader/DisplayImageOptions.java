package com.lixueandroid.imgloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;


/**
 *包含图像显示的选项. 定义:
 * <ul>
 * <li>在图像加载过程中，是否将存根的图像将显示在imageView控件上</li>
 * <li>如果空的URI传递,是否将存根的图像将显示在imageView控件上</li>
 * <li>如果图像加载失败，是否将存根的图像将显示在imageView控件上</li>
 * <li>在图像开始加载时，是否应该重置</li>
 * <li>是否将加载的图像缓存在内存中</li>
 * <li>是否将加载的图像将缓存在光盘上</li>
 * <li>图像缩放类型</li>
 * <li>解码选项（包括位图解码配置）</li>
 * <li>加载的图像前延迟时间</li>
 * <li>辅助对象将被传递给 ImageDownloader</li>
 * <li>位图图像的预处理器（之前缓存在内存里）</li>
 * <li>位图图像的后处理器（缓存在内存里后，才显示）</li>
 * <li>如何解码显示</li>
 * </ul>
 * 
 * 你可以创建一个实例
 * 可以new DisplayImageOptions.Builder().cacheInMemory().showStubImage().build
 * 或者用静态方法：createSimple()
 * 
 * @author lixue
 * @since 1.0.0
 */
public final class DisplayImageOptions {

	private final int stubImage;
	private final int imageForEmptyUri;
	private final int imageOnFail;
	private final boolean resetViewBeforeLoading;
	private final boolean cacheInMemory;
	private final boolean cacheOnDisc;
	private final ImageScaleType imageScaleType;
	private final Options decodingOptions;
	private final int delayBeforeLoading;
	private final Object extraForDownloader;
	private final BitmapProcessor preProcessor;
	private final BitmapProcessor postProcessor;
	private final BitmapDisplayer displayer;
	private final Handler handler;

	private DisplayImageOptions(Builder builder) {
		stubImage = builder.stubImage;
		imageForEmptyUri = builder.imageForEmptyUri;
		imageOnFail = builder.imageOnFail;
		resetViewBeforeLoading = builder.resetViewBeforeLoading;
		cacheInMemory = builder.cacheInMemory;
		cacheOnDisc = builder.cacheOnDisc;
		imageScaleType = builder.imageScaleType;
		decodingOptions = builder.decodingOptions;
		delayBeforeLoading = builder.delayBeforeLoading;
		extraForDownloader = builder.extraForDownloader;
		preProcessor = builder.preProcessor;
		postProcessor = builder.postProcessor;
		displayer = builder.displayer;
		handler = builder.handler;
	}

	public boolean shouldShowStubImage() {
		return stubImage != 0;
	}

	public boolean shouldShowImageForEmptyUri() {
		return imageForEmptyUri != 0;
	}

	public boolean shouldShowImageOnFail() {
		return imageOnFail != 0;
	}

	public boolean shouldPreProcess() {
		return preProcessor != null;
	}

	public boolean shouldPostProcess() {
		return postProcessor != null;
	}

	public boolean shouldDelayBeforeLoading() {
		return delayBeforeLoading > 0;
	}

	public int getStubImage() {
		return stubImage;
	}

	public int getImageForEmptyUri() {
		return imageForEmptyUri;
	}

	public int getImageOnFail() {
		return imageOnFail;
	}

	public boolean isResetViewBeforeLoading() {
		return resetViewBeforeLoading;
	}

	public boolean isCacheInMemory() {
		return cacheInMemory;
	}

	public boolean isCacheOnDisc() {
		return cacheOnDisc;
	}

	public ImageScaleType getImageScaleType() {
		return imageScaleType;
	}

	public Options getDecodingOptions() {
		return decodingOptions;
	}

	public int getDelayBeforeLoading() {
		return delayBeforeLoading;
	}

	public Object getExtraForDownloader() {
		return extraForDownloader;
	}

	public BitmapProcessor getPreProcessor() {
		return preProcessor;
	}

	public BitmapProcessor getPostProcessor() {
		return postProcessor;
	}

	public BitmapDisplayer getDisplayer() {
		return displayer;
	}

	public Handler getHandler() {
		return (handler == null ? new Handler() : handler);
	}

	/**
	 * Builder for {@link DisplayImageOptions}
	 * 
	 * @author lixue
	 */
	public static class Builder {
		private int stubImage = 0;
		private int imageForEmptyUri = 0;
		private int imageOnFail = 0;
		private boolean resetViewBeforeLoading = false;
		private boolean cacheInMemory = false;
		private boolean cacheOnDisc = false;
		private ImageScaleType imageScaleType = ImageScaleType.IN_SAMPLE_POWER_OF_2;
		private Options decodingOptions = new Options();
		private int delayBeforeLoading = 0;
		private Object extraForDownloader = null;
		private BitmapProcessor preProcessor = null;
		private BitmapProcessor postProcessor = null;
		private BitmapDisplayer displayer = DefaultConfigurationFactory.createBitmapDisplayer();
		private Handler handler = null;

		public Builder() {
			//如果设置为true，如果系统需要回收内存，则生成的位图将分配它的像素使得它们可以被清除，
			decodingOptions.inPurgeable = true;
			//这个字段依赖于inPurgeable。如果inPurgeable为false，那么这个字段被忽略。
			//如果inPurgeable为true，那么这个字段确定是否可以将位图的输入数据（InputStream中，数组等），或者如果它必须作出深刻的副本共享一个参考。
			decodingOptions.inInputShareable = true;
		}

		/**
		 * 在图像加载过程中，设置一个图像
		 * 
		 * @param stubImageRes Stub image resource
		 */
		public Builder showStubImage(int stubImageRes) {
			stubImage = stubImageRes;
			return this;
		}

		/**
		 *如果URI为空，设置一个图像
		 * 
		 * @param imageRes Image resource
		 */
		public Builder showImageForEmptyUri(int imageRes) {
			imageForEmptyUri = imageRes;
			return this;
		}

		/**
		 * 当图像加载出错时，设置一个图像
		 * 
		 * @param imageRes Image resource
		 */
		public Builder showImageOnFail(int imageRes) {
			imageOnFail = imageRes;
			return this;
		}

		/**图像开始加载前被重置*/
		public Builder resetViewBeforeLoading() {
			resetViewBeforeLoading = true;
			return this;
		}

		/**加载的图像被缓存在内存中 */
		public Builder cacheInMemory() {
			cacheInMemory = true;
			return this;
		}

		/**加载的图像被缓存在磁盘中 */
		public Builder cacheOnDisc() {
			cacheOnDisc = true;
			return this;
		}

		/**
		 *设置解码图像，当定义解码的图像的模式时这个参数被设置 默认值为 - {@link ImageScaleType#IN_SAMPLE_POWER_OF_2}
		 */
		public Builder imageScaleType(ImageScaleType imageScaleType) {
			this.imageScaleType = imageScaleType;
			return this;
		}

		/** 设置解码图像的配置. 默认值为 - {@link Bitmap.Config#ARGB_8888} */
		public Builder bitmapConfig(Bitmap.Config bitmapConfig) {
			decodingOptions.inPreferredConfig = bitmapConfig;
			return this;
		}

		/**
		 * 设置选项<br />
		 * <b>备注:</b> {@link Options#inSampleSize} of incoming options will <b>NOT</b> be considered. Library
		 * calculate the most appropriate sample size itself according yo {@link #imageScaleType(ImageScaleType)}
		 * options.<br />
		 * <b>NOTE:</b> This option overlaps {@link #bitmapConfig(android.graphics.Bitmap.Config) bitmapConfig()}
		 * option.
		 */
		public Builder decodingOptions(Options decodingOptions) {
			this.decodingOptions = decodingOptions;
			return this;
		}

		/** 设置延迟时间. 默认 - 没有延迟. */
		public Builder delayBeforeLoading(int delayInMillis) {
			this.delayBeforeLoading = delayInMillis;
			return this;
		}

		/** 设置将被传入的辅助对象（额外对象） {@link ImageDownloader#getStream(java.net.URI, Object)} */
		public Builder extraForDownloader(Object extra) {
			this.extraForDownloader = extra;
			return this;
		}

		/**
		 * 设置位处理器，这将是处理位图之前，他们将在内存中缓存。所以内存缓存将包含传入的预处理器所处理的位图。
		 * 即使在内存中缓存被禁用，图片将被预处理。
		 */
		public Builder preProcessor(BitmapProcessor preProcessor) {
			this.preProcessor = preProcessor;
			return this;
		}

		/**
		 *设置位图处理器，这将是处理位图之前，他们将被显示在{@ ImageView的}，但之后他们就会被保存在内存中缓存。
		 */
		public Builder postProcessor(BitmapProcessor postProcessor) {
			this.postProcessor = postProcessor;
			return this;
		}

		/**
		 * 设置自定义图像加载任务 {@link BitmapDisplayer displayer} . 
		 * 默认值 -{@link DefaultConfigurationFactory#createBitmapDisplayer()}
		 */
		public Builder displayer(BitmapDisplayer displayer) {
			this.displayer = displayer;
			return this;
		}

		/**
		 * 设置自定誃的{@linkplain Handler handler} 为显示图像和触发 {@linkplain ImageLoadingListenerlistener} 事件.
		 * 
		 */
		public Builder handler(Handler handler) {
			this.handler = handler;
			return this;
		}

		/** 	为将传入的选项设置所有的选项 */
		public Builder cloneFrom(DisplayImageOptions options) {
			stubImage = options.stubImage;
			imageForEmptyUri = options.imageForEmptyUri;
			imageOnFail = options.imageOnFail;
			resetViewBeforeLoading = options.resetViewBeforeLoading;
			cacheInMemory = options.cacheInMemory;
			cacheOnDisc = options.cacheOnDisc;
			imageScaleType = options.imageScaleType;
			decodingOptions = options.decodingOptions;
			delayBeforeLoading = options.delayBeforeLoading;
			extraForDownloader = options.extraForDownloader;
			preProcessor = options.preProcessor;
			postProcessor = options.postProcessor;
			displayer = options.displayer;
			handler = options.handler;
			return this;
		}

		/** 建立配置的对象 {@link DisplayImageOptions}  */
		public DisplayImageOptions build() {
			return new DisplayImageOptions(this);
		}
	}

	/**
	 * 创建选项（适用单一显示）:
	 * <ul>
	 * <li><b>在加载前视图将不被重置not</b></li>
	 * <li>加载的图像将 <b>不</b> 被缓存在内存中</li>
	 * <li>加载的图像将 <b>不</b> 被缓存在磁盘上</li>
	 * <li>{@link ImageScaleType#IN_SAMPLE_POWER_OF_2} 这个类型就被应用</li>
	 * <li>{@link Bitmap.Config#ARGB_8888}这个配置将被应用于将被解码的图像</li>
	 * <li>{@link SimpleBitmapDisplayer}这个被应用于图像的显示</li>
	 * </ul>
	 * 
	 *这些选项是适当的简单的一次性显示的图像 (来自 drawables 或 Internet) .
	 */
	public static DisplayImageOptions createSimple() {
		return new Builder().build();
	}
}