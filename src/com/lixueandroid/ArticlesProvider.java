package com.lixueandroid;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.lixueandroid.domain.Articles;

public class ArticlesProvider extends ContentProvider{
	private static final String TAG=ArticlesProvider.class.getSimpleName();
	private static final String DB_NAME="Articles.db";
	private static final String DB_TABLE="ArticlesTable";
	private static final int DB_VERSION=1;
	private static final String DB_CREATE="create table "+DB_TABLE+" ("+
			Articles.ID+" integer primary key autoincrement ,"+
			Articles.TITLE+" text not null ," +
			Articles.ABSTRACT +" text not null ,"+
			Articles.URL +" text not null);";
	
	private static final UriMatcher uriMatcher;
	static{
		uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Articles.AUTHORITY, "item", Articles.ITEM);
		uriMatcher.addURI(Articles.AUTHORITY, "item/#", Articles.ITEM_ID);
		uriMatcher.addURI(Articles.AUTHORITY, "pos/#",Articles.ITEM_POS);
	}
	private static final HashMap<String, String> articleProjectMap;
	static{
		articleProjectMap=new HashMap<String, String>();
		articleProjectMap.put(Articles.ID, Articles.ID);
		articleProjectMap.put(Articles.TITLE, Articles.TITLE);
		articleProjectMap.put(Articles.ABSTRACT, Articles.ABSTRACT);
		articleProjectMap.put(Articles.URL,Articles.URL);
	}
	private ContentResolver resolver=null	;
	private DBHelper dbHelper=null;


	/* 用来返回数据的MIME类型
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case Articles.ITEM:
			return Articles.CONTENT_TYPE;
		case Articles.ITEM_ID:
		case Articles.ITEM_POS:
			return Articles.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Error uri:"+uri);
		}
	}

	/* 用来插入新的数据
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (uriMatcher.match(uri)!=Articles.ITEM) {
			throw new IllegalArgumentException("Error uri:"+uri);
		}
		SQLiteDatabase dbDatabase=dbHelper.getWritableDatabase();
		long id=dbDatabase.insert(DB_TABLE, Articles.ID, values);
		if (id<0) {
			throw new SQLiteException("unable to insert "+values+"for "+uri);
		}
		Uri newUri=ContentUris.withAppendedId(uri, id);
		resolver.notifyChange(newUri, null);
		return newUri;
	}

	/* 用来执行一些初始化的工作。
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		Context context=getContext();
		resolver=context.getContentResolver();
		dbHelper=new DBHelper(context, DB_NAME, null, DB_VERSION);
		Log.i(TAG, "Articles Provider Create.");
		return true;
	}

	/* 用来更新已有的数据
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		int count=0;
		switch (uriMatcher.match(uri)) {
		case Articles.ITEM:
			count=db.update(DB_TABLE, values,selection, selectionArgs);
			break;
		case Articles.ITEM_ID:{
			String id=uri.getPathSegments().get(1);
			count=db.update(DB_TABLE, values, Articles.ID+"="+id+(!TextUtils.isEmpty(selection)) != null?" and ("+selection+" )": "", selectionArgs);
			break;
		}
		default:
			throw new IllegalArgumentException("Error uri:"+uri);
		}
		resolver.notifyChange(uri, null);
		return 0;
	}
	
	/*，用来删除数据
	 *  (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		int count=0;
		switch (uriMatcher.match(uri)) {
		case Articles.ITEM:
			count=db.delete(DB_TABLE,selection,selectionArgs);
			break;
		case Articles.ITEM_ID:{
			String id=uri.getPathSegments().get(1);
			count=db.delete(DB_TABLE, Articles.ID+"="+id+(!TextUtils.isEmpty(selection)) != null?" and ("+selection+" )": "", selectionArgs);
			break;
		}
		default:
			throw new IllegalArgumentException("Error uri:"+uri);
		}
		return 0;
	}
	
	/* 用来返回数据给调用者(查询)
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
		Log.i(TAG, "ArticlesProvider query:"+uri);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		SQLiteQueryBuilder sqlbBuilder=new SQLiteQueryBuilder();
		String limit=null;
		switch (uriMatcher.match(uri)) {
		case Articles.ITEM:
			sqlbBuilder.setTables(DB_TABLE);
			sqlbBuilder.setProjectionMap(articleProjectMap);
			break;
		case Articles.ITEM_ID:
			String id=uri.getPathSegments().get(1);
			sqlbBuilder.setTables(DB_TABLE);
			sqlbBuilder.setProjectionMap(articleProjectMap);
			sqlbBuilder.appendWhere(Articles.ID+"="+id);
			break;
		case Articles.ITEM_POS:
			String pos=uri.getPathSegments().get(1);
			sqlbBuilder.setTables(DB_TABLE);
			sqlbBuilder.setProjectionMap(articleProjectMap);
			limit=pos+",1";
		default:
			throw new IllegalArgumentException("Error uri:"+uri);
		}
		Cursor cursor=sqlbBuilder.query(db, projection, selection, selectionArgs, null, null, TextUtils.isEmpty(sortOrder)?Articles.DEFAULT_SORT_ORDER:sortOrder,limit);
		cursor.setNotificationUri(resolver, uri);
		return cursor;
	}

	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		Log.i(TAG, "ArticlesProvider.call:"+method);
		if (method.equals(Articles.METHOD_GET_ITEM_COUNT)) {
			return getItemCount();
		}
		throw new IllegalArgumentException("Error method call:"+method);
	}
	private Bundle getItemCount(){
		Log.i(TAG, "ArticlesProvider getItemCount");
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select count(1) from "+DB_TABLE, null);
		int count=0;
		if (cursor.moveToFirst()) {
			count=cursor.getInt(0);
		}
		Bundle bundle=new Bundle();
		bundle.putInt(Articles.KEY_ITEM_COUNT, count);
		cursor.close();
		db.close();
		return bundle;
	}
	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "创建表的语句:"+DB_CREATE);
			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists "+DB_TABLE);
			onCreate(db);
		}
	}
}
