package com.lixueandroid.providers;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 *	文章ContentProvider
 */
public class ArticlesProvider extends ContentProvider{
	 private static final String LOG_TAG = "com.lixueandroid.providers.articles.ArticlesProvider";  
	  
     private static final String DB_NAME = "Articles.db";  
     private static final String DB_TABLE = "ArticlesTable";  
     private static final int DB_VERSION = 1;  

     private static final String DB_CREATE = "create table " + DB_TABLE +  
                             " (" + Articles.ID + " integer primary key autoincrement, " +  
                             Articles.TITLE + " text not null, " +  
                             Articles.ABSTRACT + " text not null, " +  
                             Articles.URL + " text not null);";  

     private static final UriMatcher uriMatcher;  
     static {  
             uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);  
             uriMatcher.addURI(Articles.AUTHORITY, "item", Articles.ITEM);  
             uriMatcher.addURI(Articles.AUTHORITY, "item/#", Articles.ITEM_ID);  
             uriMatcher.addURI(Articles.AUTHORITY, "pos/#", Articles.ITEM_POS);  
     }  

     private static final HashMap<String, String> articleProjectionMap;  
     static {  
             articleProjectionMap = new HashMap<String, String>();  
             articleProjectionMap.put(Articles.ID, Articles.ID);  
             articleProjectionMap.put(Articles.TITLE, Articles.TITLE);  
             articleProjectionMap.put(Articles.ABSTRACT, Articles.ABSTRACT);  
             articleProjectionMap.put(Articles.URL, Articles.URL);  
     }  

//     private DBHelper dbHelper = null;  
     private ContentResolver resolver = null;  
     
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {  
        case Articles.ITEM:  
                return Articles.CONTENT_TYPE;  
        case Articles.ITEM_ID:  
        case Articles.ITEM_POS:  
                return Articles.CONTENT_ITEM_TYPE;  
        default:  
                throw new IllegalArgumentException("Error Uri: " + uri);  
        }  
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
		return 0;
	}
}
