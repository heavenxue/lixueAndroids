package com.lixueandroid.imgloader;

import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


/**
 *图像类型简化为两种类型: {@link #FIT_INSIDE} and {@link #CROP}
 * 
 * @author lixue
 * @since 1.6.1
 */
public enum ViewScaleType {
	/**
	 * 均匀缩放图像（保持图像的长宽比），这样的尺寸（宽度和高度)图像将等于或小于相应的维度的视图。
	 */
	FIT_INSIDE,
	/**
	 * 均匀缩放图像（保持图像的长宽比），这样的尺寸（宽度和高度）图像将等于或大于相应的视图维度。
	 */
	CROP;

	/**
	 *自定义图像类型
	 * 
	 * @param imageView {@link ImageView}
	 * @return {@link #FIT_INSIDE} for
	 *         <ul>
	 *         <li>{@link ScaleType#FIT_CENTER}</li>
	 *         <li>{@link ScaleType#FIT_XY}</li>
	 *         <li>{@link ScaleType#FIT_START}</li>
	 *         <li>{@link ScaleType#FIT_END}</li>
	 *         <li>{@link ScaleType#CENTER_INSIDE}</li>
	 *         </ul>
	 *         {@link #CROP} for
	 *         <ul>
	 *         <li>{@link ScaleType#CENTER}</li>
	 *         <li>{@link ScaleType#CENTER_CROP}</li>
	 *         <li>{@link ScaleType#MATRIX}</li>
	 *         </ul>
	 *         ,
	 * 
	 */
	public static ViewScaleType fromImageView(ImageView imageView) {
		switch (imageView.getScaleType()) {
			case FIT_CENTER:
			case FIT_XY:
			case FIT_START:
			case FIT_END:
			case CENTER_INSIDE:
				return FIT_INSIDE;
			case MATRIX:
			case CENTER:
			case CENTER_CROP:
			default:
				return CROP;
		}
	}
}