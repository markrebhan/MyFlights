package com.example.myflights;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FlightData {

	static final String TAG = "FlightData";

	public static final String TABLE = "flights";
	// define columns
	public static final String C_ID = "_id";
	public static final String C_ORIGIN = "origin";
	public static final String C_DESTINATION = "destination";
	public static final String C_DEPART_TIME = "depart_time";
	public static final String C_ARRIVAL_TIME = "arrival_time";
	public static final String C_AIRLINE = "airline";
	public static final String C_FLIGHT = "flight";
	public static final String C_FLIGHTXML_ENABLED = "flightXML_enabled";
	public static final String C_METAREX = "meterex";
	public static final String C_STATUS = "status";
	public static final String C_IS_DELETED = "is_deleted";

	// columns for airport names
	public static final String C_ORIGIN_NAME = "origin_name";
	public static final String C_DESTINATION_NAME = "destination_name";

	Context context;
	static DbHelper dbHelper;
	static SQLiteDatabase db;
	AirportData airportData;
	AirlineData airlineData;

	// this constructor is the main constructor for the DB any other tables will
	// need to reference this dbHelper and SQLiteDatabase variable
	// create a constructor to instantiate a DB
	public FlightData(Context context) {
		this.context = context;
		this.airportData = new AirportData(context);
		this.airlineData = new AirlineData(context);
		dbHelper = new DbHelper(context);

	}

	public void insertData(int origin, int destination, String departuretime,
			String arrivaltime, int airline, String flight, int flightXML) {

		ContentValues values = new ContentValues();
		values.put(C_ORIGIN, origin);
		values.put(C_DESTINATION, destination);
		values.put(C_DEPART_TIME, departuretime);
		values.put(C_ARRIVAL_TIME, arrivaltime);
		values.put(C_AIRLINE, airline);
		values.put(C_FLIGHT, flight);
		values.put(C_FLIGHTXML_ENABLED, flightXML);
		values.put(C_STATUS, 0);
		values.put(C_IS_DELETED, 0);

		db = dbHelper.getWritableDatabase();
		db.insertWithOnConflict(TABLE, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);

	}

	public Cursor query(boolean viewAll) {
		db = dbHelper.getReadableDatabase();

		String WhereIsDeleted = "WHERE is_deleted = 0";
		// show deleted records if preferences is set to view all
		if (viewAll)
			WhereIsDeleted = "";

		// here is a nasty SELECT statement
		// remember that a the simplecursoradapter is looking for specific names
		// of columns.
		Cursor cursor = db
				.rawQuery(
						"SELECT f._id, p1.airport AS origin, p1.name AS origin_name, p2.airport AS destination, p2.name AS destination_name, "
								+ "f.depart_time AS depart_time, f.arrival_time AS arrival_time, l.airline AS airline,"
								+ " f.flight AS flight, f.status AS status, f.flightXML_enabled AS flightXML_enabled, p1.timezone AS timezone, "
								+ "p2.timezone AS timezone2 from flights AS f JOIN airports AS p1 on"
								+ " f.origin = p1._id JOIN airports AS p2 on f.destination = p2._id LEFT JOIN airlines AS l"
								+ " ON f.airline = l._id "
								+ WhereIsDeleted
								+ " ORDER BY f.depart_time", null);

		return cursor;

	}

	public Cursor query(int id) {
		db = dbHelper.getReadableDatabase();

		// where clause if we are only choosing 1 ID to query
		String WhereID = "";
		if (id != -1)
			WhereID = " WHERE f._id = " + Integer.toString(id);

		// here is a nasty SELECT statement
		// remember that a the simplecursoradapter is looking for specific names
		// of columns.
		Cursor cursor = db
				.rawQuery(
						"SELECT f._id, p1.airport AS origin, p1.name AS origin_name, p2.airport AS destination, p2.name AS destination_name, "
								+ "f.depart_time AS depart_time, f.arrival_time AS arrival_time, l.airline AS airline,"
								+ " f.flight AS flight, f.status AS status, f.flightXML_enabled AS flightXML_enabled, p1.timezone AS timezone, " 
								+ "p2.timezone AS timezone2 from flights AS f JOIN airports AS p1 on"
								+ " f.origin = p1._id JOIN airports AS p2 on f.destination = p2._id LEFT JOIN airlines AS l"
								+ " ON f.airline = l._id "
								+ WhereID
								+ " ORDER BY f.depart_time", null);

		return cursor;
	}

	public int queryEntityID(String entity, String table, String column) {
		db = dbHelper.getReadableDatabase();
		String[] columns = { C_ID };
		String[] selectionArgs = { entity };
		Cursor cursor = db.query(table, columns, column + "=?", selectionArgs,
				null, null, null);
		boolean hasData = cursor.moveToFirst();
		if (hasData) {
			int col = cursor.getColumnIndex(C_ID);
			return cursor.getInt(col);

		} else
			return -1;
	}

	// Check if there is already a duplicate entry based on airline, flight, and
	// depart time
	// if the cursor returns more than 0 entries, then return false
	public boolean queryForDup(int airlineCode, String flight, String departTime) {
		db = dbHelper.getReadableDatabase();
		String[] columns = { C_ID };
		String[] selectionArgs = { Integer.toString(airlineCode), flight,
				departTime, departTime };
		Log.d(TAG, Integer.toString(airlineCode) + " " + flight + " "
				+ departTime);
		Cursor cursor = db
				.query(TABLE, columns, C_AIRLINE + "=? AND " + C_FLIGHT
						+ "=? AND " + C_DEPART_TIME + ">? AND " + C_DEPART_TIME
						+ "<? + 86400", selectionArgs, null, null, null);
		Log.d(TAG, Integer.toString(cursor.getCount()));
		int count = cursor.getCount();
		if (count == 0)
			return true;
		else
			return false;
	}

	// TODO split up into two update statements to better handle both cases (API
	// enabled or no)
	public int updateDeleted() {
		ContentValues values = new ContentValues();
		values.put(C_IS_DELETED, 1);
		// mark deleted flights as arrived;
		values.put(C_STATUS, 4);
		long currentDate = System.currentTimeMillis() / 1000;
		long delayedDate = currentDate + 11000;
		db = dbHelper.getWritableDatabase();
		int rowsAffected = db.update(TABLE, values, "((" + C_ARRIVAL_TIME + "<"
				+ currentDate + ") OR (" + C_DEPART_TIME + "<" + delayedDate
				+ " AND " + C_ARRIVAL_TIME + " IS NULL))" + " AND "
				+ C_IS_DELETED + " = 0", null);
		return rowsAffected;

	}

	// delete from table if user specifies it
	public void userDeletesRecord(int id) {
		db = dbHelper.getWritableDatabase();
		String[] whereArgs = { Integer.toString(id) };
		db.delete(TABLE, C_ID + "=?", whereArgs);
	}

}
