package com.lixueandroid.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * SD卡工具箱
 * @author feifei
 *
 */
public class SDCardUtils {
	
	/**
	 * 获取SD卡的状态
	 * @return 
	 */
	public static String getState(){
		return Environment.getExternalStorageState();
	}
	
	/**
	 * SD卡是否可用
	 * @return 只有当SD卡已经安装并且准备好了才返回true
	 */
	public static boolean isAvailable(){
		return getState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取SD卡的根目录
	 * @return null：不存在SD卡
	 */
	public static File getRootDirectory(){
		return isAvailable()?Environment.getExternalStorageDirectory():null;
	}
	
	/**
	 * 获取SD卡的根路径
	 * @return null：不存在SD卡
	 */
	public static String getRootPath(){
		File rootDirectory = getRootDirectory();
		return rootDirectory != null ?rootDirectory.getPath():null;
	}
	
	/**
	 * 获取SD卡总的容量
	 * @return 总容量；-1：SD卡不可用
	 */
	public static long getTotalSize(){
		long result = -1;
		if(isAvailable()){
			StatFs statFs = new StatFs(getRootDirectory().getPath());
			result = statFs.getBlockCount() * statFs.getBlockSize();
		}
		return result;
	}
	
	/**
	 * 获取SD卡中可用的容量
	 * @return 可用的容量；-1：SD卡不可用
	 */
	public static long getAvailableSize(){
		long result = -1;
		if(isAvailable()){
			StatFs statFs = new StatFs(getRootDirectory().getPath());
			result = statFs.getAvailableBlocks() * statFs.getBlockSize();
		}
		return result;
	}
}