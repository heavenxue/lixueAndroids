package com.lixueandroid.domain;

/**
 * 服务端广播发过来的数据-类
 * @author lixue
 *
 */
public class BroadcastData {
	private String type;//text,alert,shock,read,playMRdo,exitApp,shutdown,cardRec,giveUp,saveQsn,saveCardImg,getStatus,login
	private String msg;//消息内容
	private String during;//倒计时数量，为0，需要操作员自己点击确定，如果为-1，则不能点击确定，必须要输入管理员确认码才能点击确定
	private String repnum;//播放手机上指定的声音文件n次
	private String rdoId;//本地的文件的名称或者ID，这个中的id待定
	private String statusCode;//状态码 200为登录成功，404为登录名不存在，400为已登录,500 为识别错误
	private String maxC;//最大卡号
	private String minC;//最小卡号
	private String showL;//是否长亮闪光灯  1为长亮  0为不长亮
	private String dorDuring;//休眠等待时间，单位为秒
	private String exitPwd;//退出的时候的管理码
	private String manPwd;//通常的管理码
	private String capBarcode;//二维码
	private String capQsn;//问题
	private String cardPreDuring;
	private String loginName;//登录名
	private String sumCardNum;//名片采集总数
	private String warnNum;//警告总数
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDuring() {
		return during;
	}
	public void setDuring(String during) {
		this.during = during;
	}
	public String getRepnum() {
		return repnum;
	}
	public void setRepnum(String repnum) {
		this.repnum = repnum;
	}
	public String getRdoId() {
		return rdoId;
	}
	public void setRdoId(String rdoId) {
		this.rdoId = rdoId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMaxC() {
		return maxC;
	}
	public void setMaxC(String maxC) {
		this.maxC = maxC;
	}
	public String getMinC() {
		return minC;
	}
	public void setMinC(String minC) {
		this.minC = minC;
	}
	public String getShowL() {
		return showL;
	}
	public void setShowL(String showL) {
		this.showL = showL;
	}
	public String getDorDuring() {
		return dorDuring;
	}
	public void setDorDuring(String dorDuring) {
		this.dorDuring = dorDuring;
	}
	public String getExitPwd() {
		return exitPwd;
	}
	public void setExitPwd(String exitPwd) {
		this.exitPwd = exitPwd;
	}
	public String getManPwd() {
		return manPwd;
	}
	public void setManPwd(String manPwd) {
		this.manPwd = manPwd;
	}
	public String getCapBarcode() {
		return capBarcode;
	}
	public void setCapBarcode(String capBarcode) {
		this.capBarcode = capBarcode;
	}
	public String getCapQsn() {
		return capQsn;
	}
	public void setCapQsn(String capQsn) {
		this.capQsn = capQsn;
	}
	public String getCardPreDuring() {
		return cardPreDuring;
	}
	public void setCardPreDuring(String cardPreDuring) {
		this.cardPreDuring = cardPreDuring;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getSumCardNum() {
		return sumCardNum;
	}
	public void setSumCardNum(String sumCardNum) {
		this.sumCardNum = sumCardNum;
	}
	public String getWarnNum() {
		return warnNum;
	}
	public void setWarnNum(String warnNum) {
		this.warnNum = warnNum;
	}
}
