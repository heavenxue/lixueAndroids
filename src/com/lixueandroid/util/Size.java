package com.lixueandroid.util;

/**
 * 尺寸
 * @author xiaopan
 *
 */
public class Size {
	private int width;	//宽
	private int height;	//高
	
	public Size() {}
	
	public Size(int width, int height){
		setWidth(width);
		setHeight(height);
	}
	
	/**
	 * 获取宽
	 * @return 宽
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 设置宽 
	 * @param width 宽
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 获取高
	 * @return 高
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 设置高
	 * @param width 高
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}