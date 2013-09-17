package com.lixueandroid.activity.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 调用此服务的代码应该为：
 * 	Intent intent=new Intent();
	intent.setClass(getBaseContext(), DownLoadService.class);
	startService(intent);
 * @author lixue
 *
 */
public class TcpClientService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 生成一个TcpClient线程
		SocketThread socketThread=new SocketThread();
		// 启动新线程
		Thread th = new Thread(socketThread);
		th.start();
		return super.onStartCommand(intent, flags, startId);
	}
	public class SocketThread implements Runnable{
		private static final String HOST = "192.168.2.106"; // 主机地址（服务端所在pc的ip地址）
		private static final int PORT = 8058; // 端口号
		private Socket socket = null; // 套接字
		private BufferedReader in = null;
		private PrintWriter out = null;
		private String content = "";
		private SocketAddress socketAddress;
		@SuppressLint("ShowToast")
		@Override
		public void run() {
			try{
				socket = new Socket();
				socketAddress = new InetSocketAddress(HOST, PORT);
				socket.connect(socketAddress, 1000);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				while (true) {
					if (socket.isConnected()) {
						if (!socket.isInputShutdown()) {
							if ((content = in.readLine()) != null) {
								content += "\n";
								//这里的content是json字符串，要解析的
//								try {
//									JSONObject jsonObject = new JSONObject(content); 
//									JSONArray jsonArray = jsonObject.getJSONArray("");
//									List<BroadcastData> broadcastdatalist=new ArrayList<BroadcastData>();
//									for(int i=0;i<jsonArray.length();i++){
//										broadcastdatalist.add(parseBrroadcastData(jsonObject));
//									}
//								} catch (JSONException e) {
//									e.printStackTrace();
//								} 
								//以广播的形式发送出去
//						private BroadcastData parseBrroadcastData(JSONObject jsonObject) throws JSONException{
//							BroadcastData broadcastData =new BroadcastData();
//							broadcastData.setCapBarcode(jsonObject.getString(""));
//							return broadcastData;		
//						}
							Intent intent = new Intent();    
					        intent.setAction("com.lixueandroid.broadcast.receiver.DEMO_ACTION");//发出自定义广播
					        intent.putExtra("content", content);
					        sendBroadcast(intent);
							} else {
								
						}
					}
				} else {
					// 如果没有连接上，那么要重试连接
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.i("", "");
			Log.w("", "登录失败：" + ex.getMessage()+";服务端没有打开，请先打开服务端才可!");
			Toast.makeText(getBaseContext(), "登录失败：" + ex.getMessage()+";服务端没有打开，请先打开服务端才可!", Toast.LENGTH_LONG);
		}finally{
			disConnectToServer();
		}
		}
	
	/**
	 * 关闭与服务端的连接
	 */
	public void disConnectToServer() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
}
