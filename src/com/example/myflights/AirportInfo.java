package com.example.myflights;

import android.database.Cursor;
import android.util.Log;

public class AirportInfo {
	
	private String airport;
	private float latitude, longitude;
	
	// constructor
	public AirportInfo(String airport){
		this.airport = airport;
		QueryData();
	}
	
	public void QueryData(){
		Cursor cursor = MyFlightsApp.flightData.airportData
				.queryLatLong(airport);
		
		cursor.moveToFirst();
		this.latitude = cursor.getFloat(cursor.getColumnIndex(AirportData.C_LAT));
		this.longitude = cursor
				.getFloat(cursor.getColumnIndex(AirportData.C_LONG));
	}

	// set getters for the object
	public String getAirport() {
		return airport;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}
	
	

}
