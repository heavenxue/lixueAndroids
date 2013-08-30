package com.lixueandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ViewFlipper;

import com.lixue.lixueandroid.R;

/**
 * 屏幕切换动画效果
 * @author Administrator
 *
 */
public class ViewFilpperActivity extends Activity { 
    /** Called when the activity is first created. */ 
     
    private ViewFlipper flipper; 
    private GestureDetector detector; 
 
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_viewflipper); 
        flipper = (ViewFlipper) findViewById(R.id.flipper); 
        flipper.setLongClickable(true); 
        flipper.setOnTouchListener(new OnTouchListener() { 
            @Override 
            public boolean onTouch(View v, MotionEvent event) { 
                // TODO Auto-generated method stub 
                detector.onTouchEvent(event); 
                return false; 
            } 
        }); 
         detector = new GestureDetector(this, new OnGestureListener(){ 
                @Override 
                public boolean onDown(MotionEvent e) { 
                    //用户轻触屏幕。（单击）  
                    return true; 
                } 
                //用户按下屏幕，快速移动后松开（就是在屏幕上滑动）  
                //e1:第一个ACTION_DOWN事件（手指按下的那一点）  
                //e2:最后一个ACTION_MOVE事件 （手指松开的那一点）  
                //velocityX:手指在x轴移动的速度 单位：像素/秒  
                //velocityY:手指在y轴移动的速度 单位：像素/秒  
                @Override 
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { 
                    int x = (int) (e2.getX() - e1.getX()); 
                    if(x>0){ 
                        flipper.setInAnimation(ViewFilpperActivity.this,R.anim.in_left_right ); 
                              flipper.setOutAnimation(ViewFilpperActivity.this,R.anim.out_left_right ); 
                              flipper.showPrevious();                      
                    }else{ 
                        flipper.setInAnimation(ViewFilpperActivity.this,R.anim.in_right_left ); 
                              flipper.setOutAnimation(ViewFilpperActivity.this,R.anim.out_right_left ); 
                              flipper.showNext();                  
                    } 
                    return true; 
                } 
                @Override 
                public void onLongPress(MotionEvent e) { 
                    // TODO Auto-generated method stub 
                    //用户长按屏幕  
 
                } 
                @Override 
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { 
                    // TODO Auto-generated method stub 
                    return false;//用户按下屏幕并拖动  
 
                } 
                @Override 
                public void onShowPress(MotionEvent e) { 
                    // TODO Auto-generated method stub 
                    //用户轻触屏幕，尚末松开或拖动，注意，强调的是没有没有松开或者拖动状态 
                } 
                @Override 
                public boolean onSingleTapUp(MotionEvent e) { 
                    // TODO Auto-generated method stub 
                    return false;//用户轻触屏幕后松开。 
                } 
            }); 
    }  
}