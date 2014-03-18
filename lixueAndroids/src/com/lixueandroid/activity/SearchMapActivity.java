package com.lixueandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lixue.lixueandroid.R;
import com.lixueandroid.MyApplication;
import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.map.MyPoiOverlay;

public class SearchMapActivity extends MyBaseActivity implements MKSearchListener{
	private Button searchButton;
	private MapView mapView; 
	
	private MyApplication app;
	private MapController mMapController;
	private MKSearch mkSearch;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		app=new MyApplication();
		if(app.mBMapMan==null){
			app.mBMapMan=new BMapManager(getBaseContext());
			app.mBMapMan.init(MyApplication.strKey, new MyApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_mymap);
		searchButton=(Button) findViewById(R.id.button_search_service);
		mapView=(MapView) findViewById(R.id.bmapsView);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mkSearch.poiSearchInCity("北京", "KFC");
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		mapView.setBuiltInZoomControls(true);
		mMapController=mapView.getController();
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
		GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));  
		mMapController.setCenter(point);//设置地图中心点  
		GeoPoint p=null;
		Intent intent=new Intent();
		if ( intent.hasExtra("x") && intent.hasExtra("y") ){
			//当用intent参数时，设置中心点为指定点
			Bundle b = intent.getExtras();
			p = new GeoPoint(b.getInt("y"), b.getInt("x"));
		}else{
			//设置中心点为天安门
			p = new GeoPoint((int)(39.915* 1E6), (int)(116.404 * 1E6));
		}
		mMapController.setCenter(p);
		mMapController.setZoom(12);//设置地图zoom级别
		mkSearch=new MKSearch();
		mkSearch.init(app.mBMapMan, this);
	}

	@Override
	public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		if(arg0!=0){
			toastL("未找到搜索结果");
		}else{
			toastL("成功，成功查看详情页面");
		}
	}

	@Override
	public void onGetPoiResult(MKPoiResult res, int arg1, int erro) {
		if(res!=null){
			 if (res.getCurrentNumPois() > 0) {
	            // 将poi结果显示到地图上
	            MyPoiOverlay poiOverlay = new MyPoiOverlay(SearchMapActivity.this, mapView, mkSearch);
	            poiOverlay.setData(res.getAllPoi());
	            mapView.getOverlays().clear();
	            mapView.getOverlays().add(poiOverlay);
	            mapView.refresh();
	            //当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
	            for( MKPoiInfo info : res.getAllPoi() ){
	            	if ( info.pt != null ){
	            		mapView.getController().animateTo(info.pt);
	            		break;
	            	}
	            }
	        } else if (res.getCityListNum() > 0) {
	        	//当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
	            String strInfo = "在";
	            for (int i = 0; i < res.getCityListNum(); i++) {
	                strInfo += res.getCityListInfo(i).city;
	                strInfo += ",";
	            }
	            strInfo += "找到结果";
	            toastL(strInfo);
	        }
		}else{
			toastL("没有搜索结果");
		}
	}

	@Override
	public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
