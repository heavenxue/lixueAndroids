package com.lixueandroid.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.lixueandroid.domain.Mp3Info;



public class Utils {
	/**
	 * 为给定的字符串添加HTML红色标记，当使用Html.fromHtml()方式显示到TextView 的时候其将是红色的
	 * @param string 给定的字符串
	 * @return
	 */
	public static String addHtmlRedFlag(String string){
		return "<font color=\"red\">"+string+"</font>";
	}
	
	/**
	 * 将给定的字符串中所有给定的关键字标红
	 * @param sourceString 给定的字符串
	 * @param keyword 给定的关键字
	 * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml()转换为Spanned对象再传递给TextView对象
	 */
	public static String keywordMadeRed(String sourceString, String keyword){
		String result = "";
		if(sourceString != null && !"".equals(sourceString.trim())){
			if(keyword != null && !"".equals(keyword.trim())){
				result = sourceString.replaceAll(keyword, "<font color=\"red\">"+keyword+"</font>"); 
			}else{
				result = sourceString;
			}
		}
		return result;
	}

	/**
	 * 获取本地上所有的mp3文件
	 * @return
	 */
	public static List<Mp3Info> getLocalMp3Files(String path){
		List<Mp3Info> mp3Files=new ArrayList<Mp3Info>();
		String totalPath= Environment.getExternalStorageDirectory()+File.separator+path;
		File file=new File(totalPath);
		File[] files=file.listFiles();
		if(files!=null&&files.length>0){
			for (int i = 0; i < files.length; i++) {
				Mp3Info mp3Info=new Mp3Info();
				mp3Info.setMp3Name(files[i].getName());
				mp3Info.setMp3Size(files[i].length()+"");
				mp3Files.add(mp3Info);
			}
		}
		return mp3Files;
	}
	/**
	 * 删除指定路径下的所有文件
	 * @param path
	 */
	public static boolean deleteAllFiles(String path) {
		boolean success = false; 
	    try { 
	        ArrayList<File> images = new ArrayList<File>(); 
	        getFiles(images, path); 
	        File latestSavedImage = images.get(0); 
	        if (latestSavedImage.exists()) { 
	            for (int i = 1; i < images.size(); i++) { 
	                File nextFile = images.get(i); 
	                if(getExtensionName(nextFile.getName()).equals("png")){
	                	if (nextFile.lastModified() > latestSavedImage.lastModified()) { 
	                		latestSavedImage = nextFile; 
	                	} 
	                }
	            } 
	            Log.e("brady", "images = " + latestSavedImage.getAbsolutePath()); 
	            success = latestSavedImage.delete(); 
	        } 
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	    return success; 
	}
	/**
	 * 得到指定路径下的所有文件
	 * @param fileList
	 * @param path
	 */
	private static void getFiles(ArrayList<File> fileList, String path) { 
	    File[] allFiles = new File(path).listFiles(); 
	    for (int i = 0; i < allFiles.length; i++) { 
	        File file = allFiles[i]; 
	        if (file.isFile()) { 
	            fileList.add(file); 
	        } else if (!file.getAbsolutePath().contains(".thumnail")) { 
	            getFiles(fileList, file.getAbsolutePath()); 
	        } 
	    } 
	}
    /**
     * 获取文件的扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {    
        if ((filename != null) && (filename.length() > 0)) {    
            int dot = filename.lastIndexOf('.');    
            if ((dot >-1) && (dot < (filename.length() - 1))) {    
                return filename.substring(dot + 1);    
            }    
        }    
        return filename;    
    }    
    /**
     * 
     * @param str  要截取的字符串 如输入：测试testing嘻嘻
     * @param length 截取的字符串位数   :12
     * 结果：测试testing...
     * @return
     */
    public String subStringByBytes(String str, int length) {
        String result = "";
        int i = 0;
        int j = 0;

        StringBuffer buff = new StringBuffer(str);
        String stmp;
        int len = buff.length();
        for (i = 0; i < len; i++) {
            if (j < length) {
            stmp = buff.substring(i, i + 1);
            try {
                stmp = new String(stmp.getBytes("utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (stmp.getBytes().length > 1) {
                j += 2;
            } else {
                j += 1;
            }
            result += stmp;
            }else{
                break;
            }
        }
        if (j > length) {
            result = result.substring(0, result.length() - 1);
        }
        result += "...";
        return result;
        }
}