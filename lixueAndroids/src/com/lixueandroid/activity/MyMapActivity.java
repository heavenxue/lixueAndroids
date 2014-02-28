package com.lixueandroid.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.cloud.BoundSearchInfo;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchInfo;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
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
import com.lixueandroid.map.CloudOverlay;
import com.lixueandroid.map.MyPoiOverlay;

/**
 *	地图
 */
public class MyMapActivity extends MyBaseActivity implements MKSearchListener,CloudListener{
	BMapManager mBMapMan = null;  
	/**
	 * 地图主控件
	 */
	MapView mMapView = null;  
	MKSearch mMkSearch=null;
	/**
	 *  用MapController完成地图控制 
	 */
	private MapController mMapController = null;
	/**
	 *  MKMapViewListener 用于处理地图事件回调
	 */
	MKMapViewListener mMapListener = null;
	private Button searchServiceButton;
	private ListView mSuggestionList;
	private Button localButton;
	private Button roundButton;
	private Button rectangleButton;
	private Button detailButton;
	private MyApplication app;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错  
		app=(MyApplication) this.getApplication();
		  if (app.mBMapManager == null) {
	            app.mBMapManager = new BMapManager(this);
	            /**
	             * 如果BMapManager没有初始化则初始化BMapManager
	             */
	            app.mBMapManager.init(MyApplication.strKey,new MyApplication.MyGeneralListener());
	        }
		setContentView(R.layout.activity_mymap);
		mMapView=(MapView)findViewById(R.id.bmapsView);
		searchServiceButton=(Button) findViewById(R.id.button_search_service);
		mSuggestionList=(ListView) findViewById(R.id.mylist);
		localButton=(Button) findViewById(R.id.button_local);
		roundButton=(Button) findViewById(R.id.button_round);
		rectangleButton=(Button) findViewById(R.id.button_rectangle);
		detailButton=(Button) findViewById(R.id.button_detail);
	}

	/* (non-Javadoc)
	 * @see me.xiaopan.easyandroid.app.BaseActivityInterface#onInitListener(android.os.Bundle)
	 */
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
				GeoPoint ptLB = new GeoPoint( (int)(39.901375 * 1E6),(int)(116.329099 * 1E6));   
				// 北京北站  
				GeoPoint ptRT = new GeoPoint( (int)(39.949404 * 1E6),(int)(116.360719 * 1E6));  
				//检索北京西站与北京北站为顶点所确定的距形区域内的KFC餐厅
				mMkSearch.poiSearchInbounds("KFC", ptLB, ptRT);  
				//-----------城市检索----------------
				//检索北京的KFC餐厅
//				mMkSearch.poiSearchInCity("北京", "餐厅");
				//周边检索
				//检索天安门周边5000米之内的KFC餐厅：
//				mMkSearch.poiSearchNearBy("KFC",  new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6)), 5000);
				//-------------地址信息查询------------------
//				mMkSearch.reverseGeocode(new GeoPoint(40057031, 116307852)); //逆地址解析  
//				mMkSearch.geocode("KFC", "北京");//地址解析  
				//-------------在线建议查询-------------------
//				mSuggestionList.setVisibility(View.VISIBLE);
//				mMapView.setVisibility(View.GONE);
//				mMkSearch.suggestionSearch("KFC", "北京");
				//-------------短串分享-----------------------
				//根据POI点的UID，生成一个短链接，用于分享
//				mMkSearch.poiDetailShareURLSearch(new MKPoiInfo().uid);
				/* 
	              * 发起地址信息短串请求，一个位置的地理信息一可以通过GeoCode/反GeoCode搜索获得. 
	              * mMkSearch 为 MKSearch象， 
	              * 参数pt为要分享的位置的经纬度坐标 ，name和addr为百度地图客户端在展示该位置时显示的名称和地址. 
	              */  
//                GeoPoint pt = new GeoPoint((int)(39.945*1E6),(int)(116.404*1E6));  
//                String name = "天安门";  
//                String addr = "西城区景山前街4号";  
//                mMkSearch.poiRGCShareURLSearch(pt,name,addr);  
				//MKSearch  对象在不使用时需执行销毁函数.  
//				mSearch.destory(); 
				//-------------驾车线路搜索----------------------
