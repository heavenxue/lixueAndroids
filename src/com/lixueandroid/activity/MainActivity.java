package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.nfc.NfcMainActivity;

/**
 * 李雪Android主界面
 * @author lixue
 *
 */
public class MainActivity extends MyBaseActivity {

	private Button btnMyCustomButton;
	private Button btnMyTouchTab;
	private Button mp3Player;
	private Button Yaoyiyao;
	private Button testViewFlipper;
	private Button screenShot;
	private Button animationdrawable;
	private Button animation;
	private Button jsonButton;
	private Button barcodescannerButton;
	private Button listRefresh;
	private Button gridRefresh;
	private Button scrollRefresh;
	private Button nfcButton;
	private Button listimgButton;
	private Button gridimgButton;
	private Button galleryButton;
	private Button socketButton;
	private Button listenerButton;
	
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		btnMyCustomButton=(Button) findViewById(R.id.button_mycoustom_button);
		btnMyTouchTab=(Button) findViewById(R.id.button_mytouch_tab);
		mp3Player=(Button) findViewById(R.id.button_mp3_player);
		Yaoyiyao=(Button) findViewById(R.id.button_yaoyiyao);
		testViewFlipper=(Button) findViewById(R.id.button_viewflipper);
		screenShot=(Button) findViewById(R.id.button_screenshot);
		animationdrawable=(Button) findViewById(R.id.button_animationdrawable);
		animation=(Button) findViewById(R.id.button_animation);
		jsonButton=(Button) findViewById(R.id.button_json);
		barcodescannerButton=(Button) findViewById(R.id.button_barcodescanner);
		listRefresh=(Button) findViewById(R.id.button_refresh_listview);
		gridRefresh=(Button) findViewById(R.id.button_refresh_gridview);
		scrollRefresh=(Button) findViewById(R.id.button_refresh_scrollview);
		nfcButton=(Button) findViewById(R.id.button_Nfc);
		listimgButton=(Button) findViewById(R.id.button_listImageLoader);
		gridimgButton=(Button) findViewById(R.id.button_gridImageLoader);
		socketButton=(Button) findViewById(R.id.button_Socket);
		galleryButton=(Button) findViewById(R.id.button_galleryimgloader);
		listenerButton=(Button) findViewById(R.id.button_listener);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		btnMyCustomButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(MyCustomerImgButtonActivity.class);
			}
		});
		btnMyTouchTab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(MyTouchTabActivity.class);
			}
		});
		mp3Player.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(Mp3PlayerMainActivity.class);
			}
		});
		Yaoyiyao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(YaoyiyaoActivity.class);
			}
		});
		testViewFlipper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(ViewFilpperActivity.class);
			}
		});
		screenShot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(ScreenShotActivity.class);
			}
		});
		animationdrawable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(AnimationDrawableActivity.class);
			}
		});
		animation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(AnimationActivity.class);
			}
		});
		jsonButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(JsonActivity.class);
			}
		});
		barcodescannerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(StartScanBarcodeActivity.class);
			}
		});
		listRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(TestListViewActivity.class);
			}
		});
		gridRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(TestGridViewActivity.class);	
			}
		});
		scrollRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(TestScrollViewActivity.class);	
			}
		});
		nfcButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(NfcMainActivity.class);
			}
		});
		listimgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(ListImgLoaderActivity.class);
			}
		});
		gridimgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(GridImgActivity.class);
			}
		});
		socketButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(SocketActivity.class);
//				Intent intent=new Intent();
//				intent.setClass(getBaseContext(),TcpClientService.class);
//				startService(intent);
			}
		});
		galleryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(GalleryImgActivity.class);
			}
		});
		listenerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(ListenerActivity.class);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		getMyApplication().getTheme();
	}
}
