package com.lixueandroid.imgloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 用 {@link ImageView}来显示. 实现可以申请一些改变位图或动画来显示图片.<br />
 * 实现必须是线程安全的.
 * 
 * @author lixue
 * @since 1.5.6
 */
public interface BitmapDisplayer {
	/**
	 * 在 {@link ImageView}里面显示图像.显示的图像应该被返回.<br />
	 * <b>注意:</b>这种方法被称为UI线程，所以它强烈建议不要做任何负载活动.
	 * 
	 * @param 图像源
	 * @param imageView {@linkplain ImageView Image view} 来显示图像
	 * @return 返回被显示在{@link ImageView}上的图像
	 */
	Bitmap display(Bitmap bitmap, ImageView imageView);
}