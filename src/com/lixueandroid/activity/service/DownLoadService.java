package com.lixueandroid.activity.service;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fileuplaod.unity.DownLoadUnity;
import com.lixueandroid.activity.Mp3PlayerRemoteActivity;
import com.lixueandroid.activity.domain.Mp3Info;

public class DownLoadService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 从Intent中将mp3info对象取出
		Mp3Info info = (Mp3Info) intent.getSerializableExtra("mp3info");
		// 生成一个下载线程，并将mp3info对象作为参数传递到线程对象当中
		DownLoadThread dlt = new DownLoadThread(info);
		// 启动新线程
		Thread th = new Thread(dlt);
		th.start();
		return super.onStartCommand(intent, flags, startId);
	}

	class DownLoadThread implements Runnable {
		private Mp3Info mp3info;

		public DownLoadThread(Mp3Info mp3info) {
			this.mp3info = mp3info;
		}

		@Override
		public void run() {
			int finish = DownLoadUnity.downFile("http://192.168.1.156/expocube/" + mp3info.getMp3Name(), "/mp3", mp3info.getMp3Name());
			String MessageShow;
			if (finish == -1) {
				MessageShow = "您已下载过";
			} else if (finish == 0) {
				MessageShow = "下载失败";
			} else {
				MessageShow = "下载成功";
			}
			// 使用Notifycation提示客户下载结果
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Notification n = new Notification(R.drawable.stat_notify_chat, MessageShow, System.currentTimeMillis());
			n.flags = Notification.FLAG_AUTO_CANCEL;
			Intent i = new Intent(getBaseContext(), Mp3PlayerRemoteActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			// PendingIntent
			PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),com.lixue.lixueandroid.R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);
			n.setLatestEventInfo(getBaseContext(), "hollo there", MessageShow, contentIntent);
			nm.notify(com.lixue.lixueandroid.R.string.app_name, n);
		}
	}
}
