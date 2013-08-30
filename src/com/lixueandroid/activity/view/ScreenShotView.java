package com.lixueandroid.activity.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import me.xiaopan.easyandroid.util.SDCardUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.lixueandroid.activity.myinterface.OnScreenShotListener;

public class ScreenShotView extends View {
	private Context mContext = null;
	private int x = 0;// 左
	private int y = 0;// 顶
	private int m = 0;// 右
	private int n = 0;// 底
	private int xx = 0;
	private int yy = 0;
	private int mm = 0;
	private int nn = 0;

	private boolean isDrawing = true;// 是否正在绘制过程中
	private boolean isInZone = false;// 是否在绘制区内
	private int count = 0;
	private long firstClick = 0l;// 第一次点击
	private long secondClick = 0l;// 第二次点击
	private Bitmap source = null;// 所截的图片
	
	private OnScreenShotListener listener;

	public ScreenShotView(Context context) {
		super(context);
	}

	public ScreenShotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScreenShotView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScreenShotView(Activity context, OnScreenShotListener listener) {
		super(context);
		mContext = context;
		this.listener=listener;
	}

	/**
	 * 设置截屏时的图片资源
	 * 
	 * @param source
	 */
	public void setSource(Bitmap source) {
		this.source = source;
	}

	@SuppressLint("DrawAllocation")
	// 检测器用户检测在绘制界面/布局界面中创建新对象的情况
	@Override
	protected void onDraw(Canvas canvas) {
		// 在画布（canvas）上开始画
		Paint mPaint = new Paint();
		if (isDrawing) {
			mPaint.setColor(Color.TRANSPARENT);
		} else {
			mPaint.setColor(Color.GREEN);
			mPaint.setAlpha(150);
			canvas.drawRect(new Rect(x, y, m, n), mPaint);
			Log.i("show", "onDraw");
		}
		super.onDraw(canvas);
	}

	@SuppressLint("SdCardPath")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isInZone = isInZone((int) event.getX(), (int) event.getY());
			Log.i("show", "isInZone:" + isInZone);
			if (isInZone) {
				xx = (int) event.getX();
				yy = (int) event.getY();
			} else {
				x = (int) event.getX();
				y = (int) event.getY();
			}
			if (isInZone) {
				count++;
				if (count == 1) {
					firstClick = System.currentTimeMillis();// 第一次按下，
															// 双击时才表明是截图
				} else if (count == 2) {
					secondClick = System.currentTimeMillis();
					if (secondClick - firstClick < 1000) {
						Log.e("show", "take a screen shot");
						Bitmap cutBitmap = getCutBitmap();
						try {
							FileOutputStream fout = new FileOutputStream((SDCardUtils.isAvailable()? mContext.getExternalFilesDir(null):mContext.getFilesDir()).getAbsolutePath()+File.separator + System.currentTimeMillis() + ".png");
							// 将压缩版本的位图写入到指定的OutputStream。
							if(cutBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout)){
								listener.onComplete(cutBitmap);
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					count = 0;
					firstClick = 0l;
					secondClick = 0l;
				}
			}
			Log.i("show", "MotionEvent.ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			isDrawing = false;
			if (isInZone) {
				Log.i("show", "@@@@@@@@@@:" + x + ";" + y + ";" + m + ";" + n);
				mm = (int) event.getX();
				nn = (int) event.getY();
				Log.i("show", "mm-xx:" + (mm - xx) + ";" + "nn-yy:" + (nn - yy));
				//更新坐标
				x += mm - xx;
				y += nn - yy;
				m += mm - xx;
				n += nn - yy;
				if (x < 0) {
					x = 0;
					m -= mm - xx;
				}
				if (x > getWidth()) {
					x = getWidth();
					m -= mm - xx;
				}
				if (y < 0) {
					y = 0;
					n -= nn - yy;
				}
				if (y > getHeight()) {
					y = getHeight();
					n -= nn - yy;
				}
				if (m < 0) {
					m = 0;
					x -= mm - xx;
				}
				if (m > getWidth()) {
					m = getWidth();
					x -= mm - xx;
				}
				if (n < 0) {
					n = 0;
					y -= nn - yy;
				}
				if (n > getHeight()) {
					n = getHeight();
					y -= nn - yy;
				}
				Log.i("show", "##########:" + x + ";" + y + ";" + m + ";" + n);
				xx = mm;
				yy = nn;
			} else {
				m = (int) event.getX();
				n = (int) event.getY();
			}
			postInvalidate();//刷新View页面（此控件刷新）
			Log.i("show", "MotionEvent.ACTION_MOVE");
			break;
		case MotionEvent.ACTION_UP:
			Log.i("show", "MotionEvent.ACTION_UP");
			if (System.currentTimeMillis() - firstClick > 500) {
				count = 0;
			}
			break;
		default:
			break;
		}
		return true;
	}

	public void refresh(int x, int y) {
		this.x = x;
		this.y = y;
		postInvalidate();
	}

	public void refresh(int m, int n, boolean isDrawing) {
		this.m = m;
		this.n = n;
		this.isDrawing = isDrawing;
		postInvalidate();
	}

	/**
	 * 是否在绘制区中
	 * 
	 * @param newX
	 * @param newY
	 * @return
	 */
	private boolean isInZone(int newX, int newY) {
		int x1, x2, y1, y2;
		if (x < m) {
			x1 = x;
			x2 = m;
		} else {
			x1 = m;
			x2 = x;
		}
		if (y < n) {
			y1 = y;
			y2 = n;
		} else {
			y1 = n;
			y2 = y;
		}
		if ((newX > x1 && newX < x2) && (newY > y1 && newY < y2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到截图图片
	 * 
	 * @return
	 */
	private Bitmap getCutBitmap() {
		Rect frame = new Rect();
		((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;// 获取状态栏高度
		View view = ((Activity) mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		String sss = "top = " + view.getTop() + " bottom = " + view.getBottom();
		Log.i("show", statusBarHeight + ";" + sss);
		Bitmap cutBitmap = Bitmap.createBitmap(source, ((x < m) ? x : m), ((y < n) ? y : n) + view.getTop(), Math.abs(x - m), Math.abs(y - n), null, false);
		return cutBitmap;
	}
}
