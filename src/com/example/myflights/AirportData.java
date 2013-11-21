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

public class AirportData {

	public static final String TABLE = "airports";
	public static final String C_ID = "_id";
	public static final String C_AIRPORT = "airport";
	public static final String C_AIRPORT_NAME = "name";

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

	 
	public void insert(String airportCode, String airport) {
		ContentValues values = new ContentValues();
		values.put(C_AIRPORT, airportCode);
		values.put(C_AIRPORT_NAME, airport);
		FlightData.db = FlightData.dbHelper.getWritableDatabase();
		FlightData.db.insertWithOnConflict(TABLE, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
	}

	// TODO move into async task to display to UI on initial load
	public void insertAllAirportData() {
		//run insert on a different thread
		new Thread() {
			public void run() {
				
				String line = null;
				String delimiter = ",";

				try {
					AssetManager am = context.getAssets();
					InputStream is = am.open("iata_to_icao.csv");
					BufferedInputStream bis = new BufferedInputStream(is);
					InputStreamReader isr = new InputStreamReader(bis);
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine()) != null) {

						String[] airport = line.split(delimiter);
						insert(airport[1], airport[2]);

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
