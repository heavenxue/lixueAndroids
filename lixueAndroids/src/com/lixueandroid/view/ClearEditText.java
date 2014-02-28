package com.lixueandroid.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 *带清除按钮的EditText
 */
public class ClearEditText extends EditText{
	/**
	 * 右侧清除按钮
	 */
	private Drawable dRight; 
	/**
	 * 左侧图标按钮
	 */
	private Drawable dLeft;
	
	public ClearEditText(Context context) {
		super(context);
		Init();
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Init();
	}
	
	public void Init(){
		setClearIcon();
		ClearEditText.this.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setClearIcon();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*
		 * 	因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 
		  * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和 
		  * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
		 */ 
		 if ((this.dRight != null) && (event.getAction() == MotionEvent.ACTION_UP)) {
             if(event.getX() >= (getWidth() - getTotalPaddingRight())&& (event.getX() <= ((getWidth() - getPaddingRight())))){
            	 setText("");
            	 event.setAction(MotionEvent.ACTION_CANCEL);
             }
		 }
		 /*
		  * 如果按下了左侧的图标按钮，我们也让其清除
		  * 
		  * */
		 if(this.dLeft!=null&&(event.getAction()==MotionEvent.ACTION_UP)){
			 if(event.getX()>=(getPaddingLeft())&&event.getX()<=(getTotalPaddingLeft())){
				 setText("");
				 event.setAction(MotionEvent.ACTION_CANCEL);
			 }
		 }		 
		return super.onTouchEvent(event);
	}

	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,Drawable right, Drawable bottom) {
		if(right!=null){
			dRight=right;
		}
		if(left!=null){
			dLeft=left;
		}
		super.setCompoundDrawables(left, top, right, bottom);
	}
	 /** 
     * 设置晃动动画 
     */  
    public void setShakeAnimation(){  
        this.setAnimation(shakeAnimation(5));  
    }  
      
      
    /** 
     * 晃动动画 
     * @param counts 1秒钟晃动多少下 
     * @return 
     */  
    public static Animation shakeAnimation(int counts){  
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);  
        translateAnimation.setInterpolator(new CycleInterpolator(counts));  
        translateAnimation.setDuration(1000);  
        return translateAnimation;  
    }  
    /**
     * 设置右侧的删除图标
     */
    public void setClearIcon(){
    	if(ClearEditText.this.getEditableText().toString().trim().length()==0){
			ClearEditText.this.setCompoundDrawables(dLeft, null, null, null);
		}else{
			if(dRight!=null){
				ClearEditText.this.setCompoundDrawables(dLeft, null, dRight, null);
			}
		}
    }
    @Override
	protected void finalize() throws Throwable {
    	dRight=null;
		super.finalize();
	}
}
