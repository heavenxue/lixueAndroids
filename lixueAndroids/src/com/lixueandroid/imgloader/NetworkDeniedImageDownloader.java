package com.lixueandroid.imgloader;

import java.io.IOException;
import java.io.InputStream;



/**
 * 从网络下载图像
 *在大多数情况下，这不应该被直接使用下载工具。
 * 
 * @author lixue
 * @since 1.8.0
 */
public class NetworkDeniedImageDownloader implements ImageDownloader {

	private final ImageDownloader wrappedDownloader;

	public NetworkDeniedImageDownloader(ImageDownloader wrappedDownloader) {
		this.wrappedDownloader = wrappedDownloader;
	}

	@Override
	public InputStream getStream(String imageUri, Object extra) throws IOException {
		switch (Scheme.ofUri(imageUri)) {
			case HTTP:
			case HTTPS:
				throw new IllegalStateException();
			default:
				return wrappedDownloader.getStream(imageUri, extra);
		}
	}
}
