package com.lixueandroid.domain;

import java.io.Serializable;


/**
 *用户类
 * 
 */
public class User implements Serializable {
	/**
	 * 序列id
	 */
	private static final long serialVersionUID = 1L;
	private int id;// QQ用户id
	private String name;// 姓名
	private String email;// 邮件
	private String password;// 密码
	private int isOnline;// 是否在线
	private int img;// 图像
	private String ip;
	private int port;
	private int group;
	public int myname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			User user = (User) o;
			if (user.getId() == id && user.getIp().equals(ip)&& user.getPort() == port) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email+ ", password=" + password + ", isOnline=" + isOnline+ ", img=" + img + ", ip=" + ip + ", port=" + port + ", group="+ group + "]";
	}
}