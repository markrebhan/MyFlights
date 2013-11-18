package com.example.myflights;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class AddFlightActivity extends Activity {
	public static String TAG = "AddFlightActivity";
	static final String[] FROM = { AirportData.C_AIRPORT,
			AirportData.C_AIRPORT_NAME };
	static final String[] FROM2 = { AirlineData.C_AIRLINE,
			AirlineData.C_AIRLINE_NAME };
	static final int[] TO = { R.id.dropdown_text1, R.id.dropdown_text2 };

	AutoCompleteTextView origin;
	AutoCompleteTextView destination;
	AutoCompleteTextView airline;
	DatePicker date;
	EditText flight;

	static DataValidation valid;
	static boolean isValid = false;
	Calendar calendar;
	Cursor cursor;
	SimpleCursorAdapter adapterOrigin;
	SimpleCursorAdapter adapterDestination;
	SimpleCursorAdapter adapterAirline;
	Context context;
	ProgressDialog progressDialog;
	private static final int DIALOG1_KEY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_flight);

		// set activity context to this
		context = this;

		origin = (AutoCompleteTextView) findViewById(R.id.Origin);
		destination = (AutoCompleteTextView) findViewById(R.id.Destination);
		airline = (AutoCompleteTextView) findViewById(R.id.Airline);
		flight = (EditText) findViewById(R.id.Flight);
		date = (DatePicker) findViewById(R.id.Date);

		adapterOrigin = new SimpleCursorAdapter(this,
				R.layout.auto_complete_row, null, FROM, TO, 0);
		origin.setAdapter(adapterOrigin);
		new AutoCompleteAirportHelper(adapterOrigin);

		adapterDestination = new SimpleCursorAdapter(this,
				R.layout.auto_complete_row, null, FROM, TO, 0);
		destination.setAdapter(adapterDestination);
		new AutoCompleteAirportHelper(adapterDestination);

		adapterAirline = new SimpleCursorAdapter(this,
				R.layout.auto_complete_row, null, FROM2, TO, 0);
		airline.setAdapter(adapterAirline);
		new AutoCompleteAirlineHelper(adapterAirline);

	}

	public void onClick(View v) {

		String originText = getEditTextString(origin);
		String destinationText = getEditTextString(destination);
		String airlineText = getEditTextString(airline);
		String flightText = getEditTextString(flight);

		int year = date.getYear();
		int month = date.getMonth();
		int day = date.getDayOfMonth();
		calendar = new GregorianCalendar(year, month, day);
		String dateText = Long.toString(calendar.getTimeInMillis() / 1000);

		// TODO add time to date if a manual time is entered...
		
		
		// insert in new data passing in the context of the class
		insertDataAsync = new InsertDataAsync(context);
		// Explicitly define the async task to this task (in case it gets
		// destroyed)
		insertDataAsync.addFlightActivity = this;
		insertDataAsync.execute(originText, destinationText, dateText,
				airlineText, flightText);

	}

	// helper function to convert edittext fields into text
	public String getEditTextString(EditText data) {
		return data.getText().toString();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 0) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Adding Flight");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		} else
			return null;

	}

	// async task to run insert operation on separate thread and post success
	// message on completion
	// we want an explicit pointer, thus we need a static class so the thread
	// will complete in case the activity is cancelled early (Screen Rotation)
	static class InsertDataAsync extends AsyncTask<String, Void, String> {

		Context mContext;

		public InsertDataAsync(Context context) {
			mContext = context;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			addFlightActivity.showDialog(DIALOG1_KEY);
		}



		@Override
		protected String doInBackground(String... params) {
			try {

				FlightInfo info = new FlightInfo(params[0], params[1],
						params[2], null, params[3], params[4], 0);
				// validate data
				valid = new DataValidation(info);
				String badData = valid.validate();

				if (badData == null) {

					
					// valid entry we can move to next activity
					isValid = true;

					// if (info.getDepartTime() )
					// make webservice call
					RESTfulCalls webcall = new RESTfulCalls();
					// returns a json object with an array of size one in it,
					// there is no array (IE no "[" ) if there is an error or no
					// data
					JSONObject response = webcall.findFlightXML(
							info.getOrigin(), info.getDestination(),
							info.getDepartTime(), info.getFlight());

					Log.d(TAG, response.toString());

					// check to see if there is an entry in flight XML and
					// update and appropriate values (exact times, airline, etc)
					info = JSONParsing.parseScheduledFlight(response, info);

					MyFlightsApp.flightData.insertData(info.getOriginCode(),
							info.getDestinationCode(), info.getDepartTime(),
							info.getArrivalTime(), info.getAirlineCode(),
							info.getFlight(), info.getFlightXMLEnabled());

					return "Successfully added " + info.getAirline()
							+ " flight " + info.getFlight();
				} else {
					return badData;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "Failed to add " + params[3] + " flight " + params[4]
						+ " due to unknown error.";
			}

		}

		// UI Thread
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// if the current activity is not null, print the message
			addFlightActivity.removeDialog(DIALOG1_KEY);
			if (addFlightActivity != null) {
				Toast.makeText(addFlightActivity, result, Toast.LENGTH_LONG)
						.show();

				if (isValid) {
					mContext.startActivity(new Intent(mContext,
							MyFlightsActivity.class));
					addFlightActivity.finish();
				}

				isValid = false;
			}

		}

		AddFlightActivity addFlightActivity = null;

	}

	private InsertDataAsync insertDataAsync = null;
}
