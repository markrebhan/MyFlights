package com.example.myflights;

import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityFlightInfo extends Activity {

	public static final String TAG = "ActivityFlightInfo";
	public static final String bundleName = "id";
	String intExtraName = ActivityMyFlights.name;
	Cursor cursor;
	String departAirport, arriveAirport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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
		String airline = cursor.getString(cursor.getColumnIndex(FlightData.C_AIRLINE));
		String flight = airline + cursor.getString(cursor.getColumnIndex(FlightData.C_FLIGHT));
		
		// find the view for the fragment and then find the textView in that fragment and set title
		View fragmentTB = getFragmentManager().findFragmentById(R.id.fragment_title_bar).getView();
		TextView title = (TextView) fragmentTB.findViewById(R.id.title);
		title.setText(flight);
		// find the imagebutton and set image
		ImageButton buttonTitle = (ImageButton) fragmentTB.findViewById(R.id.button_title);
		// find the resource image and set button that image
		int resID = getResources().getIdentifier(airline.toLowerCase(Locale.US), "drawable" , getPackageName());
		if (resID > 0) buttonTitle.setImageResource(resID);
		// if unable to find image for airline, set to default
		else buttonTitle.setImageResource(R.drawable.ic_launcher);
	}

	// onClick Listener from title bar fragment
	public void onClickTitle(View view){
		// go back to previous activity
		finish();
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

}
