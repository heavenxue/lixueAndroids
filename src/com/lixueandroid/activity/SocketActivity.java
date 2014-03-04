package com.lixueandroid.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;

public class SocketActivity extends MyBaseActivity implements Runnable {
	private TextView tv_msg = null;
	private EditText ed_msg = null;
	private Button btn_send = null;
	private static final String HOST = "192.168.2.106"; // 主机地址（服务端所在pc的ip地址）
	private static final int PORT = 8058; // 端口号
	private Socket socket = null; // 套接字
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String content = "";
	private SocketAddress socketAddress;

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.acitivity_socket);
		tv_msg = (TextView) findViewById(R.id.TextView);
		ed_msg = (EditText) findViewById(R.id.edit_content);
		btn_send = (Button) findViewById(R.id.button_send);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = ed_msg.getText().toString();
				if (socket.isConnected()) {
					if (!socket.isOutputShutdown()) {
						out.println(msg);
						toastL("发送成功："+msg);
					}
				} else {
					// 如果没有连接上，那么要重试连接
					try {
						socket.connect(socket.getLocalSocketAddress(), 1000);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		new Thread(SocketActivity.this).start();
	}

	@Override
	public void run() {
		try {
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
							mHandler.sendMessage(mHandler.obtainMessage());
						} else {

						}
					}
				} else {
					// 如果没有连接上，那么要重试连接
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			toastL("登录失败：" + ex.getMessage()+";服务端没有打开，请先打开服务端才可!");
		}finally{
			disConnectToServer();
		}
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tv_msg.setText(tv_msg.getText().toString() + content);
			//这里的content是json字符串，要解析的
//			try {
//				JSONObject jsonObject = new JSONObject(content); 
//				JSONArray jsonArray = jsonObject.getJSONArray("");
//				List<BroadcastData> broadcastdatalist=new ArrayList<BroadcastData>();
//				for(int i=0;i<jsonArray.length();i++){
//					broadcastdatalist.add(parseBrroadcastData(jsonObject));
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			} 
			//以广播的形式发送出去
		}
	};
//	private BroadcastData parseBrroadcastData(JSONObject jsonObject) throws JSONException{
//		BroadcastData broadcastData =new BroadcastData();
//		broadcastData.setCapBarcode(jsonObject.getString(""));
//		return broadcastData;		
//	}
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
