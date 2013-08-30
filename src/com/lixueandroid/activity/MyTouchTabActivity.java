package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.easyandroid.app.BaseFragmentActivity;
import me.xiaopan.easyandroid.widget.MyFragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.fragment.MyTabFragment;
import com.lixueandroid.activity.view.TouchTabScrollView;

/**
 *实现tab与viewpager的结合
 * @author Administrator
 *
 */
public class MyTouchTabActivity extends BaseFragmentActivity {

	private TouchTabScrollView mytab;
	private ViewPager myviewPager;
	private RadioGroup rg_nav_content;

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.acitivity_mytouchtab);
		mytab = (TouchTabScrollView) findViewById(R.id.myscrollview);
		//得到viewpager
		myviewPager = mytab.getViewPager();
		//得到tab
		rg_nav_content=mytab.getTabBody();
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		myviewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if (rg_nav_content != null && rg_nav_content.getChildCount() > arg0) {
					((RadioButton) rg_nav_content.getChildAt(arg0)).performClick();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		// 初始化ViewPager显示内容
		List<Fragment> fragments = new ArrayList<Fragment>(mytab.getTabTitleLength());
		fragments.add(new MyTabFragment("0"));
		fragments.add(new MyTabFragment("1"));
		fragments.add(new MyTabFragment("2"));
		fragments.add(new MyTabFragment("3"));
		fragments.add(new MyTabFragment("4"));
		myviewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
	}

}
