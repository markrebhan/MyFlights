package com.example.myflights;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//DB Helper class to help build/update DB on create
class DbHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "flightdata.db";
	public static final int DB_VERSION = 61;
	public static final String TAG = "DBHelper";

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create DB
		String sql = String
				.format("CREATE TABLE %s "
						+ "(%s integer primary key asc, %s integer, %s integer, %s datetime, %s datetime, %s integer, %s text, %s int, %s text, %s int, %s int)",
						FlightData.TABLE, FlightData.C_ID, FlightData.C_ORIGIN,
						FlightData.C_DESTINATION, FlightData.C_DEPART_TIME,
						FlightData.C_ARRIVAL_TIME, FlightData.C_AIRLINE,
						FlightData.C_FLIGHT, FlightData.C_FLIGHTXML_ENABLED,
						FlightData.C_METAREX, FlightData.C_STATUS,
						FlightData.C_IS_DELETED);
		db.execSQL(sql);
		Log.d(TAG, "Created Table: " + sql);
		
		// create table of airports
		sql = String.format("CREATE TABLE %s "
				+ "(%s integer primary key asc, %s text, %s text, %s real, %s real, %s text)",
				AirportData.TABLE, AirportData.C_ID, AirportData.C_AIRPORT,
				AirportData.C_AIRPORT_NAME, AirportData.C_LAT, AirportData.C_LONG, AirportData.C_TIMEZONE);
		db.execSQL(sql);
		Log.d(TAG, "Created Table: " + sql);
		
		// create table of airlines
		sql = String.format("CREATE TABLE %s "
				+ "(%s integer primary key asc, %s text, %s text)",
				AirlineData.TABLE, AirlineData.C_ID, AirlineData.C_AIRLINE,
				AirlineData.C_AIRLINE_NAME);
		db.execSQL(sql);

		Log.d(TAG, "Created Table: " + sql);

		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Usually ALTER TABLE statement
		db.execSQL("DROP TABLE if exists " + FlightData.TABLE);
		db.execSQL("DROP TABLE if exists " + AirlineData.TABLE);
		db.execSQL("DROP TABLE if exists " + AirportData.TABLE);
		// not supposed to do this but for dev purposes
		onCreate(db);
		MyFlightsApp.flightData.airportData.insertAllAirportData();
		MyFlightsApp.flightData.airlineData.insertAllAirlineData();
	}

}