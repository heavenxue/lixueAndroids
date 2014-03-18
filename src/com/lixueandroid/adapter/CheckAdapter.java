package com.lixueandroid.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

/**
 * 复选框或单选框-适配器
 * @author lixue
 *
 * @param <T>
 */
public abstract class CheckAdapter<T extends CheckItem> extends BaseAdapter {
	private List<T> items;	//列表项
	private boolean enableCheckMode;	//激活选择模式
	private boolean singleMode;	//单选模式
	private int currentCheckedPosition = -1;
	
	public CheckAdapter(List<T> items) {
		this.items = items;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
		notifyDataSetChanged();
	}
	
	/**
	 * 是否是单选模式
	 * @return
	 */
	public boolean isSingleMode() {
		return singleMode;
	}

	/**
	 * 设置单选模式，默认是复选模式
	 * @param singleMode
	 */
	public void setSingleMode(boolean singleMode) {
		this.singleMode = singleMode;
	}

	/**
	 * 处理复选框
	 * @param checkBox
	 * @param t
	 */
	public void handleCheckBox(CheckBox checkBox, T t){
		checkBox.setChecked(t.isChecked());
		checkBox.setVisibility(isEnableCheckMode()?View.VISIBLE:View.GONE);
	}
	
	/**
	 * 激活选择模式
	 */
	public void enableCheckMode(){
		enableCheckMode = true;
		notifyDataSetChanged();
	}

	/**
	 * 取消选择模式
	 */
	public void cancelCheckMode(){
		enableCheckMode = false;
		for(T item : items){
			item.setChecked(false);
		}
		notifyDataSetChanged();
	}
	
	/**
	 * 判定是否激活选择模式
	 * @return
	 */
	public boolean isEnableCheckMode() {
		return enableCheckMode;
	}
	
	/**
	 * 点击了某一项
	 * @param postion
	 * @return true：已经激活了选择模式并且设置成功；false：尚未激活选择模式并且设置失败
	 */
	public boolean clickItem(int postion){
		if(isEnableCheckMode()){
			if(postion < items.size()){
				if(singleMode){
					if(currentCheckedPosition == -1 || currentCheckedPosition == postion){
						T item = items.get(postion); 
						item.setChecked(!item.isChecked());
					}else{
						items.get(currentCheckedPosition).setChecked(false);
						items.get(postion).setChecked(true);
					}
					currentCheckedPosition = postion;
				}else{
					T item = items.get(postion); 
					item.setChecked(!item.isChecked());
				}
				notifyDataSetChanged();
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取选中的项
	 * @return
	 */
	public List<T> getCheckedItems(){
		List<T> checkedItems = new ArrayList<T>();
		for(T item : items){
			if(item.isChecked()){
				checkedItems.add(item);
			}
		}
		return checkedItems;
	}
	
	/**
	 * 删除选中的项
	 * @return
	 */
	public List<T> deleteCheckedItems(){
		List<T> checkedItems = new ArrayList<T>();
		Iterator<T> iterator = items.iterator();
		T item;
		while(iterator.hasNext()){
			item = iterator.next();
			if(item.isChecked()){
				checkedItems.add(item);
				iterator.remove();
			}
		}
		notifyDataSetChanged();
		return checkedItems;
	}
}