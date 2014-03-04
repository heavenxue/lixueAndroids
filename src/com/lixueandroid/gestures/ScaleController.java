package com.lixueandroid.gestures;

import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 *	缩放控制器
 */
public class ScaleController implements ScaleGestureDetector.OnScaleGestureListener{
	private float currentFactor=1.0f;
	private float[] factors;
	private ScaleGestureDetector scaleGestureDetector;
	private Matrix matrix;
	
	public ScaleController(ScaleGestureDetector scaleGestureDetector){
		this.factors=new float[]{1.0f,2.0f,3.0f};
		this.scaleGestureDetector=scaleGestureDetector;
		this.matrix=new Matrix();
	}
	
	public Boolean onTouchEvent(MotionEvent event){
		return scaleGestureDetector.onTouchEvent(event);
	}
	
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		PostScale(detector.getScaleFactor(),detector.getFocusX(),detector.getFocusY());
		return false;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return false;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		
	}
	
	/**
	 * 更新缩放比例
	 * @param scaleFactor
	 * @param focusX
	 * @param focusY
	 */
	private void PostScale(float newScaleFactor,float focusX,float focusY){
		//如果缩放系数比1.0f还小，就让其为0.5f
		if(currentFactor * newScaleFactor < factors[0]/2){
			newScaleFactor = factors[0]/2/currentFactor;
			currentFactor = factors[0]/2;
		}else if(currentFactor * newScaleFactor > factors[factors.length - 1]){//如果缩放系统比3.0f还大，那么就就让其为3.0f
			newScaleFactor = factors[factors.length - 1]/currentFactor;
			currentFactor = factors[factors.length - 1];
		}else{//否则主为它本身自己的缩放系数
			currentFactor *= newScaleFactor;
		}
		matrix.postScale(newScaleFactor,newScaleFactor, focusX, focusY);
		//检查矩形边界，防止出界
	}
}