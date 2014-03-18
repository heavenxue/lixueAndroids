package com.lixueandroid.imgloader;

/**
 * 生成sd卡上的文件名
 * 
 * @author lixue
 * @since 1.3.1
 */
public interface FileNameGenerator {

	/** 通过URI定义的图像生成唯一的文件名*/
	public abstract String generate(String imageUri);
}
