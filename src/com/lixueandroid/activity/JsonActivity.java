package com.lixueandroid.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;

import me.xiaopan.easyandroid.app.BaseActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.util.HttpApacheFac;

/**
 * 解析Json数据的实例
 * @author Administrator
 *
 */
public class JsonActivity extends BaseActivity {
	private TextView jsonTextView;
	private TextView resolveJsonTextView;
	private Button requestButton;
	private Button fengzhuangrequestButton;
	private Button jsonRequestButton;
	private Button cookiesRequestButton;
	private Button ipRequestButton;
	private Button serveletRequestButton;
	private Button serveletArrayButton;
	
	private HttpPost post;
	private HttpResponse resp ;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_json);
		jsonTextView=(TextView) findViewById(R.id.text_json);
		resolveJsonTextView=(TextView) findViewById(R.id.text_resolve_json);
		requestButton=(Button) findViewById(R.id.button_simple_request);
		fengzhuangrequestButton=(Button) findViewById(R.id.button_fengzhuang_request);
		jsonRequestButton=(Button) findViewById(R.id.button_json_request);
		cookiesRequestButton=(Button) findViewById(R.id.button_cookies_request);
		ipRequestButton=(Button) findViewById(R.id.button_ip_request);
		serveletRequestButton=(Button) findViewById(R.id.button_servlet_request);
		serveletArrayButton=(Button) findViewById(R.id.button_servletarray_request);
//		HttpPost post = new HttpPost("http://m.weather.com.cn/data/101110101.html");
		try {
			post = new HttpPost("http://192.168.2.254:8016/MobileCode.ashx");
			resp = HttpApacheFac.getHttpClient().execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		//简单请求
		requestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					//在返回的状态码正确后
					if(resp.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
						StringBuffer sb = new StringBuffer();
						BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
						for(String line=br.readLine();line!=null;line=br.readLine()){
							sb.append(line);
						}
						toastS(sb.toString());
					}else{
						toastL("请求失败，错误代码为:"+resp.getStatusLine().getStatusCode());
						post.abort();
					}
				}catch(Exception e){
					toastS("请求失败："+e.getMessage());
					e.printStackTrace();
				}
			}
		});
		//封装请求
		fengzhuangrequestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HashMap<String,Object>args = new HashMap<String,Object>();
				//?mobile=13699265972&checkType=1
				args.put("mobile","13699265972");
				args.put("checkType","1");
				toastL(HttpApacheFac.doPost("http://192.168.2.254:8016/MobileCode.ashx", args));
			}
		});
		//json请求
		jsonRequestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					HashMap<String,Object>args = new HashMap<String,Object>();
					args.put("type","json");
					JSONObject jobj = new JSONObject(HttpApacheFac.doPost("http://192.168.2.254:8016/MobileCode.ashx",args));
					StringBuffer sb = new StringBuffer("得到的JSON数据\n");
					sb.append("状态："+jobj.getString("status")+"\n");
					sb.append("结果:"+jobj.getString("result")+"\n");
					toastS(sb.toString());
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		//cookies请求
		cookiesRequestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toastL(HttpApacheFac.getCookies("http://192.168.2.254:8016/MobileCode.ashx?mobile=13426188936&checkType=1"));
			}
		});
		//ip地址 请求
		ipRequestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					StringBuffer sb = new StringBuffer();
					int count = 0;
					for(Enumeration<NetworkInterface>niEn = NetworkInterface.getNetworkInterfaces();niEn.hasMoreElements();){
						NetworkInterface net = niEn.nextElement();
						for(Enumeration<InetAddress>iaEn = net.getInetAddresses();iaEn.hasMoreElements();){
							InetAddress addr = iaEn.nextElement();
							if(!addr.isLoopbackAddress()){
								sb.append((InetAddressUtils.isIPv4Address(addr.getHostAddress())?"IPv4地址:":"IPv6地址:")+addr.getHostAddress()+"\n");
								count++;
							}
						}
					}
					sb.append("数量："+count+"个");
					toastL(sb.toString());
				}catch(Exception e){
					toastL("发生异常");
					e.printStackTrace();
				}				
			}
		});
		//servelet简单请求
		serveletRequestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toastL(HttpApacheFac.doPost("http://192.168.2.254:8016/MobileCode.ashx?mobile=13426188936&checkType=1", null));
			}
		});
		serveletArrayButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				HashMap<String,Object>args = new HashMap<String,Object>();
//				args.put("parId","0");
				try{
					JSONArray ja = new JSONArray(HttpApacheFac.doPost("http://192.168.2.254:8016/areas.ashx?parId=0",null));
					if(ja!=null&&ja.length()>0){
						StringBuffer sb = new StringBuffer("JSON解析开始\n");
						JSONObject jobj = null;
						for(int i=0;i<ja.length();i++){
							jobj = (JSONObject) ja.get(i);
							sb.append("地区id:("+i+"):"+jobj.get("areaid")+"\n");
							sb.append("地区名称("+i+"):"+jobj.get("areaname")+"\n");
							sb.append("父级id:("+i+"):"+jobj.get("parid")+"\n");
						}
						sb.append("解析长度："+ja.length()+"");
						toastL(sb.toString());
					}else{
						toastL("数据为空");
					}
				}catch(Exception e){
					toastL("发生异常");
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
	}
}
