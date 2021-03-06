package com.example.myflights;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

// this broadcast receiver works with the Flight List activity to do an async update of the list view whenever
// broadcast is receiver (IE Data has changed)
public class ReceiverRefreshListData extends BroadcastReceiver {

	public final static String TAG = "ReceiverRefreshListData";
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean viewAll = PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("viewAllFlights", false);
		Log.d(TAG, "Broadcast Received");
		FragmentFlightList.cursor = MyFlightsApp.flightData.query(viewAll);
		FragmentFlightList.adapter.changeCursor(FragmentFlightList.cursor);
	}

}
