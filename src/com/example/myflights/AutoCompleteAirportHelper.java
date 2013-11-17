package com.example.myflights;

import android.database.Cursor;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

// helper class to run interfaces for converting the adapter to a string and querying DB for autocomplete suggestions
public class AutoCompleteAirportHelper {
	
	public AutoCompleteAirportHelper(SimpleCursorAdapter adapter) {
		// implement cursorToStringConverter interface
		adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {

			@Override
			public CharSequence convertToString(Cursor cursor) {
				final int index = cursor
						.getColumnIndexOrThrow(AirportData.C_AIRPORT);
				final int index2 = cursor
						.getColumnIndexOrThrow(AirportData.C_AIRPORT_NAME);

				return cursor.getString(index) + " " + cursor.getString(index2);
			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {

			@Override
			public Cursor runQuery(CharSequence currentValue) {
				return MyFlightsApp.flightData.airportData.query(currentValue);
			}

		});
	}

}
