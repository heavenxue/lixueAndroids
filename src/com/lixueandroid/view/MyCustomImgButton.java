package com.lixueandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixue.lixueandroid.R;

public class MyCustomImgButton extends LinearLayout{
	private ImageView imageView;  
    private TextView  textView;  
	public MyCustomImgButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		//取得xml里定义的view
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        inflater.inflate(R.layout.imagebtn, this);  
        imageView=(ImageView) findViewById(R.id.imageView1);  
        textView=(TextView)findViewById(R.id.textView1);  
	}

	public MyCustomImgButton(Context context) {
		super(context);
	}
	/**   
     * 设置图片资源   
     */    
    public void setImageResource(int resId) {    
        imageView.setImageResource(resId);    
    }    
    
    /**   
     * 设置显示的文字   
     */    
    public void setTextViewText(String text) {    
        textView.setText(text);    
    }    
}
