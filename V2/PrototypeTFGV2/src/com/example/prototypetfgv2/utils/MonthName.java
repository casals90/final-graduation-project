package com.example.prototypetfgv2.utils;

import java.util.ArrayList;

public class MonthName {
	
	private ArrayList<String> monthName;

	public MonthName() {
		super();
		init();
	}
	
	public void init() {
		monthName = new ArrayList<String>();
		monthName.add("Jan");
		monthName.add("Feb");
		monthName.add("Mar");
		monthName.add("Apr");
		monthName.add("May");
		monthName.add("Jun");
		monthName.add("Jul");
		monthName.add("Aug");
		monthName.add("Sep");
		monthName.add("Oct");
		monthName.add("Nov");
		monthName.add("Dec");
	}
	
	public int getMonthNumber(String name) {
		for(int i = 0; i < monthName.size(); i++) {
			if(monthName.get(i).compareTo(name) == 0)
				return i + 1;
		}
		return -1;
	}
}
