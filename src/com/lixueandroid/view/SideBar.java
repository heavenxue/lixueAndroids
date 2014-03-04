package com.lixueandroid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View{
	private String[] letters={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
	private int choose=-1;
	private Paint mPaint=new Paint();  
	private TextView textView;//用来存储选中的文字
	private onTouchingLetterChangedListener onChangedListener;
	
	public SideBar(Context context) {
		super(context);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		int width=getWidth();//当前宽度（控件的）
		int height=getHeight();//当前高度（控件的）
		int barHeight=height/letters.length;//每个字母的高度
		
		for(int i=0;i<letters.length;i++){
			mPaint.setColor(Color.rgb(33, 65, 98));  
			mPaint.setTypeface(Typeface.DEFAULT_BOLD);  
			mPaint.setAntiAlias(true);  
			mPaint.setTextSize(20);  
			// 选中的状态  
			if (i == choose) {  
				mPaint.setColor(Color.parseColor("#3399ff"));  
				mPaint.setFakeBoldText(true);  
			}  
			// x坐标等于中间-字符串宽度的一半.  
			float xPos = width / 2 - mPaint.measureText(letters[i]) / 2;  
			float yPos = barHeight * i + barHeight;  
			//画出每个字母来
			canvas.drawText(letters[i], xPos, yPos, mPaint);  
			mPaint.reset();// 重置画笔  
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final  onTouchingLetterChangedListener listener=onChangedListener;
		float currentPos=event.getY();
		int c= (int) (currentPos/getHeight()*letters.length);
		
		switch (event.getAction()) {
		//未点击
		case MotionEvent.ACTION_UP:
			choose=-1;
			
			if(textView!=null){
				textView.setVisibility(View.INVISIBLE);
			}
			invalidate();
			break;

		default:
			if(c!=choose){
				if(c>0&&c<letters.length){
					if(listener!=null){
						listener.onTouchingLetterChanged(letters[c]);
					}
					if(textView!=null){
						textView.setText(letters[c]);
						textView.setVisibility(View.VISIBLE);
					}
					choose=c;
					invalidate();
				}
			}
			break;
		}
		return super.dispatchTouchEvent(event);
	}
	public interface onTouchingLetterChangedListener{
		public void onTouchingLetterChanged(String letter);
	}
	
	/**
	 * 设置显示点击的文字 为SideBar设置显示字母的TextView 
	 * @param textView
	 */
	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	/**
	 * 设置触摸文字的事件
	 * @param onChangedListener
	 */
	public void setOnChangedListener(onTouchingLetterChangedListener onChangedListener) {
		this.onChangedListener = onChangedListener;
	}
}
