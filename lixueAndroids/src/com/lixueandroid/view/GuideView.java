package com.lixueandroid.view;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.easyjava.util.StringUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import com.lixueandroid.myinterface.MySimpleGestureListener;
import com.lixueandroid.util.BitmapDecoder;
import com.lixueandroid.util.BitmapUtils;
import com.lixueandroid.util.MathUtils;
import com.lixueandroid.util.ViewUtils;

/**
 * @author Administrator
 *
 */
public class GuideView extends ScaleImageView implements MySimpleGestureListener{
	/**
	 * 显示位置（拥有4个浮点坐标的矩形）
	 */
	private RectF displayRect;
	/**
	 * 偏移位置（拥有4个浮点坐标的矩形）
	 */
	private RectF offsetRect;
	/**
	 * 
	 */
	private int maxSideLength;
	/**
	 * 矩阵
	 */
	private Matrix drawMatrix;
	/**
	 * 当前按下的区域
	 */
	private Area CurrentDownArea;
	/**
	 * 底图图片
	 */
	private BitmapDrawable drawable;
	/**
	 * 气泡区域
	 */
	private List<Area> bubbleAreas;
	/**
	 * 各矩形区域
	 */
	private List<Area> areas;
	/**
	 * 是否初始化完成
	 */
	private boolean initFinsish;
	private Listener listener;
	private TextView textView;
	private Area waitLocationArea;
	
	public GuideView(Context context) {
		super(context);
		Init();
	}

