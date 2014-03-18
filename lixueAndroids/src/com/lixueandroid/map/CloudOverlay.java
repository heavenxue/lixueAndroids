package com.lixueandroid.map;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lixue.lixueandroid.R;

/**
 * LBS.云服务 图层
 * 
 * @author LIXUE
 *
 */
public class CloudOverlay  extends ItemizedOverlay<OverlayItem>{
	private List<CloudPoiInfo> mLbsPoints;
	private  Activity mContext;
	private MapView mapView;

	public CloudOverlay(Activity activity,MapView mMapView){
		super(null,mMapView);
		mContext=activity;
		mapView=mMapView;
	}
	 public void setData(List<CloudPoiInfo> lbsPoints) {
	        if (lbsPoints != null) {
	            mLbsPoints = lbsPoints;
	        }
	        for ( CloudPoiInfo rec : mLbsPoints ){
	        	//GeoPoint的第一个参数为纬度，第二个对数为经度
	            GeoPoint pt = new GeoPoint((int)(rec.latitude * 1e6), (int)(rec.longitude * 1e6));
	            OverlayItem item = new OverlayItem(pt , rec.title, rec.address);
	            Drawable marker1 = this.mContext.getResources().getDrawable(R.drawable.marker1);
	            item.setMarker(marker1);
	            addItem(item);
	        }
	    }
	 
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
