package com.lixueandroid.imgloader;

/**
 * 图像加载或显示失败管理器
 * 
 * @author lixue
 * @since 1.0.0
 */
public class FailReason {

	private final FailType type;

	private final Throwable cause;

	public FailReason(FailType type, Throwable cause) {
		this.type = type;
		this.cause = cause;
	}

	/**
	 * @return {@linkplain FailType Fail type}
	 */
	public FailType getType() {
		return type;
	}

	/**
	 * @return Thrown exception/error, can be <b>null</b>
	 */
	public Throwable getCause() {
		return cause;
	}

	/** Presents type of fail while image loading */
	public static enum FailType {
		/** Input/output error. Can be caused by network communication fail or error while caching image on file system. */
		IO_ERROR,
		/**
		 * Error while
		 * {@linkplain android.graphics.BitmapFactory#decodeStream(java.io.InputStream, android.graphics.Rect, android.graphics.BitmapFactory.Options)
		 * decode image to Bitmap}
		 */
		DECODING_ERROR,
		/**
		 * {@linkplain com.nostra13.universalimageloader.core.ImageLoader#denyNetworkDownloads(boolean) Network
		 * downloads are denied} and requested image wasn't cached in disc cache before.
		 */
		NETWORK_DENIED,
		/** Not enough memory to create needed Bitmap for image */
		OUT_OF_MEMORY,
		/** Unknown error was occurred while loading image */
		UNKNOWN
	}
}