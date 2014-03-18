package com.lixueandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.view.Countdown;

public class CountDownActivity extends MyBaseActivity{
	private Button cButton;
	private Countdown countDown;
	
	private TextView showTextView;
	private Button runButton;
	int timer =0;
	protected int splashTime = 3000;
	String[] name = {"A","N","D","R","O","I","D"};
	
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_countdown);
		cButton=(Button) findViewById(R.id.button_countdown_countdown);
		showTextView=(TextView) findViewById(R.id.text_show);
		runButton=(Button) findViewById(R.id.button_run);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		runButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//创建一个用于显现前三种后台线程和UI线程交互的线程  
                new TestThread(CountDownActivity.this).start();  
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		countDown=new Countdown(cButton,"倒计时按钮","倒计时%s秒");
		countDown.start();
	}
	
	/**
	 * 我们都知道在android系统中我们是不能在UI线程外更新界面的，
	 * 如果你要想在一个UI线程外更新UI的话，你得用那Handler啊，
	 * 或是异步线程类等一些方式，下面就会大家讲解一个很简单，
	 * 却很实用的方法让UI在一个“用户线程”中更新，
	 * 直接用acitivty类的runOnUiThread(runnable run)方法,
	 * 
	 * **/
	//后台线程类  
    class TestThread extends Thread  
    {  
        Activity activity;  
        public TestThread(Activity activity) {  
              
            this.activity = activity;  
        }  
        
        @Override  
        public void run() {  
            try
            {
                    for (timer = 0; timer < 7; timer++)
                    {
                            int waited = 0;
                            while(waited < splashTime)
                            {
                                    Thread.sleep(100);
                                    runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                    try {
                                                    	showTextView.setText(name[timer]);
                                                    }
                                                    catch(Exception e)
                                                    {
                                                            e.printStackTrace();
                                                    }
                                            }
                                    });
                                    waited += 100;
                            }
                    }
            }catch (InterruptedException e) {
            }
            super.run();  
        }  
    }
}
