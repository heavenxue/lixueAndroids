package com.lixueandroid.imgloader;

import java.io.IOException;

import android.graphics.Bitmap;


/**
 * 图像解码器 {@link Bitmap}.
 * 
 * @author lixue 
 * @since 1.8.3
 * @see ImageDecodingInfo
 */
public interface ImageDecoder {

	/**
	 *根据目标的大小和其他参数解码图像
	 * 
	 * @param imageDecodingInfo 
	 * @return
	 * @throws IOException
	 */
	Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException;
}