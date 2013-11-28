package com.lixueandroid.activity;


import me.xiaopan.easyandroid.app.BaseActivity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.lixueandroid.myinterface.OnScreenShotListener;
import com.lixueandroid.view.ScreenShotView;

/**
 * 截图实例
 * @author Administrator
 *
 */
public class ScreenShotActivity extends BaseActivity{
	private Button btn_screenshot = null;
	private Bitmap screenShotBitmap = null;
	private ScreenShotView screenShot = null;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_screenshot);
		btn_screenshot=(Button) findViewById(R.id.btn_screenshot);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		btn_screenshot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_screenshot:
					takeScreenShot();
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}
	private void takeScreenShot() {
		//decorView是window中的最顶层view，可以从window中获取到decorView，然后decorView有个getWindowVisibleDisplayFrame方法可以获取到程序显示的区域，包括标题栏，但不包括状态栏。
		View view = getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		screenShotBitmap = view.getDrawingCache();
		addScreenShot(this, new OnScreenShotListener() {
			public void onComplete(Bitmap bm) {
				if(bm!=null){
					toastL("截屏完成，请去此应用的SD卡中查看吧！");
				}
			}
		});
	}

	public  void addScreenShot(Activity context,OnScreenShotListener mScreenShotListener) {
            screenShot = new ScreenShotView(context,mScreenShotListener);
            screenShot.setSource(screenShotBitmap);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            context.getWindow().addContentView(screenShot, lp);
    }
}
