package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.service.BluetoothLeService;

public class DeviceControlActivity extends Activity implements OnClickListener{
	private final static String TAG = DeviceControlActivity.class.getSimpleName();
	 
    /**
     * BLE设备名称
     */
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    /**
     * BLE设备地址
     */
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
 
    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private List<BluetoothGattCharacteristic> mGattCharacteristics =new ArrayList<BluetoothGattCharacteristic>();//BLE设备所有服务类型
    private boolean mConnected = false;
 
    private Button receiveButton;
    private Button sendButton;
    
 
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
 
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder)service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }
 
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
 
    // 处理服务发射的各种活动
    // ACTION_GATT_CONNECTED: 连接上了 GATT server.
    // ACTION_GATT_DISCONNECTED: 未连接到GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: 未发现GATT services.
    // ACTION_DATA_AVAILABLE: 从设备接收到的数据。这可以是读或通知操作的结果。
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };
 
    private void clearUI() {
        mDataField.setText(R.string.no_data);
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
 
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
 
        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);
        receiveButton=(Button) findViewById(R.id.button_recieve);
        sendButton=(Button) findViewById(R.id.button_send);
        receiveButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        if (getActionBar()!=null) {
        	getActionBar().setTitle(mDeviceName);
        	getActionBar().setDisplayHomeAsUpEnabled(true);
		}
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
 
    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }
 
    private void displayData(String data) {
        if (data != null) {
            mDataField.setText(data);
        }
    }
 
    //加载服务,得出已知的服务,并从服务中打开相应uuid下的服务，接收ble设备服务端消息
    private void displayGattServices(BluetoothGattService gattServices) {
        if (gattServices == null) return;
        mGattCharacteristics = gattServices.getCharacteristics();
        if (mGattCharacteristics != null) {
			for (BluetoothGattCharacteristic characteristic : mGattCharacteristics) {
				final int charaProp = characteristic.getProperties();
				System.out.println("charaProp = " + charaProp + ",UUID = "+ characteristic.getUuid().toString());
				if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
					if (characteristic.getUuid().toString().equals("d3e60004-8f36-40d6-b2d5-c5d9f5e81869")) {
						System.out.println("enable notification");
//						mNotifyCharacteristic = characteristic;
						mBluetoothLeService.setCharacteristicNotification(characteristic, true);
					}
				}
			}
		}
 
    }
 
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_recieve:
			if (mGattCharacteristics != null) {
				for (BluetoothGattCharacteristic characteristic : mGattCharacteristics) {
					final int charaProp = characteristic.getProperties();
					System.out.println("charaProp = " + charaProp + ",UUID = "+ characteristic.getUuid().toString());
					if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {

						if (characteristic.getUuid().toString().equals("d3e60004-8f36-40d6-b2d5-c5d9f5e81869")||characteristic.getUuid().toString().equals("d3e60000-8f36-40d6-b2d5-c5d9f5e81869")) {
							System.out.println("enable notification");
//							mNotifyCharacteristic = characteristic;
							mBluetoothLeService.setCharacteristicNotification(characteristic, true);
						}
					}
				}
			}
			break;
		case R.id.button_send:
			if (mGattCharacteristics != null) {
				for (BluetoothGattCharacteristic characteristic : mGattCharacteristics) {
					final int charaProp = characteristic.getProperties();
//					Random r = new Random();
//
//					if (characteristic.getUuid().toString().equals("d3e60000-8f36-40d6-b2d5-c5d9f5e81869")) {
//						int time= 0;
//						while((time=r.nextInt(9))<=0){
//							
//						}
//						
//						String data = time+","+"1,,,,,";
//						Log.d(TAG, "0写:"+data);
//						characteristic.setValue(data.getBytes());
//						mBluetoothLeService.wirteCharacteristic(characteristic);
//					}				
//					if (characteristic.getUuid().toString().equals("d3e60001-8f36-40d6-b2d5-c5d9f5e81869")) {
//						int R = r.nextInt(255);
//						int G = r.nextInt(255);
//						int B = r.nextInt(255);
//						int BB = r.nextInt(100);
//						String data = R + "," + G + "," + B + "," + BB;
//						while (data.length() < 18) {
//							data += ",";
//						}
//						System.out.println(data);
//						Log.d(TAG, "1写:"+data);
//						characteristic.setValue(data.getBytes());
//						mBluetoothLeService.wirteCharacteristic(characteristic);
//					}
//					if (characteristic.getUuid().toString().equals("d3e60003-8f36-40d6-b2d5-c5d9f5e81869")) {
//						int R = r.nextInt(255);
//						int G = r.nextInt(255);
//						int B = r.nextInt(255);
//						int BB = r.nextInt(100);
//						String data = R + "," + G + "," + B + "," + BB;
//						while (data.length() < 18) {
//							data += ",";
//						}
//						System.out.println("RT");
//						Log.d(TAG, "3写:"+data);
//						characteristic.setValue("RT".getBytes());
//						mBluetoothLeService.wirteCharacteristic(characteristic);
//					}
					if (characteristic.getUuid().toString().equals("d3e60005-8f36-40d6-b2d5-c5d9f5e81869")) {
						characteristic.setValue("李雪");
						mBluetoothLeService.wirteCharacteristic(characteristic);
						System.out.println("send 李雪");
						Log.d(TAG, "5写:"+"send 李雪");
					}else {
						if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
							// If there is an active notification on a
							// characteristic, clear
							// it first so it doesn't update the data field on the
							// user interface.
//							if (mNotifyCharacteristic != null) {
//								mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
//								mNotifyCharacteristic = null;
//							}
							mBluetoothLeService.readCharacteristic(characteristic);
			
						}
					}
				}
			}
			break;

		default:
			break;
		}
	}
}
