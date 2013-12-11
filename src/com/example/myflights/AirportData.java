package com.example.myflights;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AirportData {

	public static final String TABLE = "airports";
	public static final String C_ID = "_id";
	public static final String C_AIRPORT = "airport";
	public static final String C_AIRPORT_NAME = "name";
	public static final String C_LAT = "lat";
	public static final String C_LONG = "long";
	public static final String C_TIMEZONE = "timezone";
	
	private static final String FILE = "airports.csv";

	Context context;

	public AirportData(Context context) {
		this.context = context;
	}

	public Cursor query(CharSequence currentValue) {
		FlightData.db = FlightData.dbHelper.getReadableDatabase();
		// SELECT * FROM airports WHERE airport LIKE 'XXX%'
		Cursor cursor = FlightData.db.query(TABLE, null, C_AIRPORT + " LIKE '" + currentValue + "%' OR " + C_AIRPORT + " LIKE 'K" + currentValue + "%'", null, null,
				null, C_AIRPORT);
		return cursor;
	}
	
	public Cursor queryLatLong(String airport) {
		FlightData.db = FlightData.dbHelper.getReadableDatabase();
		Cursor cursor = FlightData.db.query(TABLE, new String [] {C_ID, C_LAT, C_LONG}, C_AIRPORT + " LIKE '" + airport + "'" , null, null, null, null, "1");
		Log.d("BLA", airport + " " + Integer.toString(cursor.getCount()));
		return cursor;
	}

	 
	public void insert(String airportCode, String airport, String lat, String longi, String timezone) {
		ContentValues values = new ContentValues();
		values.put(C_AIRPORT, airportCode);
		values.put(C_AIRPORT_NAME, airport);
		values.put(C_LAT, lat);
		values.put(C_LONG, longi);
		values.put(C_TIMEZONE, timezone);
		FlightData.db = FlightData.dbHelper.getWritableDatabase();
		FlightData.db.insertWithOnConflict(TABLE, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
	}

	// TODO move into async task to display to UI on initial load
	public void insertAllAirportData() {
		//run insert on a different thread
		Log.d("DB", "BLA");
		new Thread() {
			public void run() {
				
				String line = null;
				String delimiter = ",";

				try {
					AssetManager am = context.getAssets();
					InputStream is = am.open(FILE);
					BufferedInputStream bis = new BufferedInputStream(is);
					InputStreamReader isr = new InputStreamReader(bis);
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine()) != null) {
						
						String[] airport = line.split(delimiter);
						insert(airport[1], airport[2], airport[3], airport[4], airport[5]);

					}

					br.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
