package com.lixueandroid.myinterface;

import android.view.View;

public interface IAdapter {
	public int getCount();
	public String getItem(int position);
	public long getItemId(int position);
	public View getView(int position);
}
