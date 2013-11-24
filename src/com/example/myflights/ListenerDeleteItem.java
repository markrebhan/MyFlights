package com.example.myflights;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListenerDeleteItem extends FragmentFlightList implements OnItemLongClickListener{

	public final static String TAG = "ListenerDeleteItem";
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		// the data from list entry
		Cursor cursor = (Cursor) parent.getItemAtPosition(position);
		
		// define values to send to listener callback
		String flightNo = cursor.getString(cursor.getColumnIndex(FlightData.C_FLIGHT));
		String airline = cursor.getString(cursor.getColumnIndex(FlightData.C_AIRLINE));
		int dbID = cursor.getInt(cursor.getColumnIndex(FlightData.C_ID));
		
		FragmentFlightList.mCallback.onFlightSelected(dbID, airline, flightNo);
		
		return true;
	}

}
