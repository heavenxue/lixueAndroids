package com.lixueandroid.activity.view;
import com.lixue.lixueandroid.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class ScaleImageView extends ImageView{
	private Activity mActivity;

	private int screen_W, screen_H;// 屏幕宽，屏幕高

	private int bitmap_W, bitmap_H;//图像宽，图像高

	private int MAX_W, MAX_H, MIN_W, MIN_H;//最大宽，最大高，最小宽，最小高

	private int current_Top, current_Right, current_Bottom, current_Left;//当前图像距顶部的距离，当前图像距右方屏幕的距离，当前图像距底部的距离，当前图像距左部的距离

	private int start_Top = -1, start_Right = -1, start_Bottom = -1,start_Left = -1;// 开始的上，右，下，左距屏幕的距离

	private int start_x, start_y, current_x, current_y;//开始的x、y坐标值；当前的x、y坐标值

	private float beforeLenght, afterLenght;//缩放前的长度，缩放后的长度

	private float scale_temp;//暂时缩放比例
	/**
	 * 拖放模式
	 * 
	 * @author lixue
	 *
	 */
	private enum MODE {
		NONE,//无
		DRAG,//拖拽
		ZOOM//缩放
	};
	
	private MODE mode = MODE.NONE;//默认拖放模式为无

	private boolean isControl_V = false;//是否竖直方向

	private boolean isControl_H = false;//是否水平方向

	private ScaleAnimation scaleAnimation;//缩放动画

	private boolean isScaleAnim = false;//是否是缩放动画

	private MyAsyncTask myAsyncTask;//异步线程
	
	public ScaleImageView(Context context) {
		super(context);
	}
	
	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * 设置当前界面
	 * @param mActivity
	 */
	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}
	
	/*
	 * 设置图像显示的图片
	 *  (non-Javadoc)
	 * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
	 */
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		bitmap_W = bm.getWidth();
		bitmap_H = bm.getHeight();
		
		//放大的最大尺寸是原图像的3倍
		MAX_W = bitmap_W * 3;
		MAX_H = bitmap_H * 3;
		
		//缩小的最小尺寸是原图像的一半
		MIN_W = bitmap_W / 2;
		MIN_H = bitmap_H / 2;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (start_Top == -1) {
			start_Top = top;
			start_Left = left;
			start_Bottom = bottom;
			start_Right = right;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Point[] points={new Point(100,300),new Point(200,300),new Point(100,600),new Point(200,600)};
		Path path=new Path();
		path.moveTo(100, 100);
		for(int s =0;s<points.length;s++){
			path.lineTo(points[s].x, points[s].y);
		}
		Paint paints=new Paint();
		paints.setColor(Color.RED);
		canvas.drawPath(path, paints);
		super.onDraw(canvas);
	}
	/*
	 * 单手指操作：ACTION_DOWN---ACTION_MOVE----ACTION_UP
		多手指操作：ACTION_DOWN---ACTION_POINTER_DOWN---ACTION_MOVE--ACTION_POINTER_UP---ACTION_UP.
	 *  (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		//第一个手指touch
		case MotionEvent.ACTION_DOWN:
			onTouchDown(event);
			break;
		//第二个手指touch
		case MotionEvent.ACTION_POINTER_DOWN:
			onPointerDown(event);
			break;
		//第一个手指移动
		case MotionEvent.ACTION_MOVE:
			onTouchMove(event);
			break;
		//第一个手指抬起	
		case MotionEvent.ACTION_UP:
			mode = MODE.NONE;
			break;

		//第二个手指抬起
		case MotionEvent.ACTION_POINTER_UP:
			mode = MODE.NONE;
			/** 如果是缩放，便显示缩放动画 **/
			if (isScaleAnim) {
				doScaleAnim();
			}
			break;
		}
		return true;
	}
	
	//当手指按下
	public void onTouchDown(MotionEvent event) {
		mode = MODE.DRAG;
		
		current_x = (int) event.getRawX();
		current_y = (int) event.getRawY();

		start_x = (int) event.getX();
		start_y = current_y - this.getTop();

	}
	
	//多个手指按下的时候
	public void onPointerDown(MotionEvent event) {
		if (event.getPointerCount() == 2) {
			mode = MODE.ZOOM;
			beforeLenght = getDistance(event);//计算第二个手指touch时，与第一个手指之间的距离
		}
	}
	//当一个手指滑动时
	public void onTouchMove(MotionEvent event){
		int left = 0, top = 0, right = 0, bottom = 0;
		/**此时为拖动 **/
		if (mode == MODE.DRAG) {
			/** 在这里要进行判断处理，防止在drag时候越界 **/  
	        /** 获取相应的l，t,r ,b **/  
			left = current_x - start_x;
			right = current_x + this.getWidth() - start_x;
			top = current_y - start_y;
			bottom = current_y - start_y + this.getHeight();

			/** 如果是水平屏幕 **/
			if (isControl_H) {
				if (left >= 0) {
					left = 0;
					right = this.getWidth();
				}
				if (right <= screen_W) {
					left = screen_W - this.getWidth();
					right = screen_W;
				}
			} else {
				left = this.getLeft();
				right = this.getRight();
			}
			/**如果屏幕是垂直的**/
			if (isControl_V) {
				if (top >= 0) {
					top = 0;
					bottom = this.getHeight();
				}

				if (bottom <= screen_H) {
					top = screen_H - this.getHeight();
					bottom = screen_H;
				}
			} else {
				top = this.getTop();
				bottom = this.getBottom();
			}
			if (isControl_H || isControl_V)
				this.setPosition(left, top, right, bottom);

			current_x = (int) event.getRawX();
			current_y = (int) event.getRawY();

		}
		/**处理缩放 **/
		else if (mode == MODE.ZOOM) {

			afterLenght = getDistance(event);//获取两点间的距离

			float gapLenght = afterLenght - beforeLenght;//变化的长度

			if (Math.abs(gapLenght) > 5f) {
				scale_temp = afterLenght / beforeLenght;//缩放的比例

				this.setScale(scale_temp);

				beforeLenght = afterLenght;
			}
		}
	}
	
	/**
	 * 得到当前手指缩放滑动的距离
	 * @param event
	 * @return
	 */
	public float getDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return FloatMath.sqrt(x * x + y * y);
	}
	
	/** 设置屏幕宽度 **/
	public void setScreen_W(int screen_W) {
		this.screen_W = screen_W;
	}

	/** 设置屏幕高度 **/
	public void setScreen_H(int screen_H) {
		this.screen_H = screen_H;
	}
	/**设置位置 **/
	private void setPosition(int left, int top, int right, int bottom) {
		this.layout(left, top, right, bottom);
	}

	/**设置缩放 **/
	void setScale(float scale) {
		int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// 获取缩放水平距离
		int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;//获取缩放垂直距离

		// 放大 
		if (scale > 1 && this.getWidth() <= MAX_W) {
			current_Left = this.getLeft() - disX;
			current_Top = this.getTop() - disY;
			current_Right = this.getRight() + disX;
			current_Bottom = this.getBottom() + disY;

			this.setFrame(current_Left, current_Top, current_Right,current_Bottom);
			/***
			 * 此时因为考虑到对称，所以只做一遍判断就可以了。 
			 */
			if (current_Top <= 0 && current_Bottom >= screen_H) {
				isControl_V = true;// 开户垂直监控
			} else {
				isControl_V = false;
			}
			if (current_Left <= 0 && current_Right >= screen_W) {
				isControl_H = true;//开户水平监控
			} else {
				isControl_H = false;
			}

		}
		// 缩小
		else if (scale < 1 && this.getWidth() >= MIN_W) {
			current_Left = this.getLeft() + disX;
			current_Top = this.getTop() + disY;
			current_Right = this.getRight() - disX;
			current_Bottom = this.getBottom() - disY;
			/***
			 * 在这里要进行缩放处理
			 */
			// 上边越界
			if (isControl_V && current_Top > 0) {
				current_Top = 0;
				current_Bottom = this.getBottom() - 2 * disY;
				if (current_Bottom < screen_H) {
					current_Bottom = screen_H;
					isControl_V = false;//关闭垂直监控
				}
			}
			// 下边越界
			if (isControl_V && current_Bottom < screen_H) {
				current_Bottom = screen_H;
				current_Top = this.getTop() + 2 * disY;
				if (current_Top > 0) {
					current_Top = 0;
					isControl_V = false;//关闭垂直监控
				}
			}

			//左边越界
			if (isControl_H && current_Left >= 0) {
				current_Left = 0;
				current_Right = this.getRight() - 2 * disX;
				if (current_Right <= screen_W) {
					current_Right = screen_W;
					isControl_H = false;//关闭水平监控
				}
			}
			//右边越界
			if (isControl_H && current_Right <= screen_W) {
				current_Right = screen_W;
				current_Left = this.getLeft() + 2 * disX;
				if (current_Left >= 0) {
					current_Left = 0;
					isControl_H = false;//关闭水平监控
				}
			}

			if (isControl_H || isControl_V) {
				this.setFrame(current_Left, current_Top, current_Right,current_Bottom);
			} else {
				this.setFrame(current_Left, current_Top, current_Right,current_Bottom);
				isScaleAnim = true;// 开户缩放动画
			}

		}

	}

	/***
	 * 执行动画
	 */
	public void doScaleAnim() {
		myAsyncTask = new MyAsyncTask(screen_W, this.getWidth(),this.getHeight());
		myAsyncTask.setLTRB(this.getLeft(), this.getTop(), this.getRight(),this.getBottom());
		myAsyncTask.execute();
		isScaleAnim = false;//关闭动画
	}
	
	/**
	 * 回缩动画執行 
	 * @author 
	 *
	 */
	class MyAsyncTask extends AsyncTask<Void, Integer, Void>{
		 private int screen_W, current_Width, current_Height;  
		  
	        private int left, top, right, bottom;  
	  
	        private float scale_WH;// 宽高的比例  
	  
	        /** 当前的位置属性 **/  
	        public void setLTRB(int left, int top, int right, int bottom) {  
	            this.left = left;  
	            this.top = top;  
	            this.right = right;  
	            this.bottom = bottom;  
	        }  
	  
	        private float STEP = 5f;// 步伐  
	  
	        private float step_H, step_V;// 水平步伐，垂直步伐  
	  
	        public MyAsyncTask(int screen_W, int current_Width, int current_Height) {  
	            super();  
	            this.screen_W = screen_W;  
	            this.current_Width = current_Width;  
	            this.current_Height = current_Height;  
	            scale_WH = (float) current_Height / current_Width;  
	            step_H = STEP;  
	            step_V = scale_WH * STEP;  
	        }  
	  
	        @Override  
	        protected Void doInBackground(Void... params) {  
	  
	            while (current_Width <= screen_W) {  
	  
	                left -= step_H;  
	                top -= step_V;  
	                right += step_H;  
	                bottom += step_V;  
	  
	                current_Width += 2 * step_H;  
	  
	                left = Math.max(left, start_Left);  
	                top = Math.max(top, start_Top);  
	                right = Math.min(right, start_Right);  
	                bottom = Math.min(bottom, start_Bottom);  
	  
	                onProgressUpdate(new Integer[] { left, top, right, bottom });  
	                try {  
	                    Thread.sleep(10);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	            return null;  
	        }  
	        @Override  
	        protected void onProgressUpdate(final Integer... values) {  
	            super.onProgressUpdate(values);  
	            mActivity.runOnUiThread(new Runnable() {  
	                @Override  
	                public void run() {  
	                    setFrame(values[0], values[1], values[2], values[3]);  
	                }  
	            });  
	        }  
	}	
}
