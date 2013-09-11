package com.lixueandroid.imgloader;


/**
 * 作为图像URI命名图像文件{@linkplain String#hashCode() hashcode}（文件名生成器）
 * 
 * @author lixue
 * @since 1.3.1
 */
public class HashCodeFileNameGenerator implements FileNameGenerator {
	@Override
	public String generate(String imageUri) {
		return String.valueOf(imageUri.hashCode());
	}
}
