package com.example.myflights;

import org.json.JSONObject;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

/*NOTES
 * 1. DB stores times in EPOCH (Seconds after 1/1/1970), JAVA/Android uses milliseconds after 1/1/1970 so multiply by 1000 from DB -> App and divide by 1000 from App - DB
 * 
 * 
 */

public class MyFlightsApp extends Application implements
		OnSharedPreferenceChangeListener {
	static final String TAG = "MyFlightsApp";
	public static final String ACTION_REFRESH = "com.example.myflights.RefreshFlightDataService";
	public static final String ACTION_REFRESH_DELETE = "com.example.myflights.RefreshDeleteService";

	static FlightData flightData;
	SharedPreferences prefs;
	static final Intent test = new Intent(ACTION_REFRESH_DELETE);

	@Override
	public void onCreate() {
		super.onCreate();

		flightData = new FlightData(this);
		
		// get current preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// notify this listener when a changed has been made
		prefs.registerOnSharedPreferenceChangeListener(this);

		Log.d(TAG, "App created.");
		
		new Thread(){
			public void run(){
				
				RESTfulCalls r = new RESTfulCalls();
				JSONObject response = r.flightInfoEx("JBU", "1413");
				Log.d(TAG, response.toString());
				
				
			}
		}.start();
		
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		this.prefs = prefs;
		
		// send broadcast to query DB to show all flights if in same activity
		//sendBroadcast(new Intent(ACTION_REFRESH));
	}

}
