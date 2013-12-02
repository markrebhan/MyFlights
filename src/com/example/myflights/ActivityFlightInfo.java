package com.example.myflights;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class ActivityFlightInfo extends Activity{

	public static final String TAG = "ActivityFlightInfo";
	public static final String bundleName = "id";
	String intExtraName = ActivityMyFlights.name;
	
	
	private GoogleMap googleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_flight_info);
		
		
		// find the mapFragment to set the map object to the map fragment
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		googleMap = mapFragment.getMap();
		
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// put in bundle ID to pass into flight info fragment to populate view with data
		int id = getIntent().getIntExtra(intExtraName, -1);
		
		Cursor cursor = MyFlightsApp.flightData.query(id);
		
		
		// find fragment in current activity to set arguments for fragment
		FragmentFlightInfo fragment = (FragmentFlightInfo) getFragmentManager().findFragmentById(R.id.fragment_flight_info);
		fragment.refreshViews(cursor);
		
		// get weather information from departure airport
		cursor.moveToFirst();
		String airport = cursor.getString(cursor.getColumnIndex(FlightData.C_ORIGIN));
		
		FragmentWeather fragment2 = (FragmentWeather) getFragmentManager().findFragmentById(R.id.fragment_weather);
		fragment2.refreshWeatherData(airport);
		
	}
	
	

}