//				MKPlanNode start = new MKPlanNode();  
//				//天安门到百度大厦的驾车路线	
//				start.pt = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));  
//				MKPlanNode end = new MKPlanNode();  
//				end.pt = new GeoPoint(40057031, 116307852);// 设置驾车路线搜索策略，时间优先、费用最少或距离最短  
//				mMkSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);  //时间优先
////				mMkSearch.setDrivingPolicy(MKSearch.ECAR_FEE_FIRST);//费用最少
////				mMkSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);//距离最短
////				mMkSearch.drivingSearch(null, start, null, end);  
//				mMkSearch.walkingSearch(null, start, null, end);
				//公交线路搜索
//				mMkSearch.poiSearchInCity("北京", "717");  //利用兴趣点搜索的方法获取待查公交线路的信息；
				//---------------------位置图层----------------------------
				//位置图层,能够实现在地图上显示当前位置的图标以及指南针：
//				MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);  
//				LocationData locData = new LocationData();  
//				//手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）  
//				locData.latitude = 39.945;  
//				locData.longitude = 116.404;  
//				locData.direction = 2.0f;  
//				myLocationOverlay.setData(locData);  
//				mMapView.getOverlays().add(myLocationOverlay);  
//				mMapView.refresh();  
//				mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude* 1e6)));  
				//---------------------图层---------------------------
				//自定义图层
				//准备要添加的Overlay  
//				double mLat1 = 39.90923;  
//				double mLon1 = 116.397428;  
//				double mLat2 = 39.9022;  
//				double mLon2 = 116.3922;  
//				double mLat3 = 39.917723;  
//				double mLon3 = 116.3722;  
//				// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)  
//				GeoPoint p1 = new GeoPoint((int) (mLat1 * 1E6), (int) (mLon1 * 1E6));  
//				GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));  
//				GeoPoint p3 = new GeoPoint((int) (mLat3 * 1E6), (int) (mLon3 * 1E6));  
//				//准备overlay图像数据，根据实情情况修复  
//				Drawable mark= getResources().getDrawable(R.drawable.marker1);  
//				//用OverlayItem准备Overlay数据  
//				OverlayItem item1 = new OverlayItem(p1,"item1","item1");  
//				item1.setTitle("item1");
//				//使用setMarker()方法设置overlay图片,如果不设置则使用构建ItemizedOverlay时的默认设置  
//				OverlayItem item2 = new OverlayItem(p2,"item2","item2");  
//				item2.setMarker(mark);  
//				OverlayItem item3 = new OverlayItem(p3,"item3","item3");  
//				   
//				//创建IteminizedOverlay  
//				MyOverlay itemOverlay = new MyOverlay(mark, mMapView);  
//				//将IteminizedOverlay添加到MapView中  
//				  
//				mMapView.getOverlays().clear();  
//				mMapView.getOverlays().add(itemOverlay);  
//				   
//				//现在所有准备工作已准备好，使用以下方法管理overlay.  
//				//添加overlay, 当批量添加Overlay时使用addItem(List<OverlayItem>)效率更高  
//				itemOverlay.addItem(item1);  
//				itemOverlay.addItem(item2);  
//				itemOverlay.addItem(item3);  
//				mMapView.refresh();  
				//弹出窗口示图层
				//创建pop对象，注册点击事件监听接口  
//				PopupOverlay pop=new PopupOverlay(mMapView, new PopupClickListener() {
//					
//					@Override
//					public void onClickedPopup(int arg0) {
//						//点击第个图片的时候的事件
//						toastS("点击了"+arg0);
//					}
//				});
//				/**  准备pop弹窗资源，根据实际情况更改 
//				 *  弹出包含三张图片的窗口，可以传入三张图片、两张图片、一张图片。 
//				 *  弹出的窗口，会根据图片的传入顺序，组合成一张图片显示. 
//				 *  点击到不同的图片上时，回调函数会返回当前点击到的图片索引index 
//				 */  
//				Bitmap[] bmps = new Bitmap[3];  
//				try {  
//					 bmps[0] = BitmapFactory.decodeStream(getAssets().open("marker1.png"));  
//				     bmps[1] = BitmapFactory.decodeStream(getAssets().open("marker2.png"));  
//				     bmps[2] = BitmapFactory.decodeStream(getAssets().open("marker3.png"));  
//				} catch (IOException e) {  
//				         e.printStackTrace();  
//				}  
//				//弹窗弹出位置  
//				GeoPoint ptTAM = new GeoPoint((int)(39.915 * 1E6), (int) (116.404 * 1E6));  
//				//弹出pop,隐藏pop  
//				pop.showPopup(bmps, ptTAM, 32);  
//				//隐藏弹窗  
//				//  pop.hidePop();
				//绘制几何图形