	public GuideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}

	public GuideView(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
		Init();
	}
	private void Init(){
		displayRect=new RectF();
		offsetRect=new RectF();
		maxSideLength=2000;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(drawable!=null){
			if(drawMatrix!=null){
				canvas.concat(drawMatrix);
			}
			//绘制底图
			drawable.draw(canvas);
			if(CurrentDownArea!=null&&!CurrentDownArea.isShowBubble()&&!CurrentDownArea.isClickedArea()){
				//绘制按下状态的区域
				CurrentDownArea.drawPressed(getContext(), canvas);
			}
			//绘制气泡
			offsetRect.set(0, 0, 0,0);
			if(bubbleAreas!=null&&bubbleAreas.size()>0){
				for (Area area : bubbleAreas) {
					if(area.isShowBubble()){
						area.drawBubble(getContext(), canvas);
					}
				}
			}
		}
	}
	/**
	 * 设置初始化开始加载时的状态
	 */
	public void setInitStart(){
		if(drawable!=null){
			if(!drawable.getBitmap().isRecycled()){
				drawable.getBitmap().recycle();
			}
			drawable.setCallback(null);
			unscheduleDrawable(null);
			drawable=null;
		}
		if(drawMatrix!=null){
			drawMatrix.reset();
			drawMatrix=null;
		}
		if(areas!=null){
			areas.clear();
			areas=null;
		}
		initFinsish=false;
	}
	/**
	 * 设置初始化完成时的状态
	 * @param mapBitmapDrawable
	 * @param areas
	 */
	public void setInitFinish(BitmapDrawable mapBitmapDrawable,List<Area> areas){
		if(mapBitmapDrawable!=null&&areas!=null&&areas.size()>0){
			this.areas=areas;
			this.drawable=mapBitmapDrawable;
			drawMatrix=new Matrix();
			invalidate();
			if(listener!=null){
				listener.onInitFinish();
			}
			initFinsish=true;
			if(waitLocationArea != null){
				location(waitLocationArea);
				waitLocationArea = null;
			}
		}
	}
	/**
	 * 设置地图
	 * @param baseMapBitmap 地图图片
	 * @param newAreas  地图上的区域
	 * @param suggestMapWidth 地图的宽，如果该值小于0将以baseMapBitmap.getWidth()代替
	 * @param suggestMapHeight
	 */
	public void setMap(final Bitmap baseMapBitmap, final List<Area> newAreas, final int suggestMapWidth, final int suggestMapHeight){
		setInitStart();
		if(baseMapBitmap!=null&&newAreas!=null&&newAreas.size()>0){
			new AsyncTask<Integer, Integer, BitmapDrawable>() {

				@Override
				protected void onPreExecute() {
					if(listener!=null){
						listener.onInitStart();
					}
				}

				@Override
				protected BitmapDrawable doInBackground(Integer... params) {
					int width=suggestMapWidth>0?suggestMapWidth:baseMapBitmap.getWidth();
					int height=suggestMapHeight>0?suggestMapHeight:baseMapBitmap.getHeight();
					
					/* 限制底图的最大边长并创建底图副本 */
					Bitmap bitmap=null;
					if(baseMapBitmap.getWidth()>maxSideLength){
						bitmap=BitmapUtils.scaleByWidth(baseMapBitmap, maxSideLength);
						baseMapBitmap.recycle();
					}else if(baseMapBitmap.getHeight()>maxSideLength){
						bitmap=BitmapUtils.scaleByHeight(baseMapBitmap, maxSideLength);
						baseMapBitmap.recycle();
					}else{
						bitmap=baseMapBitmap.copy(Config.ARGB_8888, true);
						baseMapBitmap.recycle();
					}
					/* 在底图上绘制区域 */
					Canvas canvas=new Canvas(bitmap);
					Paint paint=new Paint();
					float scaling=(float)bitmap.getWidth()/suggestMapWidth;
					if(newAreas!=null&&newAreas.size()>0){
						int w=0;
						for (Area area : newAreas) {
							area.drawArea(canvas, paint, scaling);
							onProgressUpdate(Integer.valueOf(MathUtils.percent(++w, newAreas.size(), 0, false, true)));
						}
					}
					//创建底图
					BitmapDrawable bitmapDrawable=new BitmapDrawable(getResources(), bitmap);
					bitmapDrawable.setBounds(0, 0, width, height);
					return bitmapDrawable;
				}
				
				@Override
				protected void onProgressUpdate(Integer... values) {
					if(listener!=null){
						listener.onInitProgressUpdate(values[0]);
					}
				}
				
				@Override
				protected void onPostExecute(BitmapDrawable result) {
					setInitFinish(result, newAreas);
				}
			}.execute(0);
		}
	}
	/**
	 * 设置地图（默认大小为原图的大小）
	 * @param bitmap
	 * @param newAreas
	 */
	public void setMap(Bitmap bitmap,List<Area> newAreas){
		 setMap(bitmap, newAreas, -1, -1);
	}
	/**
	 * 设置地图
	 * @param filePath（文件路径）
	 * @param newAreas（地图上的区域列表）
	 * @param suggestMapWidth--建议宽度
	 * @param suggestMapHeight--建议高度
	 */
	public void setMap(final String filePath,final List<Area> newAreas){
		if(StringUtils.isNotNullAndEmpty(filePath)&&newAreas!=null&&newAreas.size()>0){
			setInitStart();
			new AsyncTask<Integer,Integer, BitmapDrawable>(){

				@Override
				protected void onPreExecute() {
					initFinsish=false;
					if(listener!=null){
						listener.onInitStart();
					}
				}

				@Override
				protected BitmapDrawable doInBackground(Integer... params) {
					Bitmap baseBitmap=BitmapFactory.decodeFile(filePath);
					Bitmap bitmap=null;
					if(baseBitmap.getWidth()>maxSideLength){
						bitmap=BitmapUtils.scaleByWidth(baseBitmap, maxSideLength);
						baseBitmap.recycle();
					}else if(baseBitmap.getHeight()>maxSideLength){
						bitmap=BitmapUtils.scaleByHeight(baseBitmap, maxSideLength);
						baseBitmap.recycle();
					}else{
						bitmap=baseBitmap.copy(Config.ARGB_8888, true);
						baseBitmap.recycle();
					}
					/*初始化地图的最终宽高*/
					Options options=BitmapDecoder.decodeSizeFromFile(filePath);
					int width=options.outWidth;
					int height=options.outHeight;
					
					/*在底图上创建区域*/
					Canvas canvas=new Canvas(bitmap);
					Paint paint=new Paint();
					float scaling=(float)bitmap.getWidth()/width;
					int w=0;
					for (Area area : newAreas) {
						area.drawArea(canvas, paint, scaling);
						onProgressUpdate(Integer.valueOf(MathUtils.percent(++w, newAreas.size(), 0, false, true)));
					}
					/*创建底图*/
					BitmapDrawable bitmapDrawable=new BitmapDrawable(getResources(), bitmap);
					bitmapDrawable.setBounds(0,0,width,height);
					return bitmapDrawable;
				}
				
				@Override
				protected void onProgressUpdate(Integer... values) {
					if(listener!=null){
						listener.onInitProgressUpdate(values[0]);
					}
				}
				
				@Override
				protected void onPostExecute(BitmapDrawable result) {
					setInitFinish(result, newAreas);
				}
			}.execute(0);
		}
	}
	
	/**
	 * 从assets文件夹取出图片加载地图
	 * @param filename
	 * @param newAreas
	 */
	public void setMapByAssets(final String filename,final List<Area> newAreas){
		if(StringUtils.isNotNullAndEmpty(filename)&&newAreas!=null&&newAreas.size()>0){
			setInitStart();
			new AsyncTask<Integer, Integer, BitmapDrawable>(){

				@Override
				protected void onPreExecute() {
					initFinsish=false;
					if(listener!=null){
						listener.onInitStart();
					}
				}

				@Override
				protected BitmapDrawable doInBackground(Integer... params) {
					Bitmap baseBitmap=new BitmapDecoder((int)Runtime.getRuntime().maxMemory()/4/4).decodeFromAssets(getContext(), filename);
					Bitmap bitmap=null;
					if(baseBitmap.getWidth()>maxSideLength){
						bitmap=BitmapUtils.scaleByWidth(baseBitmap, maxSideLength);
						baseBitmap.recycle();
					}else if(baseBitmap.getHeight()>maxSideLength){
						bitmap=BitmapUtils.scaleByHeight(baseBitmap, maxSideLength);
						baseBitmap.recycle();
					}else{
						bitmap=baseBitmap.copy(Config.ARGB_8888, true);
						baseBitmap.recycle();
					}
					
					/*初始化底图的最终宽高*/
					Options options=BitmapDecoder.decodeSizeFromAssets(getContext(), filename);
					int width=options.outWidth;
					int height=options.outHeight;
					
					/*画区域*/
					Canvas canvas=new Canvas(bitmap);
					Paint paint=new Paint();
					float scaling=(float)bitmap.getWidth()/width;
					int w=0;
					for (Area area : newAreas) {
						area.drawArea(canvas, paint, scaling);
						onProgressUpdate(Integer.valueOf(MathUtils.percent(++w, bitmap.getWidth(), 0, false, true)));
					}
					/*创建底图*/
					BitmapDrawable bitmapDrawable=new BitmapDrawable(getResources(), bitmap);
					bitmapDrawable.setBounds(0,0,width,height);
					return bitmapDrawable;
				}
				
				@Override
				protected void onProgressUpdate(Integer... values) {
					if(listener!=null){
						listener.onInitProgressUpdate(values[0]);
					}
				}
				
				@Override
				protected void onPostExecute(BitmapDrawable result) {
					setInitFinish(result, newAreas);
				}
			};
		}
	}
	 /**
     * 获取可用宽度（去除左右内边距）
     * @return
     */
	public int getAvailableWidth() {
        return this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    /**
     * 获取可用高度（去除上下内边距）
     * @return
     */
    public int getAvailableHeight() {
        return this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
    }
	
    /**
     * 获取显示区域
     * @return
     */
    public final RectF getDisplayRect() {
    	if (isAllow()) {
			displayRect.set(drawable.getBounds());
			drawMatrix.mapRect(displayRect);
			if(textView != null){
				textView.setText(displayRect.toString());
			}
			
			/* 尝试扩大显示区域以便显示出超出的部分 */
			displayRect.left += offsetRect.left;
			displayRect.top += offsetRect.top;
			if(offsetRect.right > displayRect.width()){
				displayRect.right += offsetRect.right - displayRect.width();
			}
			if(offsetRect.bottom > displayRect.height()){
				displayRect.bottom += offsetRect.bottom - displayRect.height();
			}
			return displayRect;
		} else {
			return null;
		}
    }
	public interface Listener{
		/**
		 * 开始初始化
		 */
		public void onInitStart();
		/**
		 * 更新初始化进度
		 * @param percent
		 */
		public void onInitProgressUpdate(int percent);
		/**
		 * 初始化完成
		 */
		public void onInitFinish();
		/**
		 * 点击了某一个区域
		 * @param area
		 */
		public void onClickArea(Area area);
		/**
		 * 点击了某一个气泡
		 * @param area
		 */
		public void onClickBubble(Area area);
	}

	@Override
	public void onDown(MotionEvent motionEvent) {
		if(isAllow()){
			CurrentDownArea = findClickArea(motionEvent);
		}
	}

	@Override
	public void onUp(MotionEvent motionEvent) {
		if(isAllow() && CurrentDownArea != null){
			CurrentDownArea = null;
		}
	}
	
	@Override
	public void onSingleUp(MotionEvent e) {
		if(isAllow() && CurrentDownArea != null){
			if(CurrentDownArea.isShowBubble()){
				if(!CurrentDownArea.isClickedArea()){
					if(listener != null){
						listener.onClickBubble(CurrentDownArea);
					}
				}
			}else{
				if(listener != null){
					listener.onClickArea(CurrentDownArea);
				}
			}
		}
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(drawable != null){
			drawable.getBitmap().recycle();
			drawable.setCallback(null);
			unscheduleDrawable(drawable);
		}
	}
	/**
	 * 查找点击的区域
	 * @param x
	 * @param y
	 * @return
	 */
	private Area findClickArea(MotionEvent e){
		if(isAllow() && bubbleAreas != null || areas != null){
			RectF rectF = getDisplayRect();
			if(rectF != null){
				float x = (e.getX()-rectF.left)/currentScale;
				float y = (e.getY()-rectF.top)/currentScale;
				
				Area clickArea = null;
				if(bubbleAreas != null && bubbleAreas.size() > 0){
					for(Area area : bubbleAreas){
						if(area.isClickBubble(getContext(), x, y)){
							clickArea = area;
							clickArea.setClickedArea(false);
							break;
						}
					}
				}
				
				if(clickArea == null && areas != null && areas.size() > 0){
					for(Area area : areas){
						if(area.isClickArea(x, y)){
							clickArea = area;
							clickArea.setClickedArea(true);
							break;
						}
					}
				}
				return clickArea;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	/**
	 * 显示一个气泡
	 */
	public void showBubble(Area newArea){
		if(isAllow()&&newArea!=null){
			if(bubbleAreas==null) bubbleAreas=new ArrayList<Area>();
			bubbleAreas.add(newArea);
			offsetRect.set(0,0,0,0);
			checkOffset(newArea);
			invalidate();
		}
	}
	/**
	 * 显示定位气泡（一个气泡）
	 * @param newArea
	 */
	public void showSingleBubble(Area newArea){
		if(isAllow()&&newArea!=null){
			if(bubbleAreas==null){
				bubbleAreas=new ArrayList<Area>(1);
			}else{
				//清除所有的气泡
				clearAllBubble();
			}
			/*添加气泡*/
			newArea.setShowBubble(true, this);
			bubbleAreas.add(newArea);
			invalidate();
			/*移动到气泡位置*/
			Point offsetPoint=computeScrollOffset(bubbleAreas.get(0).getBubbleRect(getContext()));
			if(offsetPoint != null){
				getHandler().post(new com.lixueandroid.runnable.ScrollRunnable(getContext(), offsetPoint.x, offsetPoint.y));
			}
		}
	}
	/**
	 * 清除所有的气泡
	 */
	public void clearAllBubble(){
		if(isAllow()&&bubbleAreas!=null&&bubbleAreas.size()>0){
			for (Area area : bubbleAreas) {
				area.setShowBubble(false, this);
			}
		}
		bubbleAreas.clear();
	}
	/**
	 * 计算滚动位置
	 * @param rectf
	 * @return
	 */
	private Point computeScrollOffset(RectF tempRectf){
		RectF rectf = new RectF(tempRectf);
		rectf.left *=currentScale;
		rectf.top *= currentScale;
		rectf.right *= currentScale;
		rectf.bottom *= currentScale;
		
		RectF offsetRect = new RectF(Math.abs(getDisplayRect().left) - rectf.left, Math.abs(getDisplayRect().top) - rectf.top, rectf.right - (Math.abs(getDisplayRect().left) + getWidth()), rectf.bottom - (Math.abs(getDisplayRect().top) + getHeight()));
		
		float xOffset = 0;
		if(offsetRect.left > 0){
			xOffset = offsetRect.left;
		}else if(offsetRect.right > 0){
			if(rectf.width() > getWidth()){
				xOffset = offsetRect.left;
			}else{
				xOffset = -offsetRect.right;
			}
		}
		
		float yOffset = 0;
		if(offsetRect.top > 0){
			yOffset = offsetRect.top;
		}else if(offsetRect.bottom > 0){
			yOffset = -offsetRect.bottom;
		}
		
		if(xOffset != 0 && rectf.width() < getWidth()){
			xOffset += (getWidth() - rectf.width()) / 2 * (xOffset > 0?1:-1);
		}
		
		if(yOffset != 0 && rectf.height() < getHeight()){
			yOffset += (getHeight() - rectf.height()) / 2 * (yOffset > 0?1:-1);
		}
		
		if(xOffset != 0 || yOffset != 0){
			return new Point((int) xOffset, (int) yOffset);
		}else{
			return null;
		}
	}
	/**
	 * 检查是否超出了扩大区域，超出后显示出超出部分
	 * @param area
	 */
	private void checkOffset(Area area){
		/* 记录四边的值，以便扩大显示区域，显示超出部分 */
		float left=area.getBubbleRect(getContext()).left*currentScale;
		if(left<0&&left<offsetRect.left){
			offsetRect.left=left;
		}
		float right=area.getBubbleRect(getContext()).right*currentScale;
		if(right>offsetRect.right){
			offsetRect.right=right;
		}
		float top =area.getBubbleRect(getContext()).top*currentScale;
		if(top<0&&top <offsetRect.top){
			offsetRect.top=top;
		}
		float bottom=area.getBubbleRect(getContext()).bottom*currentScale;
		if(bottom>offsetRect.bottom){
			offsetRect.bottom=bottom;
		}
	}
	/**
	 * 定位
	 * @param x
	 * @param y
	 */
	public void location(final Area area){
		if(area != null){
			if(initFinsish){
				if(isAllow()){
					if(getWidth() > 0){
						setScale(1.0f);
						showSingleBubble(area);
					}else{
						getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
							@Override
							public void onGlobalLayout() {
								location(area);
								ViewUtils.removeOnGlobalLayoutListener(getViewTreeObserver(), this);
							}
						});
					}
				}
			}else{
				waitLocationArea = area;
			}
		}
	}
	public boolean isAllow(){
		return drawable != null && drawMatrix != null ;
	}
}
