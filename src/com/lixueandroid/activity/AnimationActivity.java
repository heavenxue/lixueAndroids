package com.lixueandroid.activity;

import me.xiaopan.easyandroid.app.BaseActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.lixue.lixueandroid.R;

/**
 * 一些动画的实例
 * @author Administrator
 *
 */
public class AnimationActivity extends BaseActivity{
	private ImageView imgShow;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_animation);
		imgShow=(ImageView) findViewById(R.id.img_animation_show);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		//代码实现方式 
		AnimationSet animationSet=new AnimationSet(false);
		
		RotateAnimation rotateAnimation=new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setFillBefore(false);
		rotateAnimation.setRepeatCount(2);
		
		AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f, 0f);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);
		alphaAnimation.setFillBefore(false);
		alphaAnimation.setRepeatCount(2);
		
		ScaleAnimation scaleAnimation=new ScaleAnimation(1f, 0f, 1f, 0f,Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);
		scaleAnimation.setFillBefore(false);
		scaleAnimation.setRepeatCount(2);
		
		TranslateAnimation translateAnimation=new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT,0f, Animation.RELATIVE_TO_PARENT, 0.5f);
		translateAnimation.setDuration(1000);
		translateAnimation.setFillAfter(true);
		translateAnimation.setFillBefore(false);
		translateAnimation.setRepeatCount(2);
		
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(translateAnimation);
		imgShow.setAnimation(animationSet);
	}
}
