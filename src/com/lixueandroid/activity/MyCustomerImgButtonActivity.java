package com.lixueandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.view.MyCustomImgButton;
import com.lixueandroid.util.Utils;

/**
 * 自定义图片按钮
 * @author Administrator
 *
 */
public class MyCustomerImgButtonActivity extends Activity {
	private MyCustomImgButton mycustomimgbutton1;
	private MyCustomImgButton mycustomimgbutton2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycustormimgbutton);
		mycustomimgbutton1 = (MyCustomImgButton) findViewById(R.id.mycustomer_imgbutton1);
		mycustomimgbutton2 = (MyCustomImgButton) findViewById(R.id.mycustomer_imgbutton2);
		mycustomimgbutton1.setTextViewText("确定");
		mycustomimgbutton2.setTextViewText("取消");
		mycustomimgbutton1.setImageResource(R.drawable.imgbutton);
		mycustomimgbutton1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "点击的正确按钮", 1).show();
				Utils.deleteAllFiles("E2.png");
			}
		});
		mycustomimgbutton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "点击的错误按钮", 1).show();
			}
		});
	}
}
