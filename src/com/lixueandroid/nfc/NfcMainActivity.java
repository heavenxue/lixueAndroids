package com.lixueandroid.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.MyBaseActivity;

/**
 * NFC功能首页
 * @author lixue
 *
 */
public class NfcMainActivity extends MyBaseActivity{
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	private int mCount = 0;

	private TextView nfcShowText;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_nfcmain);
		nfcShowText=(TextView) findViewById(R.id.text_nfc_show);
		//context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		 
//		 Create a generic PendingIntent that will be deliver
//		 to this activity. The NFC stack
//		 will fill in the intent with the details of the
//		discovered tag before delivering to
//		 this activity.
		//intent.FLAG_ACTIVITY_SINGLE_TOP  如果它已经运行于历史堆栈的顶部。，活动将不会启动
		mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		 
		// Setup an intent filter for all MIME based dispatches intent开始活动时，标记被发现标签上登记的具体技术和活动。
//		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED );
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
		throw new RuntimeException("fail", e);
		}
		mFilters = new IntentFilter[] {ndef,new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
		// Setup a tech list for all MifareClassic tags
		mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };

	}

	@Override
	protected void onResume() {
		super.onResume();
		if(getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)){
			nfcShowText.setText("Scan a tag");
			toastL("此手机支持NFC功能！");
			mAdapter.enableForegroundDispatch(this,mPendingIntent, mFilters, mTechLists);//启用前景调度给定的活动
		}else{
			toastL("此手机不支持NFC功能！");
			becauseExceptionFinishActivity();
			return;
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.i("Foreground dispatch","Discovered tag with intent: " + intent);
		nfcShowText.setText("Discovered tag "+ ++mCount + " with intent: " + intent);

	}

	@Override
	protected void onPause() {
		super.onPause();
		if(getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)){
			mAdapter.disableForegroundDispatch(this);
		}else{
			return;
		}
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}

}
