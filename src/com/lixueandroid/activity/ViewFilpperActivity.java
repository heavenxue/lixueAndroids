package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.adapter.imgAdapter;
import com.lixueandroid.mydata.ImageUrls;
import com.lixueandroid.view.OnDownFlingListener;
import com.lixueandroid.view.OnUpFlingListener;
import com.lixueandroid.view.slidingEventsView;

/**
 * 屏幕切换动画效果
 * @author lixue
 *
 */
public class ViewFilpperActivity extends MyBaseActivity {
//	private Button myButton1;
//	private Button myButton2;
	private ImageView myimg;
	private String[] imageUrls;
	private ListView mylist;
	/**
	 * 手势识别
	 */
	private GestureDetector detector; 
	
	private slidingEventsView myview;
	private GestureDetector myDetector;

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_viewflipper);
		myimg=(ImageView) findViewById(R.id.myimg);
		myview=(slidingEventsView) findViewById(R.id.myview);
		mylist=(ListView) findViewById(R.id.mylist);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		myimg.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return myDetector.onTouchEvent(event);
			}
		});
//			myimg.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					toastS("点了我");
//				}
//			});
		myview.setOnUpFlingListener(new OnUpFlingListener() {
			
			@Override
			public void onUpFling() {
				toastL("向上滑动了哈哈");
			}
		});
		myview.setOnDownFlingListener(new OnDownFlingListener() {
			
			@Override
			public void onDownFling() {
				toastS("向下滑动干嘛》？");
			}
		});
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		myDetector=myview.getDetector();
		imageUrls=ImageUrls.getImageUrls();
		mylist.setAdapter(new imgAdapter(getBaseContext(),imageUrls,getMyApplication().getOptions(),getMyApplication().getImageLoader()));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return myDetector.onTouchEvent(event);
	} 

}