package com.lixueandroid.activity;

import java.io.File;
import java.io.IOException;

import me.xiaopan.easyandroid.app.BaseActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.lixue.lixueandroid.R;
import com.lixueandroid.activity.domain.Mp3Info;

/**
 * mp3播放器控制页面
 * @author Administrator
 *
 */
public class Mp3PlayerActivity extends BaseActivity{
	private boolean ifStart;//是否开始
	private boolean ifPause;//是否暂停
	private boolean ifRelease;//是否结束(是否释放了资源)
	
	private Mp3Info mp3info;
	private MediaPlayer mediaPlayer;
	
	private ImageButton startImageButton;
	private ImageButton pauseImageButton;
	private ImageButton releaseImageButton;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mp3player);
		startImageButton=(ImageButton) findViewById(R.id.imgbutton_mediaplayer_start);
		pauseImageButton=(ImageButton) findViewById(R.id.imgbutton_mediaplayer_pause);
		releaseImageButton=(ImageButton) findViewById(R.id.imgbutton_mediaplayer_end);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		startImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!ifStart){
					try {
						//读取assets文件夹下的音乐文件
//						mediaPlayer=new MediaPlayer();
//						AssetManager am = getAssets();
//	                    AssetFileDescriptor afd = am.openFd("me.mp3");
//	                    FileDescriptor fd = afd.getFileDescriptor();
//	                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//	                    mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
//	                    mediaPlayer.prepare();
						
						//读取sd卡中的本地音乐文件
						mediaPlayer=MediaPlayer.create(getBaseContext(), Uri.parse(getMp3LocalPath(mp3info.getMp3Name())));
						mediaPlayer.prepare();
						
						
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mediaPlayer.setLooping(false);
					mediaPlayer.start();
					ifStart=true;
					ifRelease=false;
				}
			}
		});
		pauseImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!ifPause){
					if(!ifRelease){
						mediaPlayer.pause();
						ifPause=true;
						ifStart=true;
					}
				}else{
					if(!ifRelease){
						mediaPlayer.start();
						ifPause=false;
						ifStart=true;
					}
				}
			}
		});
		releaseImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ifStart){
					if(!ifRelease){
						mediaPlayer.stop();
						mediaPlayer.release();//使播放器不再进行播放
						ifRelease=true;
						ifStart=false;
					}
				}
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		mp3info=(Mp3Info) getIntent().getSerializableExtra("mp3payer");
	}
	
	//根据mp3文件名，得到mp3本地的路径
	private String getMp3LocalPath(String filename){
		String localpath= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"mp3"+File.separator+filename;
		return localpath;
	}
}
