package com.example.myflights;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class ListenerViewItem extends FragmentFlightList implements
		OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		
		// get cursor at current position
		Cursor cursor = (Cursor) parent.getItemAtPosition(position);

		// define values to send to listener callback
		int dbID = cursor.getInt(cursor.getColumnIndex(FlightData.C_ID));
		
		mCallback2.onFlightSelectedListener(dbID);
	}

}
