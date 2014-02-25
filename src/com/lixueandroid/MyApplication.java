package com.lixueandroid;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.lixue.lixueandroid.R;
import com.lixueandroid.imgloader.DisplayImageOptions;
import com.lixueandroid.imgloader.FadeInBitmapDisplayer;
import com.lixueandroid.imgloader.ImageLoader;
import com.lixueandroid.imgloader.ImageLoaderConfiguration;
import com.lixueandroid.imgloader.ImageLoadingListener;
import com.lixueandroid.imgloader.Md5FileNameGenerator;
import com.lixueandroid.imgloader.QueueProcessingType;
import com.lixueandroid.imgloader.RoundedBitmapDisplayer;
import com.lixueandroid.imgloader.SimpleImageLoadingListener;


/**
 * 全局变量-首选项
 * @author lixue
 *
 */
public class MyApplication extends Application {
	//初始化图像加载器的一些参数设置
	protected static ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageLoadingListener animateImageLoadingListener;
	
	//地图相关
	public BMapManager mBMapMan = null;  
	public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;

    public static final String strKey = "o1ZjGFCNU49oE2SLGSthLmQB";
  //单例模式
  	private static MyApplication intance=null;
  	
  	public static MyApplication getIntance(){
  		return intance;
  	}
  	
	@Override
	public void onCreate() {
		super.onCreate();
		imageLoader=ImageLoader.getInstance();
		setAnimateImageLoadingListener(new AnimateFirstDisplayListener());
		initImageLoader(getApplicationContext());
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory()
		.cacheOnDisc()
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
		
		/* 极光推动初始化模块 */
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //地图相关
        intance=this;
        initEngineManager(this);
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.enableLogging() // Not necessary in common
				.build();
		imageLoader.init(config);
	}
	
	/**
	 * 得到图像加载器
	 * @return ImageLoader
	 */
	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	/**
	 * 设置图像加载器
	 * @param void
	 */
	public static void setImageLoader(ImageLoader imageLoader) {
		MyApplication.imageLoader = imageLoader;
	}

	/**
	 * 得到图像加载器的选项设置
	 * @return DisplayImageOptions
	 */
	public DisplayImageOptions getOptions() {
		return options;
	}

	/**
	 * 设置图像加载器的选项设置
	 * @param options
	 */
	public void setOptions(DisplayImageOptions options) {
		this.options = options;
	}

	/**
	 * 得到图像开始加载时的事件
	 * @return ImageLoadingListener
	 */
	public ImageLoadingListener getAnimateImageLoadingListener() {
		return animateImageLoadingListener;
	}

	/**设置图像开始加载时的事件
	 * @param animateImageLoadingListener
	 */
	public void setAnimateImageLoadingListener(ImageLoadingListener animateImageLoadingListener) {
		this.animateImageLoadingListener = animateImageLoadingListener;
	}

	/**
	 * 图像加载时所显示的一点点渐隐渐现的动画
	 * @author lixue
	 *
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	//地图相关
	/**
	 * 初始化
	 */
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(getIntance().getApplicationContext(), "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	 /**
	  * 常用事件监听，用来处理通常的网络错误，授权验证错误等
	  * 
	 * @author LIXUE
	 *
	 */
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int arg0) {
			if(arg0==MKEvent.ERROR_NETWORK_CONNECT)
				Toast.makeText(getIntance().getApplicationContext(), "您的网络出错啦！",Toast.LENGTH_LONG).show();
			else if(arg0==MKEvent.ERROR_NETWORK_DATA)
				Toast.makeText(getIntance().getApplicationContext(), "请输入正确的检索条件！",Toast.LENGTH_LONG).show();
			else if(arg0==MKEvent.ERROR_PERMISSION_DENIED)
				Toast.makeText(getIntance().getApplicationContext(), "授权验证失败！",Toast.LENGTH_LONG).show();
			else if(arg0==MKEvent.ERROR_RESULT_NOT_FOUND)
				Toast.makeText(getIntance().getApplicationContext(), "未找到搜索结果！",Toast.LENGTH_LONG).show();
			else if(arg0==MKEvent.ERROR_ROUTE_ADDR)
				Toast.makeText(getIntance().getApplicationContext(), "路线搜索起点或终点有歧义！",Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
		 	//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                Toast.makeText(getIntance().getApplicationContext(),  "请在 DemoApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
                getIntance().m_bKeyRight = false;
            }
            else{
            	getIntance().m_bKeyRight = true;
            	Toast.makeText(getIntance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG).show();
            }
		}
	 }
}