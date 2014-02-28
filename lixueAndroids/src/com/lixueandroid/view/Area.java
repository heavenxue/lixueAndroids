package com.lixueandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface Area {
	/**
	 * 是否点击了矩形区域
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isClickArea(float x, float y);
	
	/**
	 * 是否点击了气泡
	 * @param context
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isClickBubble(Context context, float x, float y);
	
	/**
	 * 绘制矩形区域
	 * @param canvas
	 * @param paint
	 * @param scale
	 */
	public void drawArea(Canvas canvas, Paint paint, float scale);
	
	/**
	 * 绘制气泡
	 * @param context
	 * @param canvas
	 */
	public void drawBubble(Context context, Canvas canvas);
	
	/**
	 * 绘制按下的样式
	 * @param canvas
	 * @param paint
	 */
	public void drawPressed(Context context, Canvas canvas);
	
	/**
	 * 获取气泡图片
	 * @param context
	 * @return
	 */
	public Drawable getBubbleDrawable(Context context);
	
	/**
	 * 获取气泡的位置
	 * @param context
	 * @return
	 */
	public abstract RectF getBubbleRect(Context context);
	
	/**
	 * 是否显示气泡
	 * @return
	 */
	public boolean isShowBubble();
	
	/**
	 * 设置是否显示气泡
	 * @param isShowBubble
	 * @param view
	 */
	public void setShowBubble(boolean isShowBubble, View view);
	
	/**
	 * 设置点击的是否是矩形区域
	 * @param isClickedArea true：是；false：不是，不是的话点击的就是气泡
	 */
	public void setClickedArea(boolean isClickedArea);
	
	/**
	 * 判断点击的是否是矩形区域
	 * @return true：是；false：不是，不是的话点击的就是气泡
	 */
	public boolean isClickedArea();
}
