package com.lixueandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 自定义的Adapter基类
 */
public abstract class MyBaseAdapter extends BaseAdapter {
	/**
	 * 已经打开了选择模式
	 */
	private boolean openedChoiceMode;
	/**
	 * 已经打开分页模式
	 */
	private boolean openedPagingMode;
	/**
	 * 已经打开了当滚动时不刷新数据模式
	 */
	private boolean openedOnScrollNoRefreshDataMode;
	/**
	 * 是否刷新数据
	 */
	private boolean refershData = true;
	/**
	 * 是否已经进入选择模式
	 */
	private boolean intoChoiceMode = false;
	/**
	 * 选择模式的选择方式，分为单选和多选
	 */
	private ChoiceWay choiceWay = ChoiceWay.MULTI_CHOICE;
	/**
	 * 选中
	 */
	private boolean[] selecteds;
	/**
	 * 选中个数
	 */
	private int selectedNumber;
	
	/**
	 * 总条目数
	 */
	private int totalEntries;
	/**
	 * 每页条目数
	 */
	private int pageEntries = 10;
	/**
	 * 总页数
	 */
	private int totalPages;
	/**
	 * 当前页索引
	 */
	private int currentPageIndex;
	/**
	 * 分页模式的分页方式，分为替换和追加两种，默认为追加
	 */
	private PagingWay pagingWay = PagingWay.APPEND;
	/**
	 * 列表视图
	 */
	private AbsListView absListView;
	/**
	 * 选择模式监听器
	 */
	private ChoiceModeListener choiceModeListener;
	/**
	 * 分页模式监听器
	 */
	private PagingModeListener pagingModeListener;
	/**
	 * 项点击监听器
	 */
	private AdapterView.OnItemClickListener onItemClickListener;
	/**
	 * 项长按监听器
	 */
	private AdapterView.OnItemLongClickListener onItemLongClickListener;
	/**
	 * 滚动监听器
	 */
	private AbsListView.OnScrollListener onScrollListener;
	/**
	 * 全选按钮
	 */
	private CompoundButton selectAllButton;
	/**
	 * 来自自己
	 */
	private boolean fromMe = true;
	/**
	 * 上下文
	 */
	private Context context;
	
	/**
	 * 创建我的Apdater基类
	 * @param absListView 使用这个适配器的列表，要给这个列表加上当快速滚动时不刷新数据的功能
	 */
	public MyBaseAdapter(AbsListView absListView){
		setAbsListView(absListView);
	}
	
	public MyBaseAdapter(Context context){
		setContext(context);
	}
	
	public MyBaseAdapter(){
	}
	
	@Override
	public int getCount() {
		int count = getRealCount();
		//如果已经打开了分页模式
		if(isOpenedPagingMode()){
			int totalEntries = getTotalEntries();
			if(getPagingWay() == PagingWay.REPLACE){
				count = getPageEntries() <= totalEntries ? getPageEntries() : totalEntries;
				if(isLastPage()){
					count = totalEntries % getPageEntries() == 0 ? getPageEntries() : totalEntries % getPageEntries();
				}
			}else{
				count = (getCurrentPageIndex() + 1) * getPageEntries();
				if(isLastPage()){
					count = totalEntries % getPageEntries() == 0 ? getPageEntries() : count - ( getPageEntries()-totalEntries % getPageEntries());
				}
			}
		}
		return count;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 获取总条目数
	 * @return 总条目数
	 */
	public abstract int getRealCount();
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//如果刷新数据
		if(convertView == null || isRefershData()){
			return getRealView(getRealPosition(position), convertView, parent);
		}else{
			return convertView;
		}
	}
	
	/**
	 * 获取真实的视图
	 * @param realPosition 真实的位置，此位置是数据列表中真实的位置
	 * @param convertView 
	 * @param parent
	 * @return
	 */
	public abstract View getRealView(int realPosition, View convertView, ViewGroup parent);
	
	/**
	 * 打开选择模式
	 */
	public void openChiceMode(){
		setOpenedChoiceMode(true);
		setIntoChoiceMode(false);
		if(getChoiceModeListener() != null){
			getChoiceModeListener().onOpen();
		}
	}

