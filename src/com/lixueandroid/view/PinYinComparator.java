package com.lixueandroid.view;

import java.util.Comparator;

import com.lixueandroid.domain.SortModel;

public class PinYinComparator implements Comparator<SortModel>{

	@Override
	public int compare(SortModel lhs, SortModel rhs) {
		if(rhs.getSortLetters().equals("#")){
			return -1;
		}else if (lhs.getSortLetters().equals("#")){
			return 1;
		}else {
			return lhs.getSortLetters().compareTo(rhs.getSortLetters());
		}
	}
}
