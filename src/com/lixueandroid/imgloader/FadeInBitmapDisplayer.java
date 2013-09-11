package com.lixueandroid.imgloader;

import android.graphics.Bitmap;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


/**
 * 用渐隐式动画来显示图像
 * 
 * @author lixue
 * @since 1.6.4
 */
public class FadeInBitmapDisplayer implements BitmapDisplayer {

	private final int durationMillis;

	public FadeInBitmapDisplayer(int durationMillis) {
		this.durationMillis = durationMillis;
	}

	@Override
	public Bitmap display(Bitmap bitmap, ImageView imageView) {
		imageView.setImageBitmap(bitmap);
		animate(imageView, durationMillis);
		return bitmap;
	}

	/**
	 *有渐隐效果的动画
	 * 
	 * @param 用来显示图像的imageView
	 * @param 动画的长度（以毫秒为单位）
	 */
	public static void animate(ImageView imageView, int durationMillis) {
		AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
		fadeImage.setDuration(durationMillis);
		fadeImage.setInterpolator(new DecelerateInterpolator());
		imageView.startAnimation(fadeImage);
	}

}
