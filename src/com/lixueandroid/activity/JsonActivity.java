package com.lixueandroid.activity;

import me.xiaopan.easyandroid.app.BaseActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lixue.lixueandroid.R;

/**
 * 解析Json数据的实例
 * @author Administrator
 *
 */
public class JsonActivity extends BaseActivity {
	private TextView jsonTextView;
	private TextView resolveJsonTextView;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_json);
		jsonTextView=(TextView) findViewById(R.id.text_json);
		resolveJsonTextView=(TextView) findViewById(R.id.text_resolve_json);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
	}
}
