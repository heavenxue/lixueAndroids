package com.lixueandroid.activity.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DemoReceiver extends BroadcastReceiver{
	public static final String TAG="lixueBroadcastReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, intent.getStringExtra("content"));
	}

}
