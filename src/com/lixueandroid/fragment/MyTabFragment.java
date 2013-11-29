package com.lixueandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lixue.lixueandroid.R;
import com.lixueandroid.adapter.mytabAdapter;

public class MyTabFragment extends Fragment {
	private ListView listview;
	private mytabAdapter adapter;
	private List<String> myliststr;
	private String type;


	public MyTabFragment(String string) {
		this.type=string;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		listview = (ListView) inflater.inflate(R.layout.listview_mytab, null);
		myliststr = new ArrayList<String>();
		if (type.equals("0")) {
			myliststr.add("李雪 	北京昆仑亿发科技发展有限公司");
			myliststr.add("潘鹏飞	北京维万时代");
			myliststr.add("李敬		北京昆仑亿发科技发展有限公司");
		} else if (type.equals("1")) {
			myliststr.add("李雪1 	北京昆仑亿发科技发展有限公司");
			myliststr.add("潘鹏飞1	北京维万时代");
			myliststr.add("李敬1		北京昆仑亿发科技发展有限公司");
		} else if (type.equals("2")) {
			myliststr.add("李雪2 	北京昆仑亿发科技发展有限公司");
			myliststr.add("潘鹏飞2	北京维万时代");
			myliststr.add("李敬2		北京昆仑亿发科技发展有限公司");
		} else if (type.equals("3")) {
			myliststr.add("李雪3 	北京昆仑亿发科技发展有限公司");
			myliststr.add("潘鹏飞3	北京维万时代");
			myliststr.add("李敬3		北京昆仑亿发科技发展有限公司");
		} else if (type.equals("4")) {
			myliststr.add("李雪4 	北京昆仑亿发科技发展有限公司");
			myliststr.add("潘鹏飞4	北京维万时代");
			myliststr.add("李敬34	北京昆仑亿发科技发展有限公司");
		}
		adapter = new mytabAdapter(getActivity(), myliststr);
		listview.setAdapter(adapter);
		return listview;
	}
}