//				//故宫左上角  
//				GeoPoint geoPoint1 = new GeoPoint((int)(39.929 * 1E6),(int)(116.397 * 1E6));  
//				//故宫右下角  
//				GeoPoint geoPoint2 = new GeoPoint((int)(39.920 * 1E6),(int)(116.408 * 1E6));  
//				GeoPoint palaceCenter = new GeoPoint((int)(39.924 * 1E6),(int)(116.403 * 1E6));  
//				//创建一个覆盖故宫范围的距形  
//				Geometry palaceGeometry = new Geometry();  
//				palaceGeometry.setEnvelope(geoPoint1, geoPoint2);  //设置机壳
//				//注：Symbol对象不可复用，要绘制多个图形，需要创建多个Symbol。
//				Symbol palaceSymbol = new Symbol();//创建样式  
//				Symbol.Color palaceColor= palaceSymbol.new Color();//创建颜色  
//				palaceColor.red = 0;//设置颜色的红色分量  
//				palaceColor.green = 0;//设置颜色的绿色分量  
//				palaceColor.blue = 255;//设置颜色的蓝色分量  
//				palaceColor.alpha = 126;//设置颜色的alpha值  
//				// 创建一个边框对象  
//				// 参数 - 5： 边框的线宽  
//				// 参数 - polygonSymbol.new Color(0xffff0000)：边框的颜色  
//				Stroke stroke = new Stroke(5,palaceSymbol.new Color(0xffff0000));  
//				palaceSymbol.setSurface(palaceColor,1,3,stroke);//设置样式参数，颜色：palaceColor是否填充距形：是线  
//				//开始创建图形
//				Graphic palaceGraphic=new Graphic(palaceGeometry, palaceSymbol);//注：Graphic对象不可复用，要绘制多个图形，需要创建多个Graphic。
//				/** 
//				* 创建一个GraphicsOverlay来装载故宫的Graphic 
//				* 构造参数mMapView为MapView对象，如果不知道如何生成MapView对象，可参看Hello World章节。 
//				*/  
//				GraphicsOverlay palaceOverlay = new GraphicsOverlay(mMapView);  
//				/** 
//				* 向GraphicsOverlay添加Graphic 
//				*一个GraphicsOVerlay可添加多个Graphic 
//				*GraphicsOVerlay的remove接口使用setData()返回的ID来移除指定Graphic 
//				*/  
//				long palaceId = palaceOverlay.setData(palaceGraphic);  
//				//将overlay添加到mapview中  
//				mMapView.getOverlays().add(palaceOverlay);  
//				//刷新地图使新添加的overlay生效  
//				mMapView.refresh();  
//				//移动，缩放地图到最视野  
//				mMapView.getController().setZoom(16);  
//				mMapView.getController().setCenter(palaceCenter);
				//绘制文字图层
//				//文字颜色
//				Symbol textSymbol = new Symbol();    
//				Symbol.Color textColor = textSymbol.new Color();    
//				textColor.alpha = 255;    
//				textColor.red = 0;    
//				textColor.blue = 255;    
//				textColor.green = 0;    
//				//文字背景颜色              
//				Symbol textSymbol1 = new Symbol();    
//				Symbol.Color textColor1 = textSymbol1.new Color();    
//				textColor1.alpha = 150;    
//				textColor1.red = 80;    
//				textColor1.blue = 80;    
//				textColor1.green = 80;
//				//文字项
//				TextItem textItem = new TextItem();  
//				textItem.fontColor = textColor;  
//				textItem.bgColor = textColor1;  
//				textItem.fontSize = 30;  
//				textItem.text = "看看我制作的文字图层吧";  
//				textItem.pt = mMapView.getMapCenter();  
//				
//				//定义文字图层
//				TextOverlay textOverlay=new TextOverlay(mMapView);
//				textOverlay.addText(textItem);
//				mMapView.getOverlays().add(textOverlay);
//				mMapView.refresh();
				//图片图层
