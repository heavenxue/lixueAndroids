package com.lixueandroid.activity;

import me.xiaopan.easyandroid.app.BaseActivity;
import me.xiaopan.easyandroid.util.CameraManager;
import me.xiaopan.easyandroid.util.CameraManager.CameraCallback;
import me.xiaopan.easyandroid.util.CameraUtils;
import me.xiaopan.easyandroid.util.barcode.Decoder;
import me.xiaopan.easyandroid.util.barcode.Decoder.DecodeListener;
import me.xiaopan.easyandroid.util.barcode.ScanFrameView;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.lixue.lixueandroid.R;

/**
 * 扫描二维码
 * @author Administrator
 *
 */
public class BarcodeScannerActivity extends BaseActivity implements  CameraCallback,PreviewCallback,ResultPointCallback,DecodeListener{
	public static final String  STATE_FLASH_CHECKED="STATE_FLASH_CHECKED";//闪光灯按钮是否被打开
	public static final String RETURN_BARCODE_CONTENT="RETURN_BARCODE_CONTENT";//返回二维码的内容
	
	private CameraManager cameraManager;//相机管理器
	private RefreshScanFrameRunnable refreshScanFrameRunnable;//扫描框处理器
	private Handler handler;//处理器
	private SoundPool soundPool;//声音池
	private int beepId;//哗哗的音效
	private Decoder decoder;//解码器
	private boolean allowDecode;	//允许解码（当解码成功后要停止解码，只有点击屏幕后才会再次开始解码）
	
