package com.example.myflights;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityFlightInfo extends Activity {

	public static final String TAG = "ActivityFlightInfo";
	public static final String bundleName = "id";
	String intExtraName = ActivityMyFlights.name;
	String airline;
	Cursor cursor;
	String departAirport, arriveAirport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_flight_info);
		
		// put in bundle ID to pass into flight info fragment to populate view with data
		int id = getIntent().getIntExtra(intExtraName, -1);
		
		cursor = MyFlightsApp.flightData.query(id);
		cursor.moveToFirst();
		
		departAirport = cursor.getString(cursor.getColumnIndex(FlightData.C_ORIGIN));
		arriveAirport = cursor.getString(cursor.getColumnIndex(FlightData.C_DESTINATION));
		
		// pass in airports to draw on maps
		FragmentMap fragmentMap = (FragmentMap) getFragmentManager().findFragmentById(R.id.fragment_map);
		fragmentMap.onDBLoaded(departAirport, arriveAirport);
		airline = cursor.getString(cursor.getColumnIndex(FlightData.C_AIRLINE));
		String flight = airline + cursor.getString(cursor.getColumnIndex(FlightData.C_FLIGHT));
		
		// set action bar title to flight
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(flight);
		
		// find the resource image and set activity logo to it (App logo by default)
		int resID = getResources().getIdentifier(airline.toLowerCase(Locale.US), "drawable" , getPackageName());
		if (resID > 0) actionBar.setLogo(resID);
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// find fragment in current activity to set arguments for fragment
		FragmentFlightInfo fragment = (FragmentFlightInfo) getFragmentManager()
				.findFragmentById(R.id.fragment_flight_info);
		fragment.refreshViews(cursor);

		// get weather information from departure airport

		FragmentWeather fragment2 = (FragmentWeather) getFragmentManager()
				.findFragmentById(R.id.fragment_weather);
		fragment2.refreshWeatherData(departAirport);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_flight_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh_flight_info:
			//TODO create refresh service for this activity
			finish();
			return true;
		default:
			return false;
		}
	}

}
