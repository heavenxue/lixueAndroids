package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
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
import com.lixueandroid.MyBaseActivity;

/**
 *	地图
 */
public class MyMapActivity extends MyBaseActivity implements MKSearchListener{
	BMapManager mBMapMan = null;  
	MapView mMapView = null;  
	MKSearch mMkSearch=null;
	
	private Button searchServiceButton;
	private ListView mSuggestionList;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		mBMapMan=new BMapManager(getApplication());  
		mBMapMan.init("o1ZjGFCNU49oE2SLGSthLmQB", null);    
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错  
		setContentView(R.layout.activity_mymap);
		mMapView=(MapView)findViewById(R.id.bmapsView);
		searchServiceButton=(Button) findViewById(R.id.button_search_service);
		mSuggestionList=(ListView) findViewById(R.id.mylist);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		/* 
		 * 搜索服务
		 */
		searchServiceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//-----------范围检索----------------
//				// 北京西站  
//				GeoPoint ptLB = new GeoPoint( (int)(39.901375 * 1E6),(int)(116.329099 * 1E6));   
//				// 北京北站  
//				GeoPoint ptRT = new GeoPoint( (int)(39.949404 * 1E6),(int)(116.360719 * 1E6));  
//				//检索北京西站与北京北站为顶点所确定的距形区域内的KFC餐厅
//				mMkSearch.poiSearchInbounds("KFC", ptLB, ptRT);  
				//-----------城市检索----------------
				//检索北京的KFC餐厅
//				mMkSearch.poiSearchInCity("北京", "KFC");
				//周边检索
				//检索天安门周边5000米之内的KFC餐厅：
//				mMkSearch.poiSearchNearBy("KFC",  new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6)), 5000);
				//-------------地址信息查询------------------
//				mMkSearch.reverseGeocode(new GeoPoint(40057031, 116307852)); //逆地址解析  
//				mMkSearch.geocode("KFC", "北京");//地址解析  
				//-------------在线建议查询-------------------
				mSuggestionList.setVisibility(View.VISIBLE);
				mMapView.setVisibility(View.GONE);
				mMkSearch.suggestionSearch("KFC", "北京");
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		//设置启用内置的缩放控件  
		mMapView.setBuiltInZoomControls(true);
		//设置实时路况
//		mMapView.setTraffic(true);  
		//显示卫星图
//		mMapView.setSatellite(true);  
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放  
		MapController mMapController=mMapView.getController();  
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)  
		GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));  
		mMapController.setCenter(point);//设置地图中心点  
		mMapController.setZoom(12);//设置地图zoom级别
		//搜索服务初始化
		mMkSearch=new MKSearch();
		mMkSearch.init(mBMapMan, this);
	}
	@Override  
	protected void onDestroy(){  
        mMapView.destroy();  
        if(mBMapMan!=null){  
                mBMapMan.destroy();  
                mBMapMan=null;  
        }  
        super.onDestroy();  
	}  
	@Override  
	protected void onPause(){  
        mMapView.onPause();  
        if(mBMapMan!=null){  
               mBMapMan.stop();  
        }  
        super.onPause();  
	}  
	@Override  
	protected void onResume(){  
        mMapView.onResume();  
        if(mBMapMan!=null){  
                mBMapMan.start();  
        }  
       super.onResume();  
	}

	//返回地址信息搜索结果    
	@Override
	public void onGetAddrResult(MKAddrInfo res, int error) {
		if (error != 0) {  
	        String str = String.format("错误号：%d", error);  
	        Toast.makeText(MyMapActivity.this, str, Toast.LENGTH_LONG).show();  
	        return;  
	    }  
	    //地图移动到该点  
	    mMapView.getController().animateTo(res.geoPt);  
	    if (res.type == MKAddrInfo.MK_GEOCODE) {  
	        //地理编码：通过地址检索坐标点  
	        String strInfo = String.format("纬度：%f 经度：%f", res.geoPt.getLatitudeE6()/1e6, res.geoPt.getLongitudeE6()/1e6);  
	        Toast.makeText(MyMapActivity.this, strInfo, Toast.LENGTH_LONG).show();  
	    }  
	    if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {  
	        //反地理编码：通过坐标点检索详细地址及周边poi  
	        String strInfo = res.strAddr;  
	        Toast.makeText(MyMapActivity.this, strInfo, Toast.LENGTH_LONG).show();  
	    }  
	}

	//返回公交车详情信息搜索结果    		
	@Override
	public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		
	}

	//返回驾乘路线搜索结果
	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		
	}

	//返回poi详情信息搜索结果  
	@Override
	public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		
	}

	//返回poi搜索结果  
	@Override
	public void onGetPoiResult(MKPoiResult arg0, int arg1, int error) {
		// 错误号可参考MKEvent中的定义  
		if ( error == MKEvent.ERROR_RESULT_NOT_FOUND){  
			Toast.makeText(MyMapActivity.this, "抱歉，未找到结果",Toast.LENGTH_LONG).show();  
			return ;  
        }  
        else if (error != 0 || arg0 == null) {  
			Toast.makeText(MyMapActivity.this, "搜索出错啦..", Toast.LENGTH_LONG).show();  
			return;  
		}  
		// 将poi结果显示到地图上  
		PoiOverlay poiOverlay = new PoiOverlay(MyMapActivity.this, mMapView);  
		poiOverlay.setData(arg0.getAllPoi());  
		mMapView.getOverlays().clear();  
		mMapView.getOverlays().add(poiOverlay);  
		mMapView.refresh();  
		//当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空  
		for(MKPoiInfo info : arg0.getAllPoi() ){  
			if ( info.pt != null ){  
				mMapView.getController().animateTo(info.pt);  
				break;  
			}  
    	}  
	}

	//在此处理短串请求返回结果.   
	@Override
	public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {
		
	}

	//返回联想词信息搜索结果    
	@Override
	public void onGetSuggestionResult(MKSuggestionResult res, int iError) {
		if (iError!= 0 || res == null) {  
            Toast.makeText(MyMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();   
            return;  
        }  
        int nSize = res.getSuggestionNum();  
        String[] mStrSuggestions = new String[nSize];  
        for (int i = 0; i <nSize; i++){  
           mStrSuggestions[i] = res.getSuggestion(i).city + res.getSuggestion(i).key;  
        }  
        ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(MyMapActivity.this, android.R.layout.simple_list_item_1,mStrSuggestions);  
        mSuggestionList.setAdapter(suggestionString);  
	}

	@Override
	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		//返回公交搜索结果 
		
	}

	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		//返回步行路线搜索结果
		
	}  
}
