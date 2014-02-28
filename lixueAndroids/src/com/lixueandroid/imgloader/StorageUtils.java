package com.lixueandroid.imgloader;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


/**
 * 存储路径-大类
 * 
 * @author lixue
 * @since 1.0.0
 */
public final class StorageUtils {

	private static final String INDIVIDUAL_DIR_NAME = "uil-images";//默认目录名称

	private StorageUtils() {
	}

	/**
	 * 返回应用缓存目录，缓存目录将建立在sd卡上
	 * <i>("/Android/data/[app_package_name]/cache")</i> 如果SD卡安装，那么android系统定义的缓存目录在设备的文件系统中
	 * 
	 * @param context Application context
	 * @return Cache {@link File directory}
	 */
	public static File getCacheDirectory(Context context) {
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	/**
	 * 返回单个应用程序缓存目录
	 * 
	 * @param context Application context
	 * @return Cache {@link File directory}
	 */
	public static File getIndividualCacheDirectory(Context context) {
		File cacheDir = getCacheDirectory(context);
		File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
		if (!individualCacheDir.exists()) {
			if (!individualCacheDir.mkdir()) {
				individualCacheDir = cacheDir;
			}
		}
		return individualCacheDir;
	}

	/**
	 * 返回指定的应用程序缓存目录. 
	 * 
	 * @param context Application context
	 * @param cacheDir Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
	 * @return Cache {@link File directory}
	 */
	public static File getOwnCacheDirectory(Context context, String cacheDir) {
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
		}
		if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				Log.w("", "Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				Log.i("", "Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}
}