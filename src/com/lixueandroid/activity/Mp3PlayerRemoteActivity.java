package com.lixueandroid.activity;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fileuplaod.unity.DownLoadUnity;
import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.adapter.Mp3PlayerAdapter;
import com.lixueandroid.domain.Mp3Info;
import com.lixueandroid.service.DownLoadService;
import com.lixueandroidhandler.Mp3ListContentHandler;

/**
 * Mp3播放器（在服务器下载mp3文件）
 * @author lixue
 *
 */
public class Mp3PlayerRemoteActivity extends MyBaseActivity{
	
	private final int UPDATE=1;
	private final int ABOUT=2;
	//显示歌曲的列表
	private ListView Mp3playerlistview;
	//变量集合-为显示列表
	private List<Mp3Info> mp3InfoList;
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mp3playerremote);
		Mp3playerlistview=(ListView) findViewById(R.id.list_Mp3Player);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		Mp3playerlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Mp3Info mp3Info=mp3InfoList.get(position);
				Intent intent=new Intent();
				intent.putExtra("mp3info", mp3Info);
				intent.setClass(getBaseContext(), DownLoadService.class);
				startService(intent);
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		String xmlStr=downLoadXml("http://192.168.1.156/expocube/resources.xml");
		System.out.println("XML---------->"+xmlStr); 
		xmlParser(xmlStr);
		Mp3playerlistview.setAdapter(new Mp3PlayerAdapter(getBaseContext(), mp3InfoList));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, UPDATE, 1, "更新列表");
		menu.add(0, ABOUT, 2, "关于");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==UPDATE){
			//用户点击了更新列表按钮
			String xmlStr=downLoadXml("http://192.168.1.156/expocube/resources.xml");
			System.out.println("XML---------->"+xmlStr); 
			xmlParser(xmlStr);
			Mp3playerlistview.setAdapter(new Mp3PlayerAdapter(getBaseContext(), mp3InfoList));
			//用来显示MP3列表
			/*accessNetwork(new Mp3PlayerRequest(), new TypeToken<List<Mp3Info>>(){}.getType(), new AccessNetworkListener<List<Mp3Info>>() {

				@Override
				public void onStart() {
					showLoadingHintView();
				}

				@Override
				public void onSuccess(List<Mp3Info> responseObject) {
					if(responseObject!=null){
						mp3Playerlist=responseObject;
						Mp3playerlistview.setAdapter(new Mp3PlayerAdapter(getBaseContext(), mp3Playerlist));
					}
				}

				@Override
				public void onError(ErrorInfo errorInfo) {
					toastL(R.string.toast_network_connectException);
				}

				@Override
				public void onEnd() {
					closeLoadingHintView();
				}
			});*/
		}else if(item.getItemId()==ABOUT){
			//用户点击了关于按钮
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	//下载XML文件
	public String downLoadXml(String xmlUrl){
		return DownLoadUnity.DownLoadTxtFile(xmlUrl);
	}
	
	//XML文件解析
	public List<Mp3Info> xmlParser(String xmlStr){
		SAXParserFactory saxParserFactory=SAXParserFactory.newInstance();
		try{
			XMLReader reader=saxParserFactory.newSAXParser().getXMLReader();
			mp3InfoList=new ArrayList<Mp3Info>();
			Mp3ListContentHandler mp3ListContentHandler=new Mp3ListContentHandler(mp3InfoList);
			reader.setContentHandler(mp3ListContentHandler);
			reader.parse(new InputSource(new StringReader(xmlStr)));
			
			for (Iterator<Mp3Info> iterator = mp3InfoList.iterator(); iterator.hasNext();) {
				Mp3Info mp3Info = (Mp3Info) iterator.next();
				System.out.println(mp3Info);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
