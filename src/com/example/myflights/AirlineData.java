package com.example.myflights;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AirlineData {
	public static final String TABLE = "airlines";
	public static final String C_ID = "_id";
	public static final String C_AIRLINE = "airline";
	public static final String C_AIRLINE_NAME = "name";
	public static final String C_AIRLINE_LOGO = "logo";
	
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	Context context;
	public AirlineData(Context context){this.context = context;}
	
	public void insert(String airlineCode, String airline) {
		ContentValues values = new ContentValues();
		values.put(C_AIRLINE, airlineCode);
		values.put(C_AIRLINE_NAME, airline);
		FlightData.db = FlightData.dbHelper.getWritableDatabase();
		FlightData.db.insertWithOnConflict(TABLE, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public Cursor query(CharSequence currentValue) {
		FlightData.db = FlightData.dbHelper.getReadableDatabase();
		// SELECT * FROM airports WHERE airport LIKE 'XXX%'
		Cursor cursor = FlightData.db.query(TABLE, null, C_AIRLINE + " LIKE '" + currentValue + "%'", null, null,
				null, C_AIRLINE);
		return cursor;
	}

	// TODO move into async task to display to UI on initial load
	public void insertAllAirlineData() {
		//run insert on a different thread
		new Thread() {
			public void run() {
				
				String line = null;
				String delimiter = ",";

				try {
					AssetManager am = context.getAssets();
					InputStream is = am.open("airlines.csv");
					BufferedInputStream bis = new BufferedInputStream(is);
					InputStreamReader isr = new InputStreamReader(bis);
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine()) != null) {

						String[] airline = line.split(delimiter);
						insert(airline[0], airline[1]);

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
