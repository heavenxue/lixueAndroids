package com.lixueandroid.map;

import android.app.Activity;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKSearch;

/**
 * poi图层
 * @author LIXUE
 *
 */
public class MyPoiOverlay extends PoiOverlay{
	private MKSearch mkSearch;
	
	public MyPoiOverlay(Activity arg0, MapView arg1,MKSearch mkSearch) {
		super(arg0, arg1);
		this.mkSearch=mkSearch;
	}

	@Override
	protected boolean onTap(int i) {
		super.onTap(i);
		MKPoiInfo info = getPoi(i);
        if (info.hasCaterDetails) {
        	mkSearch.poiDetailSearch(info.uid);
        }
		return true;
	}
	
}
