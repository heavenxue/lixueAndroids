package com.lixueandroid.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateUtils {
	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}
	/**
	 * 判断给定字符串时间是否为昨天
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isYesterday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			int ti=time.getDate()+1;
			int to=today.getDate();
			if(ti==to){
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * 判断给定字符串时间是否为本周
	 * 
	 * @param sdaString
	 * @return
	 */
	public static boolean isThisWeek(String sdaString){
		if(isWeek(sdaString).equals("0")){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断给定字符串时间是否为本周以前的时间
	 * 
	 * @param sdaString
	 * @return
	 */
	public static boolean isTheWeekAgo1(String sdaString){
		if(isWeek(sdaString).equals("1")){
			return true;
		}
		return false;
	} 
	
	
	/**
	 *  判断给定字符串时间是否为本周
	 *  
	 * @param sdate
	 * @return
	 */
	public static String isWeek(String sdate) {
		Date todayDate=new Date();
		int s=getDateBefore(todayDate, 7).getDate();
		Date theDate=toDate(sdate);
		int y=theDate.getDate();
		if(s==y){
			return "0";	
		}else if(s>y){
			return "1";
		}else{
			return "";
		}
	}
	
	/**
	 * 计算date之前n天的日期
	 */
	public static Date getDateBefore(Date date, int n) {
		Calendar now = Calendar.getInstance();  
		now.setTime(date);  
		now.set(Calendar.DATE, now.get(Calendar.DATE) - n);  
		return now.getTime();
	}
	
	/** 
     * 得到几天后的时间 
     */        
    public static Date getDateAfter(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
        return now.getTime();  
    }  
	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@SuppressLint("SimpleDateFormat")
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@SuppressLint("SimpleDateFormat")
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
}
