package com.lixueandroid.imgloader;

import java.io.IOException;
import java.io.InputStream;


/**
 * 根据uri获取图像.<br />
 * 实现必须是线程安全的。
 * 
 * @author lixue
 * @since 1.4.0
 */
public interface ImageDownloader {
	/**
	 *通过uri接收图像的输入流{@link InputStream}
	 * 
	 * @param imageUri Image URI
	 * @param extra 辅助对象传递给 {@link DisplayImageOptions.Builder#extraForDownloader(Object)DisplayImageOptions.extraForDownloader(Object)};可以为null
	 * @return {@link InputStream} of image
	 * @throws IOException如果出现异常，跑出输入流异常
	 * @throws UnsupportedOperationException 如果图像的URI有不支持的方案（协议）
	 */
	InputStream getStream(String imageUri, Object extra) throws IOException;

	/** Represents supported schemes(protocols) of URI. Provides convenient methods for work with schemes and URIs. */
	public enum Scheme {HTTP("http"), HTTPS("https"), FILE("file"), CONTENT("content"), ASSETS("assets"), DRAWABLE("drawable"), UNKNOWN("");

		private String scheme;
		private String uriPrefix;

		Scheme(String scheme) {
			this.scheme = scheme;
			uriPrefix = scheme + "://";
		}

		/**
		 *自定义传入的URI方案
		 * 
		 * @param uri URI for scheme detection
		 * @return Scheme of incoming URI
		 */
		public static Scheme ofUri(String uri) {
			if (uri != null) {
				for (Scheme s : values()) {
					if (s.belongsTo(uri)) {
						return s;
					}
				}
			}
			return UNKNOWN;
		}

		private boolean belongsTo(String uri) {
			return uri.startsWith(uriPrefix);
		}

		/** Appends scheme to incoming path */
		public String wrap(String path) {
			return uriPrefix + path;
		}

		/** Removed scheme part ("scheme://") from incoming URI */
		public String crop(String uri) {
			if (!belongsTo(uri)) {
				throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, scheme));
			}
			return uri.substring(uriPrefix.length());
		}
	}
}