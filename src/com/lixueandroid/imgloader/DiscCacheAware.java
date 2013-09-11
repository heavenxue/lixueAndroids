package com.lixueandroid.imgloader;

import java.io.File;

/**
 * sd卡盘缓存接口
 * 
 * @author lixue
 * @since 1.0.0
 */
public interface DiscCacheAware {
	/**
	 * 这种方法不能保存文件的文件系统上的事实。它被称为图像缓存在缓存目录中，它是内存中的位图解码。这种秩序是必需的，以防止可能后，它光盘上wascached的的前有人试过解码位图文件删除。
	 */
	void put(String key, File file);

	/**
	 * Returns {@linkplain File file object} 适当的传入键.<br />
	 * <b>NOTE:</b> Must <b>not to return</b> a null. Method must return specific {@linkplain File file object} for
	 * incoming key whether file exists or not.
	 */
	File get(String key);

	/**清除缓存目录*/
	void clear();
}