package com.lixueandroid.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.lixueandroid.MyBaseActivity;
import com.lixueandroid.adapter.BookAdapter;
import com.lixueandroid.view.BookLayout;

/**
 * 翻页效果页面
 * 
 * @author lixue
 *
 */
public class PageTurnActivity extends MyBaseActivity{

	@Override
	public void onInitLayout(Bundle savedInstanceState) {
		BookLayout bk = new BookLayout(this);
        List<String> str = new ArrayList<String>();
        str.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        str.add("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        str.add("ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
        str.add("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        str.add("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        str.add("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        BookAdapter ba = new BookAdapter(this);
        ba.addItem(str);
        bk.setPageAdapter(ba);
        setContentView(bk);
	}

	@Override
	public void onInitListener(Bundle savedInstanceState) {
		
	}

	@Override
	public void onInitData(Bundle savedInstanceState) {
		
	}

}
