package com.lixueandroid.util;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lixueandroid.domain.User;

public class JsonUtils {
	
	/**
	 * 通过url，向服务器获取json数据
	 * @param url
	 * @throws IOException 
	 */
	public static String getJsonDataFromUrl(String jsonData) throws IOException{
		String result = null;
		JsonReader reader=new JsonReader(new StringReader(jsonData));
		reader.beginArray();
		while(reader.hasNext()){
		  reader.beginObject();
		  while(reader.hasNext()){
		     String tagName=reader.nextName();
		     if(tagName.equals("name")){
		    	 result="name--->"+reader.nextString();
		     }
		     else if(tagName.equals("age")){
		    	 result+="age--->"+reader.nextInt()+".";
		     }
		     reader.endObject();
		  }
		  reader.endArray();
		}
		return result;
	}
	
	public static String getObject(String jsonData,User user){
		Gson gson=new Gson();
		user=gson.fromJson(jsonData,User.class);
		return user.getName()+":"+user.getAge();
	}
	
	public static User getMultiyObject(String jsonData){
		User myuser = null;
		Type listType=new TypeToken<LinkedList<User>>(){}.getType();
		Gson gson=new Gson();
		LinkedList<User> users=gson.fromJson(jsonData,listType);
		for(Iterator iterator=users.iterator();iterator.hasNext();){
		   myuser=(User)iterator.next();
		}
		return myuser;
	}
}
