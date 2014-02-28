package com.lixueandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lixue.lixueandroid.R;
import com.lixueandroid.util.Utils;
import com.lixueandroid.view.MyCustomImgButton;

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
		//用反射得到一个类的各字段名称
//		List<Field> fields=ClassUtils.getFields(User.class, false, true, true);
//		for (Field field : fields) {
//			System.out.println("User 的字段有:"+field.getName());
//		}
//		HttpApacheFac.getHttpInstance().setDebugMode(true);
		/**测试http协议类**/
//		HttpApacheFac.getHttpInstance().get(getBaseContext(), "http://www.baidu.com", null, null, new HttpResponseHandler() {
//			
//			@Override
//			public void start() {
//				System.out.println("HTTP协议开始");
//			}
//			
//			@Override
//			public void handleResponse(HttpResponse httpResponse) throws Throwable {
//				if(httpResponse!=null){
//					System.out.println("响应信息："+httpResponse.getEntity().toString());
//				}
//			}
//			
//			@Override
//			public void exception(Throwable t) {
//				System.out.println("异常信息："+t.getMessage());
//			}
//			
//			@Override
//			public void end() {
//				System.out.println("HTTP协议结束");
//			}
//		});
		
	}
}
