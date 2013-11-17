package com.example.myflights;

import java.util.Calendar;

public class CurrentDate {
	
	public static long currentDate(){
		Calendar currentDate = Calendar.getInstance();
		long currentDateInMil = currentDate.getTimeInMillis();
		return currentDateInMil;
	}
	
	// overloaded method to handle cases where we want to add a buffer
	public static long currentDate(long buffer){
		long currentDateInMil = currentDate();
		long bufferedDate = currentDateInMil + buffer;
		return bufferedDate;
	}

}
