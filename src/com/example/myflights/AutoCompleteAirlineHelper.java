package com.example.myflights;

import android.database.Cursor;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

public class AutoCompleteAirlineHelper {

	public AutoCompleteAirlineHelper(SimpleCursorAdapter adapter) {
		// implement cursorToStringConverter interface
		adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {

			@Override
			public CharSequence convertToString(Cursor cursor) {
				final int index = cursor
						.getColumnIndexOrThrow(AirlineData.C_AIRLINE);
				final int index2 = cursor
						.getColumnIndexOrThrow(AirlineData.C_AIRLINE_NAME);

				return cursor.getString(index) + " " + cursor.getString(index2);
			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {

			@Override
			public Cursor runQuery(CharSequence currentValue) {
				return MyFlightsApp.flightData.airlineData.query(currentValue);
			}

		});
	}
}
