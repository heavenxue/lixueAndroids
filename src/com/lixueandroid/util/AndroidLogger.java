package com.lixueandroid.util;

import java.io.File;
import java.io.IOException;

import me.xiaopan.easyjava.util.DateTimeUtils;
import me.xiaopan.easyjava.util.FileUtils;
import android.util.Log;

/**
 * <H2>AndroidLog工具箱</H2>
 *<br> *在编程的时候，不论怎样都要用到log日志去打印一些提示信息去便于自己的查看，但是当我们的程序被发布到应用市场的时候，就没有必要打印这些log日志了，
 * 所以，可以通过设置AndroidLogger.setEnableOutputToConsole()或者设置AndroidLogger.setEnableOutputToLocalFile()去控制是否输出到控制台或保存在本地
 * <br>
 * <br>*再一个就是可以通过AndroidLogger.setOutputFile()方法来设置存储Log的文件
 * <br>
 * <br>*你还可以通过setDefaultLogTag来设置默认的Tag
 * 
 *@author LIXUE
 */
public class AndroidLogger {
	/**
	 * 默认的LogTag
	 */
	private static String defaultLogTag;
	/**
	 * 是否输出到控件台
	 */
	private static boolean enableOutputToConsole;
	/**
	 * 是否输出到本地
	 */
	private static boolean enableOutputToLocalFile;
	/**
	 * 输出的文件
	 */
	private static File outputFile;
	
	public static final boolean v(String LogTag,String LogContent){
		if(enableOutputToConsole){
			 Log.v(LogTag, LogContent);
		}
		return outPutToFile(LogTag+" "+LogContent);
	}
	
	public static final boolean v(String logContent){
		return v(defaultLogTag,logContent);
	}
	
	public static final boolean d(String logTag,String logContent){
		if(enableOutputToConsole){
			Log.d(logTag, logContent);
		}
		return outPutToFile(logContent);
	}
	
	public static final boolean d(String logContent){
		return d(defaultLogTag,logContent);
	}
	
	public static final boolean i(String logTag,String logContent){
		if(enableOutputToConsole){
			Log.i(logTag, logContent);
		}
		return outPutToFile(logContent);
	}
	
	public static final boolean i(String logContent){
		return i(defaultLogTag,logContent);
	}
	
	public static final boolean w(String logTag,String logContent){
		if(enableOutputToConsole){
			Log.w(logTag, logContent);
		}
		return outPutToFile(logContent);
	}
	
	public static final boolean w(String logContent){
		return w(defaultLogTag,logContent);
	}
	
	public static final boolean e(String logTag,String logContent){
		if(enableOutputToConsole){
			Log.e(logTag, logContent);
		}
		return outPutToFile(logContent);
	}
	
	public static final boolean e(String logContent){
		return e(defaultLogTag,logContent);
	}
	
	public static final boolean wtf(String logTag,String logContent){
		return wtf(defaultLogTag, logContent);
	}
	
	public static final boolean wtf(String logContent){
		return wtf(defaultLogTag, logContent);
	}
	
	public static final boolean wtf(String logTag, Throwable th){
		if(enableOutputToConsole){
			Log.wtf(logTag, th);
		}
		return outPutToFile(logTag+" "+th.getMessage());
	}
	
	public static final boolean wtf(Throwable th){
		return wtf(defaultLogTag, th);
	}
	
	public static final boolean wtf(String logTag, String logContent, Throwable th){
		if(enableOutputToConsole){
			Log.wtf(logTag, logContent, th);
		}
		return outPutToFile(logTag+" "+logContent);
	}
	
	public static final boolean outPutToFile(String logContent){
		try {
			if(enableOutputToLocalFile&&outputFile!=null){
				if(outputFile.exists()){
					FileUtils.writeStringByLine(outputFile, DateTimeUtils.getCurrentDateTimeByDefultCustomFormat()+"" +logContent, true);
					return true;
				}else{
					File parentFile=outputFile.getParentFile();
					//如果父目录不存在就创建，如果创建失败了就直接结束
					if(!parentFile.exists()&&!parentFile.mkdirs()){
						return false;
					}
					if(outputFile.createNewFile()){
						FileUtils.writeStringByLine(outputFile, DateTimeUtils.getCurrentDateTimeByDefultCustomFormat()+"" +logContent, true);
						return true;
					}else{
						return false;
					}
				}
			}else{
				return false;
			}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
	}

	public static String getDefaultLogTag() {
		return defaultLogTag;
	}

	public static void setDefaultLogTag(String defaultLogTag) {
		AndroidLogger.defaultLogTag = defaultLogTag;
	}

	public static boolean isEnableOutputToConsole() {
		return enableOutputToConsole;
	}

	public static boolean isEnableOutputToLocalFile() {
		return enableOutputToLocalFile;
	}
	
	public static File getOutputFile() {
		return outputFile;
	}
	
	public static void setEnableOutputToConsole(boolean enableOutputToConsole) {
		AndroidLogger.enableOutputToConsole = enableOutputToConsole;
	}

	public static void setEnableOutputToLocalFile(boolean enableOutputToLocalFile) {
		AndroidLogger.enableOutputToLocalFile = enableOutputToLocalFile;
	}

	public static void setOutputFile(File outputFile) {
		AndroidLogger.outputFile = outputFile;
	}
}
