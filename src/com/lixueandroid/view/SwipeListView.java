package com.lixueandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lixue.lixueandroid.R;


/**
 * 可以滑动出现移除按钮的listview
 * 
 * @author lixue
 *
 */
public class SwipeListView extends ListView {

	private static final String TAG = SwipeListView.class.getSimpleName();

	private boolean isShown;

	private View mPreItemView;

	private View mCurrentItemView;

	private float mFirstX;

	private float mFirstY;

	private boolean mIsHorizontal;

	public SwipeListView(Context context) {
		super(context);
	}

	public SwipeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		float lastX = ev.getX();
		float lastY = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			mIsHorizontal = false;
			
			mFirstX = lastX;
			mFirstY = lastY;
			int motionPosition = pointToPosition((int) mFirstX, (int) mFirstY);

			Log.e(TAG, "onInterceptTouchEvent----->ACTION_DOWN position=" + motionPosition);
			
			if (motionPosition >= 0) {
				View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
				mPreItemView = mCurrentItemView;
				mCurrentItemView = currentItemView;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			float dx = lastX - mFirstX;
			float dy = lastY - mFirstY;
			/**当滑动列表时，此事件被listview所截获**/
			if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
				return true;
			}
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:

			Log.i(TAG, "onInterceptTouchEvent----->ACTION_UP");
			if (isShown && mPreItemView != mCurrentItemView) {
				Log.i(TAG, "1---> hiddenRight");
				/**
                 * 情况一：
                 * <p>
                 * 一个Item的右边布局已经显示，
                 * <p>
                 * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                 */
				hiddenRight(mPreItemView);
			}
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		float lastX = ev.getX();
		float lastY = ev.getY();

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "---->ACTION_DOWN");
			break;

		case MotionEvent.ACTION_MOVE:
			float dx = lastX - mFirstX;
			float dy = lastY - mFirstY;

			mIsHorizontal = isHorizontalDirectionScroll(dx, dy);
			
			if (!mIsHorizontal) {
				break;
			}

			Log.i(TAG, "onTouchEvent ACTION_MOVE");
			
			if (mIsHorizontal) {
                if (isShown && mPreItemView != mCurrentItemView) {
                	Log.i(TAG, "2---> hiddenRight");
                    /**
                     * 情况二：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
                     * <p>
                     * 向左滑动只触发该情况，向右滑动还会触发情况五
                     */
                    hiddenRight(mPreItemView);
                }
			}else {
                if (isShown) {
                	Log.i(TAG, "3---> hiddenRight");
                    /**
                     * 情况三：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }
            }
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			Log.i(TAG, "============ACTION_UP");
			if (isShown) {
				Log.i(TAG, "4---> hiddenRight");
                /**
                 * 情况四：
                 * <p>
                 * 一个Item的右边布局已经显示，
                 * <p>
                 * 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                 */
                hiddenRight(mPreItemView);
            }

            if (mIsHorizontal) {
                if (mFirstX - lastX > 30) {
                    showRight(mCurrentItemView);
	                return true;
                } else {
                	Log.i(TAG, "5---> hiddenRight");
                    /**
                     * 情况五：
                     * <p>
                     * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                     */
                    hiddenRight(mCurrentItemView);
                }
            }
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void showRight(View rightView) {
		TextView rl_right=(TextView)rightView.findViewById(R.id.item_right);
		Button button_right=(Button) rightView.findViewById(R.id.button_item_delete);
		/**从右向左移动**/
		AnimationSet animationSet=new AnimationSet(false);
		TranslateAnimation translateAnimation=new TranslateAnimation(0,-230,0,0);
		translateAnimation.setDuration(2000);
		translateAnimation.setFillEnabled(true);
		translateAnimation.setFillAfter(true);
		translateAnimation.setFillBefore(false);
		
		animationSet.addAnimation(translateAnimation);
		rl_right.setAnimation(animationSet);
		rl_right.setVisibility(View.GONE);
		button_right.setVisibility(View.VISIBLE);
		isShown = true;
		
	}
	
	private void hiddenRight(View rightView) {
		
		/**从左向右移动**/
		TextView rl_right=(TextView)rightView.findViewById(R.id.item_right);
		Button button_right=(Button) rightView.findViewById(R.id.button_item_delete);
		button_right.setVisibility(View.GONE);
		rl_right.setVisibility(View.VISIBLE);
		isShown = false;
	}

	/**
	 * @param dx
	 * @param dy
	 * @return judge if can judge scroll direction
	 */
	private boolean isHorizontalDirectionScroll(float dx, float dy) {
		boolean mIsHorizontal = true;

		if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
			mIsHorizontal = true;
			System.out.println("mIsHorizontal---->" + mIsHorizontal);
		} else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
			mIsHorizontal = false;
			System.out.println("mIsHorizontal---->" + mIsHorizontal);
		}

		return mIsHorizontal;
	}
}
