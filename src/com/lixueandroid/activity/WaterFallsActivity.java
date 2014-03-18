package com.lixueandroid.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.lixue.lixueandroid.R;
import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.view.FlowTag;
import com.lixueandroid.view.FlowView;
import com.lixueandroid.view.LazyScrollView;
import com.lixueandroid.view.LazyScrollView.OnScrollListener;

/**
 * 瀑布流--页面
 * 
 * @author lixue
 *
 */
public class WaterFallsActivity extends MyBaseActivity{
	private LazyScrollView lazyScrollView;
	private LinearLayout linearlayout_container;	//内容容器
	
	private Display display;
	private int columnCount=4;//每行几列
	private int itemWidth; //每项的宽
	private int[] itemHeight;//每项的高,每4列的高度都不一样，所以要每个都单独处理
	private int loaded_count;//加载数量
	private List<String> image_filenames;//图像名称集合
	private final String image_path = "images";//图像路径
	private int currentpage = 0;// 当前页数
	private int pageCount=30;//每页加载多少图片
	
	private Context context;
	private AssetManager assetManager;
	private Handler handler;
	
	private HashMap<Integer, FlowView> iviews;
	private HashMap<Integer, String> pins;
	private HashMap<Integer, Integer>[] pinMarks;
	
	private int[] lineIndex;
	private int[] topIndex;
	private int[] bottomIndex;
	
	private int scrollHeight;
	private ArrayList<LinearLayout> waterfallItems;
	
