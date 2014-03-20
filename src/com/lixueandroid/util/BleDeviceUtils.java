package com.lixueandroid.util;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

public class BleDeviceUtils {
	private BluetoothAdapter mBluetoothAdapter;
    /**
     * 是否正在扫描中
     */
    public boolean mScanning;
    private Handler mHandler;

    // 10秒后停止查找搜索.
    private static final long SCAN_PERIOD = 10000;
    private OnScanListener onScanListener;
    private Context context;
    
    public BleDeviceUtils(Context context,OnScanListener onScanListener,Handler handler){
    	this.onScanListener=onScanListener;
    	this.context=context;
    	this.mHandler=handler;
    	initBlueToothAdapter();
    }
    /**
     * 初始化蓝牙适配器
     */
    public void initBlueToothAdapter(){
    	final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }
    /**
     * 检查手机上是否带有蓝牙设备
     * @return
     */
    public boolean checkIsHasAdapterNull(){
        if (mBluetoothAdapter == null) {
        	return false;
        }else{
        	return true;
        }
    }
    /**
     * 检查是否支持BLE蓝牙设备
     * @return
     */
    public boolean checkIsSupportBleDevice(){
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
        	return false;
        }else
        	return true;
    }
	/**
	 * 扫描BLE设备
	 * @param enable
	 */
	public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        	if (onScanListener!=null) {
				onScanListener.onScan(device);
			}
        }
    };
}