//				double mLat5 = 39.950123;  
//				double mLon5 = 116.36115;  
//				double mLat6 = 39.911614;  
//				double mLon6 = 116.441638;  
//				GroundOverlay mGroundOverlay = new GroundOverlay(mMapView);  
//				GeoPoint leftBottom = new GeoPoint((int) (mLat5 * 1E6),(int) (mLon5 * 1E6));  
//				GeoPoint rightTop = new GeoPoint((int) (mLat6 * 1E6),(int) (mLon6 * 1E6));  
//				Drawable d = getResources().getDrawable(R.drawable.picture_sur);  
//				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();  
//				Ground mGround = new Ground(bitmap, leftBottom, rightTop);   
//				mMapView.getOverlays().add(mGroundOverlay);  
//				mGroundOverlay.addGround(mGround);  
//				mMapView.refresh();  
			}
		});
		 /**
    	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
    	 */
        mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调
				 * 缩放，平移等操作完成后，此回调被触发
				 */
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件
				 * 显示底图poi名称并移动至该点
				 * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
				 * 
				 */
				String title = "";
				if (mapPoiInfo != null){
					title = mapPoiInfo.strText;
					toastL(title);
					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap arg0) {
				/**
				 *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
				 *  可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}

			@Override
			public void onMapLoadFinish() {
				 /**
	             * 在此处理地图载完成事件 
	             */
				toastS("地图加载完成");
			}
        };
        //云服务
        //本地搜索
        localButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//LBS.云是百度地图针对LBS开发者推出的平台级服务。下面的ak要申请 后才可以使用，申请地址：http://lbsyun.baidu.com/
				LocalSearchInfo info = new LocalSearchInfo();
				info.ak = "o1ZjGFCNU49oE2SLGSthLmQB";
				info.geoTableId = 45961;
				info.tags = "";
				info.q="天安门";
				info.region = "北京市";
				CloudManager.getInstance().localSearch(info);
			}
		});
        //周边搜索
        roundButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NearbySearchInfo info = new NearbySearchInfo();
				info.ak = "o1ZjGFCNU49oE2SLGSthLmQB";
				info.geoTableId = 45961;
				info.q="故宫";
				info.location = "116.41664750113,39.920436528543";
				info.radius = 30000;
				CloudManager.getInstance().nearbySearch(info);
			}
		});
        //矩形搜索
        rectangleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BoundSearchInfo info=new BoundSearchInfo();
				info.ak = "o1ZjGFCNU49oE2SLGSthLmQB";
				info.geoTableId = 45961;
				info.q="天安门";
				info.bound="116.40,39.90;116.42,39.92";
				CloudManager.getInstance().boundSearch(info);
			}
		});
        //详细搜索
        detailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DetailSearchInfo info=new DetailSearchInfo();
				info.ak = "o1ZjGFCNU49oE2SLGSthLmQB";
				info.geoTableId = 45961;
				info.uid=47020948;
				CloudManager.getInstance().detailSearch(info);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		mMapController=mMapView.getController();
		/**
         *  设置地图是否响应点击事件  .
         */
        mMapController.enableClick(true);
        /**
         * 设置地图缩放级别
         */
        mMapController.setZoom(12);
        //设置实时路况
//		mMapView.setTraffic(true);  
        //显示卫星图
//		mMapView.setSatellite(true);
        //显示正常模式
