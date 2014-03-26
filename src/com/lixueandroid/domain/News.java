package com.lixueandroid.domain;

import android.content.ContentValues;


public class News {
	 public  String ID ;  
     public  String TITLE;  
     public  String ABSTRACT;  
     public  String URL;
	public String getID() {
		return ID;
	}
	public String getTITLE() {
		return TITLE;
	}
	public String getABSTRACT() {
		return ABSTRACT;
	}
	public String getURL() {
		return URL;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public void setABSTRACT(String aBSTRACT) {
		ABSTRACT = aBSTRACT;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}  
     
	public ContentValues toContentValues(){
		ContentValues contentValues=new ContentValues();
		contentValues.put(Articles.ID, ID);
		contentValues.put(Articles.TITLE, TITLE);
		contentValues.put(Articles.ABSTRACT, ABSTRACT);
		contentValues.put(Articles.URL, URL);
		return contentValues;
		
	}
}