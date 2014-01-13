package com.example.myflights;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

// class to build TimeConvert object to handle converting times to appropriate format and timezone
public class TimeConvert {
	
	private long time;
	private String dateFormat;
	private String timezone;
	
	public TimeConvert(long time, String dateFormat, String timezone){
		this.time = time;
		this.dateFormat = dateFormat;
		this.timezone = timezone;
	}
	
	// static method with input parameters of Timezone and Date Format
	public String convertTime(){
		SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
		df.setTimeZone(TimeZone.getTimeZone(timezone));
		return df.format(new Date(time));
	}

}
