package com.lixueandroid.imgloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * 网络下载慢时的处理器
 * Decorator. Handles <a href="http://code.google.com/p/android/issues/detail?id=6066">this problem</a> on slow networks
 * using {@link FlushedInputStream}.
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.1
 */
public class SlowNetworkImageDownloader implements ImageDownloader {

	private final ImageDownloader wrappedDownloader;

	public SlowNetworkImageDownloader(ImageDownloader wrappedDownloader) {
		this.wrappedDownloader = wrappedDownloader;
	}

	@Override
	public InputStream getStream(String imageUri, Object extra) throws IOException {
		InputStream imageStream = wrappedDownloader.getStream(imageUri, extra);
		switch (Scheme.ofUri(imageUri)) {
			case HTTP:
			case HTTPS:
				return new FlushedInputStream(imageStream);
			default:
				return imageStream;
		}
	}
}