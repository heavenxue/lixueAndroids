/**
 * 
 */
/**
 * @author LIXUE
 *
 */
package com.lixueandroid.map;

import android.app.Activity;

import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 开启导航的功能
 */
//开启导航功能（前提必须是安装百度地图v5.0及其以上版本）
public class OpenNavigation{
	private static GeoPoint pt1;
	private static GeoPoint pt2;
	private static Activity activity;
	
	public OpenNavigation(GeoPoint pt1,GeoPoint pt2,Activity activity){
		OpenNavigation.pt1=pt1;
		OpenNavigation.pt2=pt2;
		OpenNavigation.activity=activity;
	}
	public static void openNavigation(){
		/* 
		 * 导航参数 
		 * 导航起点和终点不能为空，当GPS可用时启动从用户位置到终点间的导航， 
		 * 当GPS不可用时，启动从起点到终点间的模拟导航。 
		 */  
		NaviPara para = new NaviPara();  
		para.startPoint = pt1;           //起点坐标  
		para.startName= "从这里开始";  
		para.endPoint  = pt2;            //终点坐标  
		para.endName   = "到这里结束";        
		try {  
		   //调起百度地图客户端导航功能,参数this为Activity。   
		        BaiduMapNavigation.openBaiduMapNavi(para, activity);  
		} catch (BaiduMapAppNotSupportNaviException e) {  
		        //在此处理异常  
		        e.printStackTrace();  
		}  
	}
			
}