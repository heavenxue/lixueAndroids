package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.easyandroid.app.BaseListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.adapter.DataAdapter;

/**
 * 一些动画的实例
 * 
 * @author Administrator
 *
 */
public class myAnimationActivity extends BaseListActivity{
	private Button toAnimationButton;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_myanimation);
		toAnimationButton=(Button) findViewById(R.id.button_todoAnimation);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		toAnimationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getListView().setAnimation(new setAnimationView());
//				toastL(v.getTag().toString());
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		List<Integer> listdata=new ArrayList<Integer>();
		listdata.add(1);
		listdata.add(2);
		listdata.add(3);
		listdata.add(4);
		listdata.add(5);
		listdata.add(6);
		listdata.add(7);
		listdata.add(8);
		listdata.add(9);
		getListView().setAdapter(new DataAdapter(getBaseContext(), listdata));
	}
	
	/**
	 * 开始执行动画
	 * @author lixue
	 */
	public class setAnimationView extends Animation{

		@Override
		public void initialize(int width, int height, int parentWidth,int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			setDuration(2500);
			setFillAfter(false);
			setFillEnabled(true);
			scaleCurrentDuration(0);
			scaleCurrentDuration(1);
		}
		
		@Override
		protected void applyTransformation(float interpolatedTime,Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			
		}
	}
}
