package com.lixueandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lixue.lixueandroid.R;

public class StartScanBarcodeActivity extends MyBaseActivity {
	private static final int PARAM_SCAN_CODE=100;
	private TextView showView;
	private Button startScan;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.acitivity_startscanbarcode);
		showView=(TextView) findViewById(R.id.text_show);
		startScan=(Button) findViewById(R.id.button_start_scan);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		startScan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(BarcodeScannerActivity.class,PARAM_SCAN_CODE);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==PARAM_SCAN_CODE&&resultCode==RESULT_OK){
			showView.setText(data.getStringExtra(BarcodeScannerActivity.RETURN_BARCODE_CONTENT));
		}
	}
	
}
