package com.lixueandroid.util;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

/**
 * 输入验证工具箱
 */
public class InputVerifyUtils {
	/**
	 * 验证给定的值是否合法
	 * @param context 上下文
	 * @param view 如果验证失败，将会晃动此视图一提醒用户
	 * @param vlaue 验证的值
	 * @param errorHint 不合法时提示的内容
	 * @return 是否合法
	 */
	public static boolean valiNullAndEmpty(Context context, View view, String vlaue, String errorHint){
		boolean result = true;
		if(vlaue == null || "".equals(vlaue)){
			if(errorHint != null && !"".equals(errorHint)){
				AndroidUtils.toastS(context, errorHint);
			}
			AnimationUtils.shake(view);
			result = false;
		}
		return result;
	}
	
	/**
	 * 检查普通的文本
	 * @param editText 待检查的编辑器
	 * @param minLength 最小长度，-1忽略此项
	 * @param maxLength 最大长度，-1忽略此项
	 * @param failedShake 检查不通过是否震动编辑器
	 * @param failHint 检测不通过时的提示信息，null：不提示
	 * @param checkTextListener 检查结果监听器
	 * @return 
	 */
	public static boolean checkText(EditText editText, int minLength, int maxLength, CheckTextListener checkTextListener){
		boolean result = false;
		
		String content = editText.getEditableText().toString().trim();
		
		//检验非空
		if(!"".equals(content)){
			result = true;
		}else{
			result = false;
			//如果有监听器
			if(checkTextListener != null){
				checkTextListener.onEmpty(editText, content);
			}
			return result;
		}
		
		//如果需要检查最小长度
		if(minLength > -1){
			if(content.length() >= minLength){
				result = true;
			}else{
				result = false;
				//如果有监听器
				if(checkTextListener != null){
					checkTextListener.onMinLengthCheckFailed(editText, content);
				}
				return result;
			}
		}
		
		//如果需要检查最大长度
		if(maxLength > -1){
			if(content.length() <= maxLength){
				result = true;
			}else{
				result = false;
				//如果有监听器
				if(checkTextListener != null){
					checkTextListener.onMaxLengthCheckFailed(editText, content);
				}
				return result;
			}
		}
		
		return result;
	}
	
	/**
	 * 检查结果监听器
	 * @author xiaopan
	 *
	 */
	public interface CheckTextListener{
		/**
		 * 当是空的
		 * @param editText
		 * @param content
		 */
		public void onEmpty(EditText editText, String content);

		/**
		 * 当最小长度检查不通过
		 * @param editText
		 * @param content
		 */
		public void onMinLengthCheckFailed(EditText editText, String content);
		
		/**
		 * 当失败了
		 * @param editText
		 * @param content
		 */
		public void onMaxLengthCheckFailed(EditText editText, String content);
	}
}