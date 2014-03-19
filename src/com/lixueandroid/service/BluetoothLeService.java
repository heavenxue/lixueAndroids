package com.lixueandroid.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.lixueandroid.domain.GattAttributes;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BluetoothLeService extends Service{
	 private final static String TAG = BluetoothLeService.class.getSimpleName();
	 
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;//默认未连接
 
    /**
     * 未连接
     */
    private static final int STATE_DISCONNECTED = 0;
    /**
     * 连接中
     */
    private static final int STATE_CONNECTING = 1;
    /**
     * 已连接上
     */
    private static final int STATE_CONNECTED = 2;
 
    public final static String ACTION_GATT_CONNECTED = "com.lixueandroid.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED ="com.lixueandroid.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED ="com.lixueandroid.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE ="com.lixueandroid.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA ="com.lixueandroid.EXTRA_DATA";
    private final IBinder mBinder = new LocalBinder();
    /**
     * 这是用来唯一标识信息的字符串ID的标准化128-bit格式,心率计算
     */
    public final static UUID UUID_HEART_RATE_MEASUREMENT =UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG);
	
    /**
     * Gatt回调
     */
    private BluetoothGattCallback mGattCallback=new BluetoothGattCallback() {
    	//监听连接状态
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,int newState) {
			String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "已连接到GATT服务器.");
                // 连接成功后，尝试去发现服务。
                Log.i(TAG, "尝试启动发现的服务:" +mBluetoothGatt.discoverServices());
 
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "未连接上GATT服务器.");
                broadcastUpdate(intentAction);
            }
		}
		//监听是否发现了服务
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
		}
		//监听已读到的类型
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
		}
		//监听到已改变的类型
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}
	};
	
	/**
	 *广播更新 
	 * @param action
	 */
	private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
	/**
	 * 广播更新
	 * @param action
	 * @param characteristic
	 */
	private void broadcastUpdate(final String action,final BluetoothGattCharacteristic characteristic){
		final Intent intent = new Intent(action);
        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
		Log.i(TAG, "uuid的值为:"+characteristic.getUuid());
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("接收到的心率值是:Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            //对于不是低功耗的蓝牙设备，写入十六进制格式的数据.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
	}
	public class LocalBinder extends Binder {
	       public BluetoothLeService getService(){
	    	   return BluetoothLeService.this;
	       }
	}
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	@Override
	public boolean onUnbind(Intent intent) {
		// After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
		return super.onUnbind(intent);
	}
	/**
     * I初始化一个引用到本地蓝牙适配器。
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through  BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
 
        return true;
    }
    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     * LE设备作为服务端，应用程序作为客户端去连接LE设备
     *
     * @param address The device address of the destination device.
     *
     * @return 如果返回true,如果成功启动连接。连接结果 通过异步报道BluetoothGattCallback回调函数的onConnectionStateChange回调
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
 
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
 
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // 我们要直接连接到设备上，所以我们设置了自动连接参数设置为false。
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }
    /**
     * 断开现有连接或取消一个挂起的连接,断开结果会从BluetoothGattCallback回调函数的onConnectionStateChange回调
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }
 
    /**
     * 使用给定的BLE设备后，应用程序必须调用此方法，以确保资源被正确释放。
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
 
    /**
     *要求读一个给定的BluetoothGattCharacteristic类型。读取结果报告异步通过BluetoothGattCallback的函数onCharacteristicRead回调。
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }
 
    /**
     * 是否可以通知被给出的设备类型
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
 
        // This is specific to Heart Rate Measurement.
        if (UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG2).equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG2));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }
 
    /**
     * 检索连接的设备上支持GATT的服务列表。在BluetoothGatt的discoverServices（）后，才会调用成功。
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
    	List<BluetoothGattService> ss=new ArrayList<BluetoothGattService>();
        if (mBluetoothGatt == null) return null;
        BluetoothGattService s= mBluetoothGatt.getService(UUID_HEART_RATE_MEASUREMENT);
        if (s!=null) {
			ss.add(s);
		}
//        return mBluetoothGatt.getServices();
        return ss;
    }
}