	private SurfaceView barcodeSurfaceView;
	private ScanFrameView scanFrameView;
	private ToggleButton flashLight;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_barcodescanner);
		barcodeSurfaceView=(SurfaceView) findViewById(R.id.surface_barcodeScanner);
		scanFrameView=(ScanFrameView) findViewById(R.id.scanningFrame_barcodeScanner);
		flashLight=(ToggleButton) findViewById(R.id.checkBox_barcodeScanner_flash);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		//当点击的是开始对焦
		scanFrameView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startDecode();//开始解码
			}
		});
		//当闪光灯打开时
		flashLight.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setEnableTorckFlashMode(isChecked);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		if(savedInstanceState!=null){
			flashLight.setChecked(savedInstanceState.getBoolean(STATE_FLASH_CHECKED));
		}
		//初始化相机 
		cameraManager=new CameraManager(this, barcodeSurfaceView.getHolder(), this); 
		cameraManager.setFocusIntervalTime(3000);
		cameraManager.setDisplayOrientation(90);
		
		//初始化扫描框的处理器
		handler=new Handler();
		refreshScanFrameRunnable=new RefreshScanFrameRunnable();
		
		//设置扫描框的描边宽度
		scanFrameView.setStrokeWidth(2);
		
		//初始化音效
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		beepId = soundPool.load(getBaseContext(),R.raw.beep, 100);
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		cameraManager.openBackCamera();
		setEnableTorckFlashMode(flashLight.isChecked());
	}

	@Override
	protected void onPause() {
		super.onPause();
		cameraManager.release();
	}
	
	@Override
	protected void onDestroy() {
		if(cameraManager!=null){
			cameraManager.release();
		}
		cameraManager=null;
		soundPool = null;
		decoder = null;
		refreshScanFrameRunnable = null;
		handler = null;		
		super.onDestroy();
	}
	
	 //为了防止万一程序被销毁的风险，这个方法可以保证重要数据的正确性
    //不写这个方法并不意味着一定出错，但是一旦遇到了一些非常奇怪的数据问题的时候
    //可以看看是不是由于某些重要的数据没有保存，在程序被销毁时被重置
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(STATE_FLASH_CHECKED, flashLight.isChecked());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onDecodeSuccess(Result result, Bitmap barcodeBitmap) {
		stopDecode();//停止解码
		playSound();//播放音效
		playVibrator();//发出震动提示
		handleResult(result, barcodeBitmap);//处理结果
	}

	@Override
	public void onDecodeFail() {
		if(cameraManager != null){
			cameraManager.autoFocus();//继续对焦
		}
	}

	@Override
	public void foundPossibleResultPoint(ResultPoint arg0) {
		scanFrameView.addPossibleResultPoint(arg0);
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		//如果允许解码，就尝试解码
				if (allowDecode) {
					decoder.tryDecode(data);
				}
	}


	/*
	 *  当自动对焦时
	 */
	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		//如果没有对好就继续对
		if (!success) {
			cameraManager.autoFocus();
		}
	}

	/* 
	 * 打开相机失败
	 */
	@Override
	public void onOpenCameraException(Exception e) {
		toastL(R.string.toast_cameraOpenFailed);
		becauseExceptionFinishActivity();
	}

	/*
	 *  开始预览前
	 */
	@Override
	public void onStartPreview() {
		startDecode();//开始解码
	}

	@Override
	public void onStopPreview() {
		stopDecode();//停止解码
	}
	
	/**
	 * 开始解码
	 */
	private void startDecode(){
		if(decoder != null){
			allowDecode = true;//设置允许解码
			startRefreshScanFrame();//开始刷新扫描框
			cameraManager.autoFocus();// 自动对焦
		}
	}
	
	/**
	 * 停止解码
	 */
	private void stopDecode(){
		if(decoder != null){
			stopRefreshScanFrame();//停止刷新扫描框
			allowDecode = false;
			decoder.pause();//停止解码器
		}
	}
	
	/**
	 * 开始刷新扫描框
	 */
	public void startRefreshScanFrame(){
		handler.post(refreshScanFrameRunnable);
	}
	
	/**
	 * 停止刷新扫描框
	 */
	public void stopRefreshScanFrame(){
		handler.removeCallbacks(refreshScanFrameRunnable);
	}
	
	/**
	 * 处理结果
	 * @param result
	 * @param barcodeBitmap
	 */
	private void handleResult(Result result, Bitmap barcodeBitmap){
		scanFrameView.drawResultBitmap(barcodeBitmap);
		getIntent().putExtra(RETURN_BARCODE_CONTENT, result.getText());
		setResult(RESULT_OK, getIntent());
		finishActivity();
	}
	
	/**
	 * 播放音效
	 */
	private void playSound(){
		//播放音效
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
			float volume = (float) (((float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / 15) / 3.0);
			soundPool.play(beepId, volume, volume, 100, 0, 1);
		}
	}
	
	/**
	 * 震动
	 */
	private void playVibrator(){
		((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(200);//发出震动提示
	}
	
	/**
	 * 扫描框的处理
	 *
	 */
	private class RefreshScanFrameRunnable implements Runnable{
		@Override
		public void run() {
			if(scanFrameView != null && handler != null){
				scanFrameView.refresh();
				handler.postDelayed(refreshScanFrameRunnable, 50);
			}
		}
	}
	
	/**
	 * 设置是否激活常亮闪光模式
	 * @param enable
	 */
	private void setEnableTorckFlashMode(boolean enable){
		if(cameraManager != null){
			if(enable){
				if(CameraUtils.isSupportFlashMode(cameraManager.getCamera(), Camera.Parameters.FLASH_MODE_TORCH)){
					cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				}else{
					toastL(R.string.toast_barcodeScanner_notSupport);
					flashLight.setChecked(false);
				}
			}else{
				cameraManager.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			}
		}
	}

	@Override
	public void onInitCamera(Camera camera, Parameters cameraParameters) {
		//设置预览回调
		camera.setPreviewCallback(this);
		
		//如果解码器尚未创建的话，就创建解码器并设置其监听器
		if(decoder == null){
			decoder = new Decoder(getBaseContext(), camera.getParameters(), scanFrameView);
			decoder.setResultPointCallback(this);	//设置可疑点回调
			decoder.setDecodeListener(this);	//设置解码监听器
		}		
	}
//	/* 
//	 * 初始化相机 
//	 */
//	@Override
//	public void onInitCamera(Camera camera) {
//		//设置相机回调
//		camera.setPreviewCallback(this);
//		//如果解码器尚未创建的话，就创建解码器并设置其监听器
//		if(decoder==null){
//			decoder=new Decoder(getBaseContext(), camera.getParameters(), scanFrameView);
//			 decoder.setDecodeListener(this);//设置解码监听器回调
//			 decoder.setResultPointCallback(this);//设置可疑点回调
//		}
//	}
}
