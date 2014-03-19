package com.lixueandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.lixueandroid.ThemeConfig;

/**
 * 夜间模式与日间模式的切换页面
 * @author lixue
 *
 */
public class NightModeActivity extends Activity{
	private Button nightModeButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(ThemeConfig.getMode());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nightmode);
		nightModeButton=(Button) findViewById(R.id.button_switch_nightmode);
		nightModeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ThemeConfig.changeMode();
				ThemeConfig.reload(NightModeActivity.this);
			}
		});
	}
}
