package com.lixueandroid.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.xiaopan.easyandroid.app.BaseActivity;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;

import com.lixue.lixueandroid.R;

/**
 * 实现类似微信的摇一摇效果
 * @author Administrator
 *
 */
public class YaoyiyaoActivity extends BaseActivity implements SensorEventListener {
	// Sensor管理器
	private SensorManager mSensorManager = null;

	// 震动
	private Vibrator mVibrator = null;

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_yaoyiyao);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {

	}

	@Override
	public void onInitData(Bundle savedInstanceState) {

	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			if (Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math.abs(values[2]) > 14) {
				mVibrator.vibrate(100);
				TextView tv1 = (TextView) findViewById(R.id.text_yaoyiyao);
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
				tv1.setText(f.format(new Date()) + "手机摇动了...");
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
