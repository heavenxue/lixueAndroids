package com.lixueandroid.activity.view;

import me.xiaopan.easyjava.util.StringUtils;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lixue.lixueandroid.R;

/**
 * 一个融合了横向tab和viewPager的控件
 * @author lixue
 *
 */
public class TouchTabScrollView extends LinearLayout{
	private LinearLayout r1_tab;
	private HorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;//tab身体
	private ImageView iv_nav_indicator;//tab滑块
	private int currentIndicatorLeft = 0;//tab小滑块左侧距离
	private int indicatorWidth;//tab小滑块的宽度
	public static String[] tabTitle; // 标题
	private ViewPager mViewPager;//ViewPager
	
	//此构造函数是在布局文档中使用的时候，默认的就用了此构造函数
	public TouchTabScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}
	//此构造函数是在new出这个实例时用到
	public TouchTabScrollView(Context context) {
		super(context);
		Init();
	}
	//初始化各个控件
	public void Init(){
		tabTitle = StringUtils.split((String) getContentDescription(), ' ');  
		r1_tab=(LinearLayout)inflate(getContext(), R.layout.touchtab, null);
		mHsv=(HorizontalScrollView) r1_tab.findViewById(R.id.mHsv);
		rg_nav_content=(RadioGroup) r1_tab.findViewById(R.id.rg_nav_content);
		iv_nav_indicator=(ImageView) r1_tab.findViewById(R.id.iv_nav_indicator);
		mViewPager=(ViewPager) r1_tab.findViewById(R.id.mViewPager);
		setOnCheckChanged();
		//初始化视图
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		if(tabTitle.length<4){
			indicatorWidth = dm.widthPixels / (tabTitle.length);
		}else{
			indicatorWidth = dm.widthPixels / 4;
		}
		android.view.ViewGroup.LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);
		
		//填充标题
		rg_nav_content.removeAllViews();
		for (int i = 0; i < tabTitle.length; i++) {
			RadioButton rb = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.touchtab_nav_radiogroup_item, null);
			rb.setId(i);
			rb.setText(tabTitle[i]);
			if(tabTitle.length<4){
				indicatorWidth=dm.widthPixels/tabTitle.length;
			}
			rb.setLayoutParams(new LayoutParams(indicatorWidth, LayoutParams.MATCH_PARENT));
			rg_nav_content.addView(rb);
		}
		addView(r1_tab);
		rg_nav_content.check(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
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
	//tab是由radioGroup组成和ImageView小滑块组成的，外皮由SynchorizontalScrollView包裹，radioGroup的事件见下面
	//当radioGroup滑动时(选中时)，ImageView小滑块也要跟着滑动
	public void setOnCheckChanged(){
		rg_nav_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (rg_nav_content.getChildAt(checkedId) != null) {
					//移动滑块并记录当前位置
					TranslateAnimation animation = new TranslateAnimation(currentIndicatorLeft, ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
					animation.setInterpolator(new LinearInterpolator());
					animation.setDuration(100);
					animation.setFillAfter(true);
					iv_nav_indicator.startAnimation(animation);
					currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();
					mViewPager.setCurrentItem(checkedId); // ViewPager 跟随一起 切换
					if(tabTitle.length<4){
						mHsv.smoothScrollTo((checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(1)).getLeft(), 0);
					}else{
						mHsv.smoothScrollTo((checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(), 0);
					}
				}
			}
		});
	}
	//得到viewPager对象
	public ViewPager getViewPager(){
		return mViewPager;
	}
	//得到tab的长度
	public int getTabTitleLength(){
		return tabTitle.length;
	}	
	//得到tab的body
	public RadioGroup getTabBody(){
		return rg_nav_content;
	}
}
