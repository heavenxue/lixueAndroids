package com.lixueandroid.activity.domain;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * Mp3类
 * @author Administrator
 *
 */
public class Mp3Info implements Serializable{
	/**
	 * 序列号ID
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	private String id;//id

	@Expose
	private String mp3Name;	//mp3名字

	@Expose
	private String mp3Size;	//mp3大小 

	@Expose
	private String lrcName;	//歌词名字

	@Expose
	private String lrcSize;	//歌词大小
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMp3Name() {
		return mp3Name;
	}
	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}
	public String getMp3Size() {
		return mp3Size;
	}
	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}
	public String getLrcName() {
		return lrcName;
	}
	public void setLrcName(String lrcName) {
		this.lrcName = lrcName;
	}
	public String getLrcSize() {
		return lrcSize;
	}
	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}
	
	@Override
	public String toString(){
		return id+", "+mp3Name+", "+mp3Size+", "+lrcName+", "+lrcSize;
	}
}
