package com.example.myflights;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyFlightsActivity extends Activity {
	public static final String TAG = "MyFlightsActivity";

	// mappings from DB -> listview
	static final String[] FROM = { FlightData.C_ORIGIN,
			FlightData.C_ORIGIN_NAME, FlightData.C_DESTINATION,
			FlightData.C_DESTINATION_NAME, FlightData.C_DEPART_TIME,
			FlightData.C_ARRIVAL_TIME, FlightData.C_AIRLINE,
			FlightData.C_FLIGHT, FlightData.C_STATUS, FlightData.C_FLIGHTXML_ENABLED };
	static final int[] TO = { R.id.from, R.id.from_name, R.id.to, R.id.to_name,
			R.id.depart, R.id.arrive, R.id.airline_logo, R.id.flight,
			R.id.status, R.id.flightaware };

	SimpleCursorAdapter adapter;

	ImageView airlineLogo;
	Drawable drawable;
	ListView list;
	Cursor cursor;
	MyFlightsReceiver receiver;

	// get shared preferences for viewing all flights

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_flights);
		startService(new Intent(this, RefreshFlightDataService.class));

		// drawable = getResources().getDrawable(R.drawable.jetblue_airways);
		// airlineLogo = (ImageView) findViewById(R.id.airline_logo);
		// airlineLogo.setImageDrawable(drawable);

	}

	@Override
	protected void onResume() {
		super.onResume();

		list = (ListView) findViewById(R.id.flight_lists);

		Log.d(TAG, "On Resumed");

		boolean viewAll = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getBoolean("viewAllFlights", false);
		cursor = MyFlightsApp.flightData.query(viewAll);

		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO,
				0);

		// adapter = new CursorAdapter(this, cursor);

		adapter.setViewBinder(VIEW_BINDER);

		// setListAdapter(adapter);
		list.setAdapter(adapter);
		// register receiver
		if (receiver == null)
			receiver = new MyFlightsReceiver();
		registerReceiver(receiver,
				new IntentFilter(MyFlightsApp.ACTION_REFRESH));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	public void onClick(View v) {
		startActivity(new Intent(this, AddFlightActivity.class));
		
	}

	static final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			// if(view.getId() != R.id.depart) return false;

			long time;
			String formattedDate = "";

			int viewID = view.getId();
			switch (viewID) {
			case R.id.depart:
				// TODO change date if needed
				time = cursor.getLong(cursor
						.getColumnIndex(FlightData.C_DEPART_TIME)) * 1000;
				if (time > 0) {
					formattedDate = new SimpleDateFormat(
							"MM/dd/yy hh:mm aaa z", Locale.US).format(new Date(
							time));
				}
				// if no time available, use the date for nowS
				else {
					formattedDate = new SimpleDateFormat("MM/dd/yy", Locale.US)
							.format(new Date(time));
				}
				((TextView) view).setText(formattedDate);
				return true;
			case R.id.arrive:
				time = cursor.getLong(cursor
						.getColumnIndex(FlightData.C_ARRIVAL_TIME)) * 1000;
				if (time > 0) {
					formattedDate = new SimpleDateFormat("hh:mm aaa z",
							Locale.US).format(new Date(time));
				}
				((TextView) view).setText(formattedDate);
				return true;
			case R.id.flight:
				String flightNo = cursor.getString(cursor
						.getColumnIndex(FlightData.C_FLIGHT));
				flightNo = "Flight #" + flightNo;
				((TextView) view).setText(flightNo);
				return true;
			case R.id.status:
				int statusID = cursor.getInt(cursor
						.getColumnIndex(FlightData.C_STATUS));
				String statusText = "";
				int color = Color.GREEN;
				if (statusID == 0) {
					statusText = "Scheduled";
				} else if (statusID == 1) {
					statusText = "On-Time";
				} else if (statusID == 2) {
					statusText = "Delayed";
					color = Color.YELLOW;
				} else if (statusID == 3) {
					statusText = "Cancelled";
					color = Color.RED;
				} else if (statusID == 4) {
					statusText = "Arrived";
				}

				((TextView) view).setTextColor(color);
				((TextView) view).setText(statusText);
				return true;
			case R.id.airline_logo:
				// inheritly calls bitmapfactory
				ImageView logo = (ImageView) view;
				String airline = (cursor.getString(cursor
						.getColumnIndex(FlightData.C_AIRLINE)));
				// TODO move this
				// resources.getidentifier is not performance optimized so
				// manually set res ID for logos

				if (airline != null) {
					if (airline.equals("JBU"))
						logo.setImageResource(R.drawable.jbu);
					else if (airline.equals("AAL"))
						logo.setImageResource(R.drawable.aal);
					else if (airline.equals("UAL"))
						logo.setImageResource(R.drawable.ual);
					else if (airline.equals("SWA"))
						logo.setImageResource(R.drawable.swa);
					else if (airline.equals("DAL"))
						logo.setImageResource(R.drawable.dal);
					else if (airline.equals("AWE"))
						logo.setImageResource(R.drawable.awe);
					else if (airline.equals("FFT"))
						logo.setImageResource(R.drawable.fft);
					else if (airline.equals("VRD"))
						logo.setImageResource(R.drawable.vrd);
					else if (airline.equals("ACA"))
						logo.setImageResource(R.drawable.aca);
					else if (airline.equals("WJA"))
						logo.setImageResource(R.drawable.wja);
					else
						logo.setImageResource(R.drawable.ic_launcher);
				}
				else
					logo.setImageResource(R.drawable.ic_launcher);
				
				return true;
			case R.id.flightaware:
				// if the entry has been flagged as using the API, display a small message saying this
				int XMLEnabled = cursor.getInt(cursor.getColumnIndex(FlightData.C_FLIGHTXML_ENABLED));
				
				if (XMLEnabled == 1){
					((TextView) view).setVisibility(View.VISIBLE);
					((TextView) view).setText(R.string.row_flightaware);
				}
				return true;

			}

			return false;
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent refreshIntent = new Intent(this, RefreshFlightDataService.class);
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			startService(refreshIntent);
			return true;
		case R.id.menu_prefs:
			startActivity(new Intent(this, TEMPPrefActivity.class));
		default:
			return false;
		}

	}

	// inner class receiver to listen for changes of data for this activity
	class MyFlightsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			boolean viewAll = PreferenceManager.getDefaultSharedPreferences(
					getApplicationContext())
					.getBoolean("viewAllFlights", false);
			Log.d(TAG, "Broadcast Received");
			cursor = MyFlightsApp.flightData.query(viewAll);
			adapter.changeCursor(cursor);
		}

	}

}
