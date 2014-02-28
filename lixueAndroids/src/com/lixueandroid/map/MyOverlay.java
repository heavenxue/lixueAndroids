package com.lixueandroid.map;

import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 自定义图层
 * 
 * @author LIXUE
 *
 */
public class MyOverlay extends ItemizedOverlay<OverlayItem>{
	
	public MyOverlay(Drawable mark, MapView mapview) {
		super(mark, mapview);
	}

	@Override
	public boolean onTap(GeoPoint poi, MapView arg1) {
		//在此处理mapview的点击事件，当点击时返回true；
		super.onTap(poi, arg1);
		return false;
	}
	
	protected boolean onTap(int index) {
		//在此处理item的点击事件
		System.out.println("Item onTap:"+index);
		return true;
	}
}
