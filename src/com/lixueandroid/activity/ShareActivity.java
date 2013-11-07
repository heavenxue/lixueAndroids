package com.lixueandroid.activity;

import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lixue.lixueandroid.R;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socom.Log;

/**
 * 分享
 * @author lixue
 *
 */
public class ShareActivity extends MyBaseActivity{
	private Button shareButton;
	
	//分享开始
	private UMSocialService mController ;
	private IWXAPI api;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_share);
		shareButton=(Button) findViewById(R.id.button_share);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isNetworkAvailable()){
					//设置分享内容
//					final String shareContent = contentTextView.getText().toString();
				    mController.setShareContent("分享的文字");
					// 添加新浪和QQ空间的SSO授权支持
					mController.getConfig().setSsoHandler(new SinaSsoHandler());
					//添加腾讯微博SSO支持
					mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
					//只显示授权区域
					mController.openUserCenter(getBaseContext(),SocializeConstants.FLAG_USER_CENTER_HIDE_LOGININFO);
					//分享给qq空间
					mController.getConfig().setSsoHandler( new QZoneSsoHandler(ShareActivity.this) );
					//增加微信平台
					addWXPlatform();
					//平台中的顺序也可以自定义
					mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,SHARE_MEDIA.TENCENT,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.NULL);
					//去掉不想要的平台
					mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,SHARE_MEDIA.DOUBAN,SHARE_MEDIA.QQ,SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL);
					//快速分享
					mController.openShare(ShareActivity.this, false);
				}
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
				//微信相关
				api = WXAPIFactory.createWXAPI(this, "wx7ab548e20d376c1c");
				mController = UMServiceFactory.getUMSocialService("com.umeng.share.coo", RequestType.SOCIAL) ;
				mController.getConfig().setSsoHandler(new SinaSsoHandler());
		        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		        mController.getConfig().setSsoHandler(new QZoneSsoHandler(ShareActivity.this));
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (api != null) {// for weixin call back
			setIntent(intent);
			api.handleIntent(intent, new IWXAPIEventHandler() {
				@Override
				public void onResp(BaseResp arg0) {
					SendAuth.Resp resp = (SendAuth.Resp) arg0;
					System.out.println(resp.userName);
				}

				@Override
				public void onReq(BaseReq arg0) {}
			});
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		String result = "null";
		try {
			Bundle b = data.getExtras();
			Set<String> keySet = b.keySet();
			if(keySet.size() > 0)
				result = "result size:"+keySet.size();
			for(String key : keySet){
				Object object = b.get(key);
				Log.d("TestData", "Result:"+key+"   "+object.toString());
			}
		}
		catch (Exception e) {

		}
		Log.d("TestData", "onActivityResult   " + requestCode + "   " + resultCode + "   " + result);
		
	    UMSocialService controller = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode) ;
		if( ssoHandler != null ){
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}
	 /**
     * @功能描述 :  添加微信平台分享
     */
    private void addWXPlatform(){
    	// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//		String appId = "wx649d2ffb608fe5aa";
		String appId = "wx7ab548e20d376c1c";
		// 微信图文分享必须设置一个url 
//		String contentUrl = "";
		api=WXAPIFactory.createWXAPI(getBaseContext(), appId);
		api.registerApp(appId);
		// 添加微信平台
//		mController.getConfig().supportWXPlatform(NewsDetailActivity.this, appId,contentUrl);
		// 要发送的图片内容,只能为本地文件， 不能为url
//		UMImage mUMImgBitmap = new UMImage(NewsDetailActivity.this, "http://www.umeng.com/images/pic/banner_module_social.png");
//		mUMImgBitmap.setTitle("分享到微信");
//		 target url 必须填写
//		mUMImgBitmap.setTargetUrl( contentUrl ) ;
//		mController.setShareMedia(mUMImgBitmap);
		// 设置分享文字内容
		mController.setShareContent("要分享的内容");
//		mController.setShareContent("我的分享");
//				UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
//				uMusic.setAuthor("zhangliyong");
//				uMusic.setTitle("天籁之音");
////				uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
//				uMusic.setThumb(mUMImgBitmap);
		//
		// 视频分享
//				UMVideo umVedio = new UMVideo(
//						"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
////				umVedio.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
//				umVedio.setThumb(mUMImgBitmap);
//				umVedio.setTitle("友盟社会化组件视频");
		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
		mController.getConfig().supportWXPlatform(ShareActivity.this,appId, "");     
		// 支持微信朋友圈
		mController.getConfig().supportWXCirclePlatform(ShareActivity.this,appId, "") ;
				
    }
}
