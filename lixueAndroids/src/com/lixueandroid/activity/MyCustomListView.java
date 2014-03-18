package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lixue.lixueandroid.R;
import com.lixueandroid.adapter.MyAdapter;
import com.lixueandroid.view.CustomListView;
import com.lixueandroid.view.CustomListView.OnLoadMoreListener;
import com.lixueandroid.view.CustomListView.OnRefreshListener;

public class MyCustomListView extends Activity implements OnClickListener{
	
	private CustomListView listView;
	private LinkedList<String> list;
	private MyAdapter myAdapter;
	private int width;
	
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycustomelistview);
		button1=(Button) findViewById(R.id.button1);
		button2=(Button) findViewById(R.id.button2);
		button3=(Button) findViewById(R.id.button3);
		button4=(Button) findViewById(R.id.button4);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		listView = (CustomListView) findViewById(R.id.listView);
		list = new LinkedList<String>();
		for (int i = 0; i < 10; i++) {
			list.add("第"+i+"选项");
		}
		
		//自定义适配器
		myAdapter = new MyAdapter(MyCustomListView.this , list);
		//未listview设置适配
		listView.setAdapter(myAdapter);
		//添加监听
		listView.setOnTouchListener(new OnTouchListener() {
			float x,y,upx,upy;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					x = event.getX();
					y = event.getY();
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					upx = event.getX();
					upy = event.getY();
					//获取相应位置的listview的id
					int position1 = ((ListView)v).pointToPosition((int)x, (int)y);
					int position2 = ((ListView)v).pointToPosition((int)upx, (int)upy);
					//判断是否滑动,解决getChildAt为null的问题
					if (position1 == position2 && Math.abs(x - upx) > width/2) {
						int firstvisitablepisition = ((ListView)v).getFirstVisiblePosition();
						View view = ((ListView)v).getChildAt(position1 - firstvisitablepisition);
						Log.i("position","pisition1:"+position1 + "    firstvisitablepisition:" + firstvisitablepisition);
						removeListItem(view , position1 - 1);
					}
				}
				return false;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Toast.makeText(getBaseContext(), "当前点击的是第"+arg2+"条数据", Toast.LENGTH_SHORT).show();
			}
		});
		listView.setOnLoadListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				RefreshTask rTask = new RefreshTask();
				rTask.execute(1000,1);
			}
		});
		//添加长按出现选项菜单
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("选项菜单");
				menu.add(0, 0, 0, "添加");
				menu.add(0, 1, 0, "重命名");
				menu.add(0, 2, 0, "删除");
				
			}
		});
		//长按监听事件
		listView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				listView.showContextMenu();
				return true;
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				RefreshTask rTask = new RefreshTask();
				rTask.execute(1000,0);
			}
		});
	}

	protected void removeListItem(View view, final int position) {
		Log.i("view","pisition:"+position + "    view:" + view);
		final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.item_remove);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				list.remove(position);
				myAdapter.notifyDataSetChanged();
				animation.cancel();
			}
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
		});
		view.startAnimation(animation);
		
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			//“添加”操作
			Toast.makeText(MyCustomListView.this, "点击了“添加”操作", Toast.LENGTH_SHORT).show();
			break;
			
		case 1:
			//“重命名”操作
			Toast.makeText(MyCustomListView.this, "点击了“重命名”操作", Toast.LENGTH_SHORT).show();
			break;
			
		case 2:
			//“删除”操作
			Toast.makeText(MyCustomListView.this, "点击了“删除”操作", Toast.LENGTH_SHORT).show();
			break;
	

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	// AsyncTask异步任务
		class RefreshTask extends AsyncTask<Integer, Integer, Integer>
	    {    
	    	@Override
			protected Integer doInBackground(Integer... params) 
			{
				try 
				{
					Thread.sleep(params[0]);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				if(params[1]==0){
					// 在data最前添加数据
					list.addFirst("刷新后的内容");
				}
				else{
					List<String> mylist=new ArrayList<String>();
					mylist.add("加载更多后，加载的数据");
					mylist.add("加载更多后，加载的数据");
					mylist.add("加载更多后，加载的数据");
					mylist.add("加载更多后，加载的数据");
					mylist.add("加载更多后，加载的数据");
					list.addAll(mylist);
				}
				return params[1];
			}	

			@Override
			protected void onPostExecute(Integer result) 
			{
				super.onPostExecute(result);
				myAdapter.notifyDataSetChanged();
				if(result==0){
					listView.onRefreshComplete();
				}else{
					listView.onLoadMoreComplete();
				}
			}    	
	    }
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button1:
				listView.setCanRefresh(!listView.isCanRefresh());
				if(button1.getText().toString().equals("关闭下拉刷新")){
					button1.setText("启用下拉刷新");
				}else{
					button1.setText("关闭下拉刷新");
				}
				break;
			case R.id.button2:
				listView.setCanLoadMore(!listView.isCanLoadMore());
				if(button2.getText().toString().equals("关闭加载更多")){
					button2.setText("启用加载更多");
				}else{
					button2.setText("关闭加载更多");
				}
				break;
			case R.id.button3:
				listView.setAutoLoadMore(!listView.isAutoLoadMore());
				if(button3.getText().toString().equals("关闭自动加载更多")){
					button3.setText("启用自动加载更多");
				}else{
					button3.setText("关闭自动加载更多");
				}
				break;
			case R.id.button4:
				listView.setMoveToFirstItemAfterRefresh(!listView.isMoveToFirstItemAfterRefresh());
				if(button4.getText().toString().
						equals("关闭移动到第一条Item")){
					button4.setText("启用移动到第一条Item");
				}else{
					button4.setText("关闭移动到第一条Item");
				}
				break;
			}
		}
		

}