	/**
	 * 关闭选择模式
	 */
	public void closeChiceMode(){
		setOpenedChoiceMode(false);
		setIntoChoiceMode(false);
		if(getChoiceModeListener() != null){
			getChoiceModeListener().onClose();
		}
	}
	
	/**
	 * 进入选择模式，进入之后会重新初始化所有与选择模式相关的属性
	 * @return true：进入成功；false：进入失败，原因是没有开启选择模式
	 */
	public boolean intoChoiceMode(){
		boolean result = false;
		//如果已经开启了选择模式
		if(isOpenedChoiceMode()){
			setIntoChoiceMode(true);
			setSelecteds(new boolean[getRealCount()]);
			setSelectedNumber(0);
			if(getChoiceWay() == ChoiceWay.MULTI_CHOICE && getSelectAllButton() != null){
				getSelectAllButton().setVisibility(View.VISIBLE);
				getSelectAllButton().setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(fromMe){
							if(isChecked){
								selectAll();
							}else{
								deselectAll();
							}
						}
					}
				});
			}else{
				if(getSelectAllButton() != null){
					getSelectAllButton().setOnCheckedChangeListener(null);
				}
			}
			if(getChoiceModeListener() != null){
				getChoiceModeListener().onInto();
			}
			result = true;
			notifyDataSetChanged();
		}
		return result;
	}
	
	/**
	 * 退出选择模式，退出选择模式后，之前的所有记录会清空
	 * @return true：退出成功；false：退出失败，原因是没有开启选择模式
	 */
	public boolean exitChoiceMode(){
		boolean result = false;
		//如果已经开启了选择模式
		if(isOpenedChoiceMode()){
			setIntoChoiceMode(false);
			setSelecteds(null);
			setSelectedNumber(0);
			if(getChoiceWay() == ChoiceWay.MULTI_CHOICE && getSelectAllButton() != null){
				getSelectAllButton().setVisibility(View.INVISIBLE);
				getSelectAllButton().setOnCheckedChangeListener(null);
			}
			if(getChoiceModeListener() != null){
				getChoiceModeListener().onExit();
			}
			result = true;
			notifyDataSetChanged();
		}
		return result;
	}
	
	/**
	 * 打开当滚动时不刷新数据模式
	 */
	public void openOnScrollNoRefreshDataMode(){
		setOpenedOnScrollNoRefreshDataMode(true);
	}
	
	/**
	 * 关闭当滚动时不刷新数据模式
	 */
	public void closeOnScrollNoRefreshDataMode(){
		setOpenedOnScrollNoRefreshDataMode(false);
	}
	
	/**
	 * 打开分页模式
	 */
	public void openPagingMode(){
		setOpenedPagingMode(true);
		updateTotalPages();
		if(getPagingModeListener() != null){
			getPagingModeListener().onOpen();
		}
		notifyDataSetChanged();
	}
	
	/**
	 * 关闭分页模式，关闭分页模式后会将当前页索引重设为0
	 */
	public void closePagingMode(){
		setOpenedPagingMode(false);
		setCurrentPageIndex(0);
		if(getPagingModeListener() != null){
			getPagingModeListener().onClose();
		}
		notifyDataSetChanged();
	}
	
	/**
	 * 处理选择按钮，如果已经打开了选择模式，就会将按钮的值设为选择状态数组里position位置的值，并且将Visibility属性设为VISIBLE，否则将Visibility属性设为GONE
	 * @param choiceButton 要处理的选择按钮
	 * @param realPosition 当前点击的行的索引
	 */
	protected void choiceButtonHandle(CompoundButton choiceButton, int realPosition){
		//如果选择按钮不为null
		if(choiceButton != null){
			//如果已经打开并进入了选择模式
			if(isOpenedChoiceMode() && isIntoChoiceMode()){
				//设为不可点击
				choiceButton.setClickable(false);
				//设置值
				choiceButton.setChecked(getSelecteds()[realPosition]);
				//设置显示
				choiceButton.setVisibility(View.VISIBLE);
			}else{
				//设置隐藏
				choiceButton.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 点击给定的项处理，会根据具体的选择模式（包括多选或单选两种）去设置相关项的选中状态
	 * @param position 被点击项的位置
	 * @param refresh 是否刷新
	 * @return true：处理成功；false：处理失败，因为还没有开启选择模式，或者已经开启了 但是还没有进入选择模式
	 */
	private boolean choiceClickHandle(int position, boolean refresh){
		boolean result = false;
		//如果已经打开并进入了选择模式
		if(isOpenedChoiceMode() && isIntoChoiceMode()){
			//获取真实的位置
			int realPosition = getRealPosition(position);
			//如果是多选
			if(getChoiceWay() == ChoiceWay.MULTI_CHOICE){
				//设置当前项的状态
				getSelecteds()[realPosition] = !getSelecteds()[realPosition];
				//如果当前项是选中，那么将选中个数加1，否则减1
				if(getSelecteds()[realPosition]){
					setSelectedNumber(getSelectedNumber()+1);
				}else{
					setSelectedNumber(getSelectedNumber()-1);
				}
				
				//如果全选按钮存在
				if(getSelectAllButton() != null){
					fromMe = false;
					//如果已经全部选中，就将全选按钮设为 选中状态，否则设为未选中状态
					if(getSelectedNumber() >= getRealCount()){
						getSelectAllButton().setChecked(true);
						if(getChoiceModeListener() != null){
							getChoiceModeListener().onSelectAll();
						}
					}else{
						//如果之前是全选状态
						if(getSelectAllButton().isChecked()){
							getSelectAllButton().setChecked(false);
							if(getChoiceModeListener() != null){
								getChoiceModeListener().onDeselectAll();
							}
						}
					}
					fromMe = true;
				}
			//如果是单选
			}else if(getChoiceWay() == ChoiceWay.SINGLE_CHOICE){
				//如果当前点击项不是没有选中
				if(!getSelecteds()[realPosition]){
					//遍历所有项，将当前项设为选中，其他项设为未选中
					for(int w = 0; w < getSelecteds().length; w++){
						if(w == realPosition){
							getSelecteds()[w] = true;
						}else{
							getSelecteds()[w] = false;
						}
					}
				}
				setSelectedNumber(1);
			}

			//如果选择监听器不为null，就调用项点击事件
			if(getChoiceModeListener() != null){
				getChoiceModeListener().onItemClick(realPosition, getSelecteds()[realPosition]);
				getChoiceModeListener().onUpdateSelectedNumber(getSelectedNumber());
			}
			result = true;
			if(refresh){
				notifyDataSetChanged();
			}
		}
		return result;
	}
	
	/**
	 * 设置选中
	 * @param position
	 * @return
	 */
	public boolean setChecked(int position){
		return choiceClickHandle(position, false);
	}
	
	/**
	 * 全部选中
	 * @return false：处理失败，成功的前提是开启，并进入选择模式，并且选择方式是多选
	 */
	public boolean selectAll(){
		boolean result = false;
		//如果已经打开并进入了选择模式以及选择模式是多选
		if(isOpenedChoiceMode() && isIntoChoiceMode() && getChoiceWay() == ChoiceWay.MULTI_CHOICE){
			//遍历选中状态数组将所有的状态都设为选中
			for(int w = 0; w < getSelecteds().length; w++){
				getSelecteds()[w] = true;
			}
			//设置选中个数
			setSelectedNumber(getRealCount());
			
			if(getChoiceModeListener() != null){
				getChoiceModeListener().onSelectAll();
				getChoiceModeListener().onUpdateSelectedNumber(getSelectedNumber());
			}
			result = true;
			notifyDataSetChanged();
		}
		return result;
	}

	/**
	 * 全部取消选中
	 * @return false：处理失败，成功的前提是开启，并进入选择模式，并且选择方式是多选
	 */
	public boolean deselectAll(){
		boolean result = false;
		//如果已经打开并进入了选择模式以及选择模式是多选
		if(isOpenedChoiceMode() && isIntoChoiceMode() && getChoiceWay() == ChoiceWay.MULTI_CHOICE){
			//遍历选中状态数组将所有的状态都设为未选中
			for(int w = 0; w < getSelecteds().length; w++){
				getSelecteds()[w] = false;
			}
			//设置选中个数
			setSelectedNumber(0);
			
			if(getChoiceModeListener() != null){
				getChoiceModeListener().onDeselectAll();
				getChoiceModeListener().onUpdateSelectedNumber(getSelectedNumber());
			}
			result = true;
			notifyDataSetChanged();
		}
		return result;
	}
	
	/**
	 * 获取所有选中项的索引
	 * @return 所有选中项的索引
	 */
	public int[] getSelectedIndexs(){
		int[] indexs2 = null;
		//如果已经打开并进入了选择模式
		if(isOpenedChoiceMode() && isIntoChoiceMode()){
			int[] indexs = new int[getSelecteds().length];
			int number = 0;
			for(int w = 0; w < getSelecteds().length; w++){
				if(getSelecteds()[w]){
					indexs[number++] = w;
				}
			}
			indexs2 = new int[number];
			System.arraycopy(indexs, 0, indexs2, 0, number);
		}
		return indexs2;
	}
	
	/**
	 * 获取真实的位置
	 * @param position
	 * @return 真实的位置
	 */
	public int getRealPosition(int position){
		//如果已经打开分页模式并且分页方式是替换
		if(isOpenedPagingMode() && getPagingWay() == PagingWay.REPLACE){
			position = ((currentPageIndex * pageEntries) + position);
		}
		return position;
	}
	
	/**
	 * 上一页
	 * @return false：当前已经是第一页或者尚未开启分页模式
	 */
	public boolean previousPage(){
		boolean result = false;
		//如果已经打开了分页模式
		if(isOpenedPagingMode()){
			if(currentPageIndex-1 >= 0){
				currentPageIndex--;
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 下一页
	 * @return false：当前已经是最后一页或者尚未开启分页模式
	 */
	public boolean nextPage(){
		boolean result = false;
		//如果已经打开了分页模式
		if(isOpenedPagingMode()){
			if(currentPageIndex + 1 < totalPages){
				currentPageIndex++;
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 是否是第一页
	 * @return false：当前不是第一页或者尚未开启分页模式
	 */
	public boolean isFirstPage(){
		boolean result = false;
		//如果已经打开了分页模式
		if(isOpenedPagingMode()){
			result = currentPageIndex == 0;
		}
		return result;
	}
	
	/**
	 * 是否是最后一页
	 * @return false：当前不是最后一页或者尚未开启分页模式
	 */
	public boolean isLastPage(){
		boolean result = false;
		//如果已经打开了分页模式
		if(isOpenedPagingMode()){
			result = currentPageIndex == totalPages-1;
		}
		return result;
	}

	
	/* *********************************GET/SET部分************************************* */
	/**
	 * 判断是否已经打开了选择模式
	 * @return 是否已经打开了选择模式
	 */
	public boolean isOpenedChoiceMode() {
		return openedChoiceMode;
	}

	/**
	 * 设置是否已经打开了选择模式
	 * @param choiceMode 是否已经打开了选择模式
	 */
	private void setOpenedChoiceMode(boolean choiceMode) {
		this.openedChoiceMode = choiceMode;
	}

	/**
	 * 判断是否已经打开了分页模式
	 * @return 是否已经打开了分页模式
	 */
	public boolean isOpenedPagingMode() {
		return openedPagingMode;
	}

	/**
	 * 设置已经打开了分页模式
	 * @param openedpagingMode 是否已经打开了分页模式
	 */
	private void setOpenedPagingMode(boolean openedpagingMode) {
		this.openedPagingMode = openedpagingMode;
	}

	/**
	 * 判断是否已经打开了当列表滚动时不刷新数据模式
	 * @return 是否已经打开了当列表滚动时不刷新数据模式
	 */
	public boolean isOpenedOnScrollNoRefreshDataMode() {
		return openedOnScrollNoRefreshDataMode;
	}

	/**
	 * 设置是否已经打开了当列表滚动时不刷新数据模式
	 * @param openedOnScrollNoRefreshDataMode 是否已经打开了当列表滚动时不刷新数据模式
	 */
	private void setOpenedOnScrollNoRefreshDataMode(boolean openedOnScrollNoRefreshDataMode) {
		this.openedOnScrollNoRefreshDataMode = openedOnScrollNoRefreshDataMode;
	}

	/**
	 * 判断是否刷新数据
	 * @return 是否刷新数据
	 */
	public boolean isRefershData() {
		return refershData;
	}
	
	/**
	 * 设置是否刷新数据
	 * @param refershData 是否刷新数据
	 */
	private void setRefershData(boolean refershData) {
		this.refershData = refershData;
	}
	
	/**
	 * 判断是否已经进入选择模式
	 * @return 是否已经进入选择模式
	 */
	public boolean isIntoChoiceMode() {
		return intoChoiceMode;
	}

	/**
	 * 设置是否已经进入选择模式
	 * @param intoChoiceMode 是否已经进入选择模式
	 */
	private void setIntoChoiceMode(boolean intoChoiceMode) {
		this.intoChoiceMode = intoChoiceMode;
	}

	/**
	 * 判断选择模式的选择方式
	 * @return 选择模式的选择方式，默认为ChoiceWay.MULTI_CHOICE（多选）
	 */
	public ChoiceWay getChoiceWay() {
		return choiceWay;
	}

	/**
	 * 设置选择模式的选择方式
	 * @param choiceWay 选择模式的选择方式，默认为ChoiceWay.MULTI_CHOICE（多选）
	 */
	public void setChoiceWay(ChoiceWay choiceWay) {
		this.choiceWay = choiceWay;
	}
	
	/**
	 * 获取所有选项的选中状态数组
	 * @return 所有选项的选中状态数组
	 */
	public boolean[] getSelecteds(){
		return selecteds;
	}

	/**
	 * 设置所有选项的选中状态数组
	 * @param selecteds 所有选项的选中状态数组
	 */
	public void setSelecteds(boolean[] selecteds) {
		if(isOpenedChoiceMode()){
			this.selecteds = selecteds;
		}
	}
	
	/**
	 * 获取选中个数
	 * @return 选中个数
	 */
	public int getSelectedNumber() {
		return selectedNumber;
	}
	
	/**
	 * 设置选中个数
	 * @param selectedNumber 选中个数
	 */
	public void setSelectedNumber(int selectedNumber) {
		this.selectedNumber = selectedNumber;
	}
	
	/**
	 * 获取总条目数
	 * @return 总条目数
	 */
	public int getTotalEntries(){
		//获取新的总条目数
		int newTotalEntries = getRealCount();
		//如果新的总条目数不等于旧的总条目数
		if(newTotalEntries != totalEntries){
			//设置总条目数并更新总页数以及选中项
			setTotalEntries(newTotalEntries);
		}
		return totalEntries;
	}
	
	/**
	 * 设置总条目数
	 * @param newTotalEntries
	 */
	public void setTotalEntries(int newTotalEntries) {
		//如果已经打开了选择模式
		if(isOpenedChoiceMode()){
			//如果新的总条目数大于旧的总条目数
			if(newTotalEntries > totalEntries){
				boolean[] newSelecteds = new boolean[newTotalEntries];
				System.arraycopy(getSelecteds(), 0, newSelecteds, 0, getSelecteds().length);
				setSelecteds(newSelecteds);
			}else if(newTotalEntries < totalEntries){
				boolean[] newSelecteds = new boolean[newTotalEntries];
				System.arraycopy(getSelecteds(), 0, newSelecteds, 0, newTotalEntries);
				setSelecteds(newSelecteds);
				setSelectedNumber(0);
				for(boolean selected : getSelecteds()){
					if(selected){
						setSelectedNumber(getSelectedNumber() + 1);
					}
				}
			}
		}
		
		//如果已经打开分页模式
		if(isOpenedPagingMode()){
			//记录旧的总页数
			int oldTotalPages = getTotalPages();
			
			//更新总条目数
			this.totalEntries = newTotalEntries;
			
			//更新总页数
			updateTotalPages();
			
			//如果旧的总页数大于新的总页数，那么将当前索引页设置最后一页
			if(getTotalPages() < oldTotalPages){
				setCurrentPageIndex(getTotalPages()-1);
			}
		}
	}
	
	/**
	 * 获取每页展示的条目数
	 * @return 每页展示的条目数
	 */
	public int getPageEntries() {
		return pageEntries;
	}
	
	/**
	 * 设置每页展示的条目数，此方法会顺便根据总条目数计算总页数
	 * @param pageEntries 每页展示的条目数
	 */
	public void setPageEntries(int pageEntries) {
		this.pageEntries = pageEntries;
		updateTotalPages();
	}

	/**
	 * 获取总页数
	 * @return 总页数
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * 设置总页数，此属性由当前对象在设置总条目数或每页展示条目数后自动计算，不需要手动设置
	 * @param totalPages 总页数
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * 获取当前页索引
	 * @return 当前页索引
	 */
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	/**
	 * 设置当前页索引
	 * @param currentPageIndex 当前页索引
	 * @return true：成功；fasle：失败，当前显示页的索引同将要设置的一样
	 * @throws IllegalArgumentException 当前页索引小于0或者大于等于总页数
	 */
	public boolean setCurrentPageIndex(int currentPageIndex) throws IllegalArgumentException {
		boolean result = false;
		if(this.currentPageIndex != currentPageIndex){
			if(currentPageIndex >=0 && currentPageIndex < totalPages){
				this.currentPageIndex = currentPageIndex;
				result = true;
			}else{
				throw new IllegalArgumentException("Total number of pages is "+totalPages+", But you want to set a when previous page is " + currentPageIndex);
			}
		}
		return result;
	}
	
	/**
	 * 获取分页方式
	 * @return 分页方式
	 */
	public PagingWay getPagingWay() {
		return pagingWay;
	}

	/**
	 * 设置分页方式
	 * @param pagingWay 分页方式
	 */
	public void setPagingWay(PagingWay pagingWay) {
		this.pagingWay = pagingWay;
	}
	
	/**
	 * 获取列表视图
	 * @return 列表视图
	 */
	public AbsListView getAbsListView() {
		return absListView;
	}

	/**
	 * 设置列表视图
	 * @param absListView 列表视图
	 */
	public void setAbsListView(AbsListView absListView) {
		if(absListView != null){
			this.absListView = absListView;
			absListView.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					//如果已经开启了滚动不刷新数据模式
					if(isOpenedOnScrollNoRefreshDataMode()){
						//如果是飞速滚动就标记为不刷新数据，否则就标记为刷新数据并刷新
						if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
							setRefershData(false);
						}else{
							setRefershData(true);
							notifyDataSetChanged();
						}
					}
					if(getOnScrollListener() != null){
						getOnScrollListener().onScrollStateChanged(view, scrollState);
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					if(getOnScrollListener() != null){
						getOnScrollListener().onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
					}
				}
			});
			
			absListView.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					boolean result = true;
					//如果已经开启了分页模式
					if(isOpenedChoiceMode()){
						//如果已经进入了分页模式就退出，反之就进入
						if(isIntoChoiceMode()){
							exitChoiceMode();
						}else{
							intoChoiceMode();
							choiceClickHandle((int) id, true);
						}
					}else if(getOnItemLongClickListener() != null){
						result = getOnItemLongClickListener().onItemLongClick(parent, view, getRealPosition((int) id), id);
					}
					return result ;
				}
			});
			
			absListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					//如果已经开启并进入了分页模式
					if(isOpenedChoiceMode() && isIntoChoiceMode()){
						choiceClickHandle((int) arg3, true);
					}else if(getOnItemClickListener() != null){
						getOnItemClickListener().onItemClick(arg0, arg1, getRealPosition((int) arg3), arg3);
					}
				}
			});
			
			setContext(absListView.getContext());
		}
	}
	
	/**
	 * 获取选择模式监听器
	 * @return 选择模式监听器
	 */
	public ChoiceModeListener getChoiceModeListener() {
		return choiceModeListener;
	}

	/**
	 * 设置选择模式监听器
	 * @param choiceModeListener 选择模式监听器
	 */
	public void setChoiceModeListener(ChoiceModeListener choiceModeListener) {
		this.choiceModeListener = choiceModeListener;
	}

	/**
	 * 获取分页模式监听器
	 * @return 分页模式监听器
	 */
	public PagingModeListener getPagingModeListener() {
		return pagingModeListener;
	}

	/**
	 * 设置分页模式监听器
	 * @param pagingModeListener 分页模式监听器
	 */
	public void setPagingModeListener(PagingModeListener pagingModeListener) {
		this.pagingModeListener = pagingModeListener;
	}

	/**
	 * 获取项点击监听器
	 * @return 项点击监听器
	 */
	public AdapterView.OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	/**
	 * 设置项点击监听器
	 * @param onItemClickListener 项点击监听器
	 */
	public void setOnItemClickListener(
			AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	/**
	 * 获取项长按监听器
	 * @return 项长按监听器
	 */
	public AdapterView.OnItemLongClickListener getOnItemLongClickListener() {
		return onItemLongClickListener;
	}

	/**
	 * 设置项长按监听器
	 * @param onItemLongClickListener 项长按监听器
	 */
	public void setOnItemLongClickListener(
			AdapterView.OnItemLongClickListener onItemLongClickListener) {
		this.onItemLongClickListener = onItemLongClickListener;
	}

	/**
	 * 获取滚动监听器
	 * @return 滚动监听器
	 */
	public AbsListView.OnScrollListener getOnScrollListener() {
		return onScrollListener;
	}

	/**
	 * 设置滚动监听器
	 * @param onScrollListener 滚动监听器
	 */
	public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	/**
	 * 获取全选按钮
	 * @return 全选按钮
	 */
	public CompoundButton getSelectAllButton() {
		return selectAllButton;
	}

	/**
	 * 设置全选按钮
	 * @param selectAllButton 全选按钮
	 */
	public void setSelectAllButton(CompoundButton selectAllButton) {
		this.selectAllButton = selectAllButton;
	}

	/**
	 * 更新总页数
	 */
	private void updateTotalPages(){
		//如果已经打开了分页模式
		if(isOpenedPagingMode()){
			int totalEntries = getTotalEntries();
			setTotalPages(totalEntries%getPageEntries() >0 ? (totalEntries/getPageEntries())+1 : totalEntries/getPageEntries());
		}
	}

	/**
	 * 获取上下文
	 * @return 上下文
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * 设置上下文
	 * @param context 上下文
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	
	/**
	 * 加载布局文件
	 * @param layoutResId 布局文件资源ID
	 * @param parentView 父视图
	 * @return
	 */
	public View inflateLayout(int layoutResId, ViewGroup parentView){
		View result = null;
		if(getContext() != null){
			result = LayoutInflater.from(getContext()).inflate(layoutResId, parentView);
		}
		return result;
	}
	
	/**
	 * 加载布局文件
	 * @param layoutResId 布局文件资源ID
	 * @return
	 */
	public View inflateLayout(int layoutResId){
		return inflateLayout(layoutResId, null);
	}

	/**
	 * 分页方式
	 */
	public enum PagingWay{
		/**
		 * 在列表尾部追加 
		 */
		APPEND, 
		
		/**
		 * 替换
		 */
		REPLACE;
	}

	/**
	 * 选择方式
	 * @version 1.0 
	 * @author panpf
	 * @date Jun 16, 2012
	 */
	public enum ChoiceWay{
		/**
		 * 多选
		 */
		MULTI_CHOICE, 
		
		/**
		 * 单选
		 */
		SINGLE_CHOICE;
	}

	/**
	 * 选择模式监听器
	 */
	public interface ChoiceModeListener{
		/**
		 * 当打开
		 */
		public void onOpen();
		
		/**
		 * 当关闭
		 */
		public void onClose();
		
		/**
		 * 当进入
		 */
		public void onInto();
		
		/**
		 * 当退出
		 */
		public void onExit();
		
		/**
		 * 当全部选中
		 */
		public void onSelectAll();
		
		/**
		 * 当全部取消选中
		 */
		public void onDeselectAll();
		
		/**
		 * 当项被点击
		 * @param realPosition 当前项的真实位置
		 * @param isChecked 是否选中
		 */
		public void onItemClick(int realPosition, boolean isChecked);
		
		/**
		 * 当更新选中个数
		 * @param selectedNumber 当前选中个数
		 */
		public void onUpdateSelectedNumber(int selectedNumber);
	}

	/**
	 * 分页模式监听器
	 */
	public interface PagingModeListener{
		/**
		 * 当打开
		 */
		public void onOpen();
		/**
		 * 当关闭
		 */
		public void onClose();
	}
}