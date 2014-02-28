package com.lixueandroid.activity;

import me.xiaopan.easyandroid.app.BaseActivity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;

import com.lixue.lixueandroid.R;

/**
 * ImageView能播放动画图片的实例
 * @author Administrator
 *
 */
public class AnimationDrawableActivity extends BaseActivity {
	private ImageView animationImg;
	private AnimationDrawable ad ;

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_animationdrawable);
		animationImg = (ImageView) findViewById(R.id.img_animationdrawable);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		animationImg.setBackgroundResource(R.drawable.myanimation);
		ad = (AnimationDrawable) animationImg.getBackground();
		animationImg.getViewTreeObserver().addOnPreDrawListener(opdl);

	}
	
	// 当一个视图树将要绘制时产生事件，可以添加一个其事件处理函数
	OnPreDrawListener opdl = new OnPreDrawListener() {
		@Override
		public boolean onPreDraw() {
			ad.start();
			return true; // 注意此行返回的值
		}
	};
	
}
