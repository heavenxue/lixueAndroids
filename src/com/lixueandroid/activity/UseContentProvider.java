package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.easyandroid.app.BaseActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.domain.News;
import com.lixueandroid.providers.Articles;

/**
 * 利用ContenProvider的页面
 * @author lixue
 *
 */
public class UseContentProvider extends BaseActivity{
	private TextView textView;
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_usecontentprovider);	
		textView=(TextView) findViewById(R.id.textview);
		News news=new News();
		news.setABSTRACT("天空");
		news.setTITLE("今天的新闻");
		news.setURL("http://sss.dkfj");
		saveArticlesEntry(news);
		for (News newse : getList()) {
			textView.setText("从数据库得到的数据为:标题："+newse.TITLE+",梗概:"+newse.ABSTRACT+",Url:"+newse.URL);
		}
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}
	/**
	 * 保存一条文章数据
	 * @param order
	 */
	public void saveArticlesEntry(News articles) {
		ContentValues values = articles.toContentValues();
		getBaseContext().getContentResolver().insert(Articles.CONTENT_URI, values);
	}
	
	public List<News> getList(){
		Cursor cursor;
		List<News> news=new ArrayList<News>();
		cursor=getBaseContext().getContentResolver().query(Articles.CONTENT_URI, null, null, null, Articles.DEFAULT_SORT_ORDER);
		while (cursor!=null&&cursor.moveToNext()) {
			News news2=new News();
			news2.ID=cursor.getString(cursor.getColumnIndex(Articles.ID));
			news2.ABSTRACT=cursor.getString(cursor.getColumnIndex(Articles.ABSTRACT));
			news2.TITLE=cursor.getString(cursor.getColumnIndex(Articles.TITLE));
			news2.URL=cursor.getString(cursor.getColumnIndex(Articles.URL));
			news.add(news2);
		}
		return news;
	}
}
