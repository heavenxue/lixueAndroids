package com.lixueandroid.domain;

import android.net.Uri;

public class Articles {

	  /*Data Field*/  
   public static  String ID = "_id";  
   public static  String TITLE = "_title";  
   public static  String ABSTRACT = "_abstract";  
   public static  String URL = "_url";  
 
   /*Default sort order*/  
   public static final String DEFAULT_SORT_ORDER = "_id asc";  
 
   /*Call Method*/  
   public static final String METHOD_GET_ITEM_COUNT = "METHOD_GET_ITEM_COUNT";  //得到总数量
   public static final String KEY_ITEM_COUNT = "KEY_ITEM_COUNT";  //得到每项的key值
 
   /*Authority*/  
   public static final String AUTHORITY = "com.lixue.lixueandroid.articles";  
 
   /*Match Code*/  
   public static final int ITEM = 1;  
   public static final int ITEM_ID = 2;  
   public static final int ITEM_POS =3 ;  
 
   /*MIME*/  
   public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.lixue.article";  
   public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/vnd.lixue.article";  
 
   /*Content URI*/  
   public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");  
   public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/pos");

}