//		mMapView.setSatellite(false);  
        //设置启用内置的缩放控件  
        mMapView.setBuiltInZoomControls(true);
        
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
		mMapView.regMapViewListener(MyApplication.getIntance().mBMapManager, mMapListener);
		//搜索服务初始化
		mMkSearch=new MKSearch();
		mMkSearch.init(app.mBMapMan, this);
		
		//LBS.云服务初始化
		CloudManager.getInstance().init(MyMapActivity.this);
	}
	/**
	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
	 */
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
	 @Override
	    protected void onSaveInstanceState(Bundle outState) {
	    	super.onSaveInstanceState(outState);
	    	mMapView.onSaveInstanceState(outState);
	    	
	    }
	    
	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    	super.onRestoreInstanceState(savedInstanceState);
	    	mMapView.onRestoreInstanceState(savedInstanceState);
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
	public void onGetBusDetailResult(MKBusLineResult result, int iError) {
		if (iError != 0 || result == null) {  
            Toast.makeText(MyMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();  
            return;  
	    }  
	    RouteOverlay routeOverlay = new RouteOverlay(MyMapActivity.this, mMapView);    // 此处仅展示一个方案作为示例  
	    routeOverlay.setData(result.getBusRoute());  
	    mMapView.getOverlays().clear();  
	    mMapView.getOverlays().add(routeOverlay);  
	    mMapView.refresh();  
	    mMapView.getController().animateTo(result.getBusRoute().getStart());  
	}

	//返回驾乘路线搜索结果
	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		if(arg0==null){
			return;
		}
		RouteOverlay routeOverlay=new RouteOverlay(MyMapActivity.this, mMapView);
		routeOverlay.setData(arg0.getPlan(0).getRoute(0));  
        mMapView.getOverlays().add(routeOverlay);
        mMapView.refresh(); 
	}

	//返回poi详情信息搜索结果  
	@Override
	public void onGetPoiDetailSearchResult(int arg0, int error) {
		  if (error != 0) {
			  toastL("抱歉，未找到结果");
          }
          else {
        	  toastL("成功，查看详情页面");
          }
	}

	//返回poi搜索结果  
	@Override
	public void onGetPoiResult(MKPoiResult res, int arg1, int error) {
		// 错误号可参考MKEvent中的定义  
		if ( error == MKEvent.ERROR_RESULT_NOT_FOUND){  
			Toast.makeText(MyMapActivity.this, "抱歉，未找到结果",Toast.LENGTH_LONG).show();  
			return ;  
        }  
        else if (error != 0 || res == null) {  
			Toast.makeText(MyMapActivity.this, "搜索出错啦..", Toast.LENGTH_LONG).show();  
			return;  
		}  
		 // 将地图移动到第一个POI中心点
        if (res.getCurrentNumPois() > 0) {
            // 将poi结果显示到地图上
            MyPoiOverlay poiOverlay = new MyPoiOverlay(MyMapActivity.this, mMapView, mMkSearch);
            poiOverlay.setData(res.getAllPoi());
            mMapView.getOverlays().clear();
            mMapView.getOverlays().add(poiOverlay);
            mMapView.refresh();
            //当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
            for( MKPoiInfo info : res.getAllPoi() ){
            	if ( info.pt != null ){
            		mMapView.getController().animateTo(info.pt);
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
		// 错误号可参考MKEvent中的定义  
		// 错误号可参考MKEvent中的定义  
//	       if (error != 0 || res == null) {  
//	               Toast.makeText(MyMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();  
//	               return;  
//	       }  
//	       // 找到公交路线poi node   
//	       MKPoiInfo curPoi = null;  
//	       int totalPoiNum  = res.getNumPois();  
//	       //当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空  
//	       for(int idx = 0; idx<totalPoiNum; idx++ ) {  
//	               curPoi = res.getPoi(idx);   
//	               if ( 2 == curPoi.ePoiType ) {  
//	                       break;   
//	               }  
//	       }  
//	       mMkSearch.busLineSearch("北京", curPoi.uid);  
	}

	//在此处理短串请求返回结果.   
	@Override
	public void onGetShareUrlResult(MKShareUrlResult result, int type, int error) {
		if ( error != 0){  
            //错误码不为0，表示搜索错误  
            return ;  
		}  
        if (type == MKEvent.MKEVENT_POIDETAILSHAREURL){  
            //返回poi详情短串  
            Log.d("baidumapsdk","poi详情短串"+result.url);  
        }     
        if (type == MKEvent.MKEVENT_POIRGCSHAREURL){  
            //返回地址信息短串  
            Log.d("baidumapsdk","地址信息短串"+result.url);  
        }  
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

	//返回公交搜索结果 
	@Override
	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		
	}

	//返回步行路线搜索结果
	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		if(arg0==null){
			return;
		}
		RouteOverlay routeOverlay=new RouteOverlay(MyMapActivity.this, mMapView);
		routeOverlay.setData(arg0.getPlan(0).getRoute(0));  
        mMapView.getOverlays().add(routeOverlay);
        mMapView.refresh(); 
	}

	@Override
	public void onGetDetailSearchResult(DetailSearchResult result, int arg1) {
		if (result != null) {
            if (result.poiInfo != null) {
                Toast.makeText(MyMapActivity.this, result.poiInfo.title, Toast.LENGTH_SHORT).show();
            }
            else {
            	toastL("status:" + result.status);
            }
        }
	}

	@Override
	public void onGetSearchResult(CloudSearchResult result, int arg1) {
		if (result != null && result.poiList!= null && result.poiList.size() > 0) {
            CloudOverlay poiOverlay = new CloudOverlay(this,mMapView);
            poiOverlay.setData(result.poiList);
            mMapView.getOverlays().clear();
            mMapView.getOverlays().add(poiOverlay);
            mMapView.refresh();
            mMapView.getController().animateTo(new GeoPoint((int)(result.poiList.get(0).latitude * 1e6), (int)(result.poiList.get(0).longitude * 1e6)));
        }
	}  
}
