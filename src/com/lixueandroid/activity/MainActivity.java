package com.lixueandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.nfc.NfcMainActivity;
import com.lixueandroid.util.ScreenLightManager;

/**
 * 李雪Android主界面
 * 
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
	private Button setlightButton;
	private Button locationButton;
	private Button actionbarButton;
	private Button waterWaveButton;
	private Button pageturnButton;
	private Button drawButton;
	private Button toshareButton;
	private Button animationButton;
	private Button liubianxingButton;
	private Button daolanButton;
	private Button clearEditTextButton;
	private Button testHeaderButton;
	private Button showViewButton;
	private Button mylistviewButton;
	private Button gesturesButton;
	private Button mapButton;

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		btnMyCustomButton = (Button) findViewById(R.id.button_mycoustom_button);
		btnMyTouchTab = (Button) findViewById(R.id.button_mytouch_tab);
		mp3Player = (Button) findViewById(R.id.button_mp3_player);
		Yaoyiyao = (Button) findViewById(R.id.button_yaoyiyao);
		testViewFlipper = (Button) findViewById(R.id.button_viewflipper);
		screenShot = (Button) findViewById(R.id.button_screenshot);
		animationdrawable = (Button) findViewById(R.id.button_animationdrawable);
		animation = (Button) findViewById(R.id.button_animation);
		jsonButton = (Button) findViewById(R.id.button_json);
		barcodescannerButton = (Button) findViewById(R.id.button_barcodescanner);
		listRefresh = (Button) findViewById(R.id.button_refresh_listview);
		gridRefresh = (Button) findViewById(R.id.button_refresh_gridview);
		scrollRefresh = (Button) findViewById(R.id.button_refresh_scrollview);
		nfcButton = (Button) findViewById(R.id.button_Nfc);
		listimgButton = (Button) findViewById(R.id.button_listImageLoader);
		gridimgButton = (Button) findViewById(R.id.button_gridImageLoader);
		socketButton = (Button) findViewById(R.id.button_Socket);
		galleryButton = (Button) findViewById(R.id.button_galleryimgloader);
		listenerButton = (Button) findViewById(R.id.button_listener);
		setlightButton = (Button) findViewById(R.id.button_setlight);
		locationButton = (Button) findViewById(R.id.button_photo);
		actionbarButton = (Button) findViewById(R.id.button_actionbar);
		waterWaveButton = (Button) findViewById(R.id.button_share);
		pageturnButton = (Button) findViewById(R.id.button_pageturn);
		drawButton=(Button) findViewById(R.id.button_draw);
		toshareButton=(Button) findViewById(R.id.button_toshare);
		animationButton=(Button) findViewById(R.id.button_myanimation);
		liubianxingButton=(Button) findViewById(R.id.button_liubianxing);
		daolanButton=(Button) findViewById(R.id.button_exibitionGuid);
		clearEditTextButton=(Button) findViewById(R.id.button_clearEditText);
		testHeaderButton=(Button) findViewById(R.id.button_testheaderview);
		showViewButton=(Button) findViewById(R.id.button_showView);
		mylistviewButton=(Button) findViewById(R.id.button_mylistview);
		gesturesButton=(Button) findViewById(R.id.button_gesturedetector);
		mapButton=(Button) findViewById(R.id.button_map);
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
				// Intent intent=new Intent();
				// intent.setClass(getBaseContext(),TcpClientService.class);
				// startService(intent);
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
		setlightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ScreenLightManager screenLightManager = new ScreenLightManager();
				screenLightManager.saveScreenBrightness(50,
						getContentResolver());
				screenLightManager.setScreenBrightness(50, getWindow());
			}
		});
		locationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(PhotoActivity.class);
			}
		});
		actionbarButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		waterWaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(WaterWaveActivity.class);
			}
		});
		pageturnButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(PageTurnActivity.class);
			}
		});
		drawButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(AllDrawActivity.class);
			}
		});
		toshareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(ShareActivity.class);
			}
		});
		animationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(myAnimationActivity.class);
			}
		});
		liubianxingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			startActivity(LiuBianXingActivity.class);	
			}
		});
		daolanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(GuidActivity.class);
			}
		});
		clearEditTextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			startActivity(ClearEditTextActivity.class);	
			}
		});
		testHeaderButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(TestActivity.class);
			}
		});
		showViewButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(ShowView.class);
			}
		});
		mylistviewButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(MyCustomListView.class);
			}
		});
		gesturesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(GesturesActivity.class);
			}
		});
		mapButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(MyMapActivity.class);	
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {

	}
}