	private final static String TAG=WaterFallsActivity.class.getSimpleName();
	
	
	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_waterfalls);
		lazyScrollView=(LazyScrollView) findViewById(R.id.lazyscrollview);
		linearlayout_container=(LinearLayout) findViewById(R.id.lazyscrollview_container);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		lazyScrollView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onTop() {
				Log.d(TAG, "已经滚动到顶端");
			}
			
			@Override
			public void onScroll() {
				Log.d(TAG, "正在滚动中");
				
			}
			
			@Override
			public void onBottom() {
				AddItemToContainer(++currentpage, pageCount);
				Log.d(TAG, "已经滚动到底部");
			}
			
			@Override
			public void onAutoScroll(int l, int t, int oldl, int oldt) {
				Log.d(TAG, "在正自动滚动");
				scrollHeight=lazyScrollView.getMeasuredHeight();
				//如果是向下运动
				if(t>oldt){
					//如果超出两屏了
					if(scrollHeight*2<t){
						for (int k = 0; k < columnCount; k++) {
							LinearLayout localLinearLayout = waterfallItems.get(k);
							if (pinMarks[k].get(Math.min(bottomIndex[k] + 1,lineIndex[k])) <= t + 3 * scrollHeight) {// 最底部的图片位置小于当前t+3*屏幕高度
								((FlowView) waterfallItems.get(k).getChildAt(Math.min(1 + bottomIndex[k],lineIndex[k]))).Reload();
								bottomIndex[k] = Math.min(1 + bottomIndex[k],lineIndex[k]);
							}
							Log.d("MainActivity","headIndex:" + topIndex[k]+ "  footIndex:" + bottomIndex[k]+ "  headHeight:"+ pinMarks[k].get(topIndex[k]));
							if (pinMarks[k].get(topIndex[k]) < t - 2* scrollHeight) {// 未回收图片的最高位置<t-两倍屏幕高度
								int i1 = topIndex[k];
								topIndex[k]++;
								((FlowView) localLinearLayout.getChildAt(i1)).recycle();
								Log.d("MainActivity", "recycle,k:" + k+ " headindex:" + topIndex[k]);
							}
						}
					}
				}else{// 向下运动
					for (int k = 0; k < columnCount; k++) {
						LinearLayout localLinearLayout = waterfallItems.get(k);
						if (pinMarks[k].get(bottomIndex[k]) > t + 3* scrollHeight) {
							((FlowView) localLinearLayout.getChildAt(bottomIndex[k])).recycle();
							bottomIndex[k]--;
						}
						if (pinMarks[k].get(Math.max(topIndex[k] - 1, 0)) >= t- 2 * scrollHeight) {
							((FlowView) localLinearLayout.getChildAt(Math.max(-1 + topIndex[k], 0))).Reload();
							topIndex[k] = Math.max(topIndex[k] - 1, 0);
						}
					}
				}
			}
		});
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		display=getWindowManager().getDefaultDisplay();
		itemWidth=display.getWidth()/columnCount;
		itemHeight=new int[columnCount];
		context=this;
		assetManager=this.getAssets();
		iviews=new HashMap<Integer, FlowView>();
		pins=new HashMap<Integer, String>();
		pinMarks=new HashMap[columnCount];
		lineIndex=new int[columnCount];
		topIndex=new int[columnCount];
		bottomIndex=new int[columnCount];
		for (int i = 0; i < columnCount; i++) {
			lineIndex[i]=-1;
			bottomIndex[i]=-1;
			pinMarks[i]=new HashMap();
		}
		//初始化布局
		InitLayout();
	}
	
	/**
	 * 初始化布局
	 */
	@SuppressLint("HandlerLeak")
	private void InitLayout(){
		lazyScrollView.getView();
		handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
			}
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					FlowView flowView = (FlowView) msg.obj;
					int w = msg.arg1;
					int h = msg.arg2;
					// Log.d("MainActivity",
					// String.format(
					// "获取实际View高度:%d,ID：%d,columnIndex:%d,rowIndex:%d,filename:%s",
					// v.getHeight(), v.getId(), v
					// .getColumnIndex(), v.getRowIndex(),
					// v.getFlowTag().getFileName()));
					String f = flowView.getFlowTag().getFileName();
					// 此处计算列值
					int columnIndex = GetMinValue(itemHeight);
					Log.d(TAG, "columnIndex:"+columnIndex);
					flowView.setColumnIndex(columnIndex);
					itemHeight[columnIndex] += h;
					pins.put(flowView.getId(), f);
					iviews.put(flowView.getId(), flowView);
					waterfallItems.get(columnIndex).addView(flowView);
					lineIndex[columnIndex]++;
					pinMarks[columnIndex].put(lineIndex[columnIndex],itemHeight[columnIndex]);
					bottomIndex[columnIndex] = lineIndex[columnIndex];
					break;
				}
			}

			@Override
			public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
				return super.sendMessageAtTime(msg, uptimeMillis);
			}
		};
		waterfallItems = new ArrayList<LinearLayout>();
		for (int i = 0; i < columnCount; i++) {
			LinearLayout itemLayout = new LinearLayout(this);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT);
			
			itemLayout.setPadding(2, 2, 2, 2);
			itemLayout.setOrientation(LinearLayout.VERTICAL);
			
			itemLayout.setLayoutParams(itemParam);
			waterfallItems.add(itemLayout);
			linearlayout_container.addView(itemLayout);
		}
		
		// 加载所有图片路径
		try {
			image_filenames = Arrays.asList(assetManager.list(image_path));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 第一次加载
		AddItemToContainer(currentpage, pageCount);
	}
	
	/**
	 * 向容器中添加项
	 * 
	 * @param pageindex
	 * @param pagecount
	 */
	private void AddItemToContainer(int pageindex, int pagecount) {
		int currentIndex = pageindex * pagecount;
		int imagecount = 10000;// image_filenames.size();
		for (int i = currentIndex; i < pagecount * (pageindex + 1)&& i < imagecount; i++) {
			loaded_count++;
			Random rand = new Random();
			int r = rand.nextInt(image_filenames.size());
			AddImage(image_filenames.get(r),(int) Math.ceil(loaded_count / (double) columnCount),loaded_count);
		}
	}

	/**
	 * 添加图片
	 * @param filename
	 * @param rowIndex
	 * @param id
	 */
	private void AddImage(String filename, int rowIndex, int id) {
		FlowView item = new FlowView(context);
		// item.setColumnIndex(columnIndex);
		item.setRowIndex(rowIndex);
		item.setId(id);
		item.setViewHandler(this.handler);
		// 多线程参数
		FlowTag param = new FlowTag();
		param.setFlowId(id);
		param.setAssetManager(assetManager);
		param.setFileName(image_path + "/" + filename);
		param.setItemWidth(itemWidth);

		item.setFlowTag(param);
		item.LoadImage();
		// waterfall_items.get(columnIndex).addView(item);
	}
	/**
	 * 得到最小的值
	 * @param array
	 * @return
	 */
	private int GetMinValue(int[] array) {
		int m = 0;
		int length = array.length;
		for (int i = 0; i < length; i++) {
			if (array[i] < array[m]) {
				m = i;
			}
		}
		return m;
	}
}
