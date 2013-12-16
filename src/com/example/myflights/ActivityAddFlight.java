package com.example.myflights;

import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.Toast;

public class ActivityAddFlight extends Activity implements OnDateSetListener,
		ListenerSetDate, OnAddFlightListener {

	public static final String TAG = "ActivityAddFlight";
	
	long date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_flight);

	}

	public void onClick(View v) {

		// use fragmentManager to find the fragment class to execute onclick
		// post
		FragmentAddFlight fragment = (FragmentAddFlight) getFragmentManager()
				.findFragmentById(R.id.fragment_add_flight);
		fragment.onClicked();
	}

	public void showDatePickerDialog(View v) {

		// set the value of the dialog date to what is currently showing on date
		// button
		Bundle bundle = new Bundle();
		bundle.putLong("date", date);

		FragmentManager fm = getFragmentManager();
		FragmentDatePicker datePicker = new FragmentDatePicker();
		datePicker.setArguments(bundle);
		datePicker.show(fm, "fragment_date_picker");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		FragmentAddFlight fragment = (FragmentAddFlight) getFragmentManager()
				.findFragmentById(R.id.fragment_add_flight);
		fragment.setDate(year, monthOfYear, dayOfMonth);
	}

	// user listener to set the date for dialog to show
	@Override
	public void setDate(long date) {
		this.date = date;
	}

	@Override
	public void onAddFlightListener(List<FlightInfo> flights,
			FlightInfoExtended info) {

		Log.d(TAG, "MESSAGE RECEIVED");
		
		/*
		 * Pass in the flightinfoextended object that was created earlier
		 * with an necessary modifications to the asynctask to add the 
		 * data into the DB
		 */
		insertDataAsync = new InsertDataAsync(this);
		insertDataAsync.activityAddFlight = this;

		// CASE 1: no results returned from WebCall
		if (flights.size() == 0) {
			insertDataAsync.execute(info);
		// CASE 2: Only 1 flight returned from results of WebCall
		} else if (flights.size() == 1) {
			FlightInfo TEMP = flights.get(0);
			info.setFlightXMLEnabled(1);
			info.setAirline(TEMP.getAirline());
			info.setFlight(TEMP.getFlight());
			info.setDepartTime(TEMP.getDepartTime());
			info.setArrivalTime(TEMP.getArrivalTime());
			
			insertDataAsync.execute(info);
		}
		// CASE 3: Multiple flights returned from WebCall
		else{
			
		}

	}

	/*
	 * An asynctask to insert flight data into DB
	 */
	// we want an explicit pointer, thus we need a static class so the thread
	// will complete in case the activity is cancelled early (Screen Rotation)
	static class InsertDataAsync extends
			AsyncTask<FlightInfoExtended, Void, String> {

		Context mContext;
		FlightInfoExtended info;

		public InsertDataAsync(Context context) {
			this.mContext = context;
		}

		@Override
		protected String doInBackground(FlightInfoExtended... params) {
			try {

				info = params[0];
				
				// find the airline id in DB before inserting into DB
				// NOTE airport id's were set when validating them
				int id = MyFlightsApp.flightData.queryEntityID(info.getAirline(),
						"airlines", "airline");
				info.setAirlineCode(id);
				
				MyFlightsApp.flightData.insertData(info.getOriginCode(),
						info.getDestinationCode(), info.getDepartTime(),
						info.getArrivalTime(), info.getAirlineCode(),
						info.getFlight(), info.getFlightXMLEnabled());
				return "Successfully Added " + info.getAirline() + info.getFlight();

			} catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}

		}

		// UI Thread
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (activityAddFlight != null) {

				Toast.makeText(activityAddFlight, result, Toast.LENGTH_LONG)
						.show();
				mContext.startActivity(new Intent(mContext,
				ActivityMyFlights.class));
				activityAddFlight.finish();
			}

		}
		ActivityAddFlight activityAddFlight = null;

	}
	private InsertDataAsync insertDataAsync = null;

}
