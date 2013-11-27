package com.example.myflights;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class FragmentAddFlight extends Fragment {

	public static String TAG = "FragmentAddFlight";
	static final String[] FROM = { AirportData.C_AIRPORT,
			AirportData.C_AIRPORT_NAME };
	static final String[] FROM2 = { AirlineData.C_AIRLINE,
			AirlineData.C_AIRLINE_NAME };
	static final int[] TO = { R.id.dropdown_text1, R.id.dropdown_text2 };

	AutoCompleteTextView origin;
	AutoCompleteTextView destination;
	AutoCompleteTextView airline;
	EditText flight;
	Button dateButton;

	static DataValidation valid;
	static boolean isValid = false;
	Calendar calendar;
	Cursor cursor;
	SimpleCursorAdapter adapterOrigin;
	SimpleCursorAdapter adapterDestination;
	SimpleCursorAdapter adapterAirline;
	Context context;
	ProgressDialog progressDialog;
	
	// current date chosen
	long date;
	
	//
	ListenerSetDate mCallback;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try{ 
			mCallback = (ListenerSetDate) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnDateSetListener Listener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_flight, container,
				false);

		// set activity context to getActivity()
		context = getActivity();

		origin = (AutoCompleteTextView) view.findViewById(R.id.Origin);
		destination = (AutoCompleteTextView) view
				.findViewById(R.id.Destination);
		airline = (AutoCompleteTextView) view.findViewById(R.id.Airline);
		flight = (EditText) view.findViewById(R.id.Flight);
		dateButton = (Button) view.findViewById(R.id.date_button);
		// set the min date to current date

		adapterOrigin = new SimpleCursorAdapter(getActivity(),
				R.layout.auto_complete_row, null, FROM, TO, 0);
		origin.setAdapter(adapterOrigin);
		new AutoCompleteAirportHelper(adapterOrigin);

		adapterDestination = new SimpleCursorAdapter(getActivity(),
				R.layout.auto_complete_row, null, FROM, TO, 0);
		destination.setAdapter(adapterDestination);
		new AutoCompleteAirportHelper(adapterDestination);

		adapterAirline = new SimpleCursorAdapter(getActivity(),
				R.layout.auto_complete_row, null, FROM2, TO, 0);
		airline.setAdapter(adapterAirline);
		new AutoCompleteAirlineHelper(adapterAirline);
		
		final Calendar c = Calendar.getInstance();
		date = c.getTimeInMillis();
		mCallback.setDate(date);
		String formattedDate = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.US).format(new Date(date));
		dateButton.setText(formattedDate);

		return view;
	}

	
	// called before onResume so button change reflected
	public void setDate(int year, int month, int day){
		final Calendar c = Calendar.getInstance();
		c.set(year, month, day, 0, 0, 0);
		date = c.getTimeInMillis();
		mCallback.setDate(date);
		String formattedDate = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.US).format(new Date(date));
		dateButton.setText(formattedDate);
	}

	public void onClicked() {
		String originText = getEditTextString(origin);
		String destinationText = getEditTextString(destination);
		String airlineText = getEditTextString(airline);
		String flightText = getEditTextString(flight);
		// take substring of long to get rid of last 3 numbers to convert to seconds after 1970 for API
		
		String dateText = Long.toString(date/1000);
		
		
		// TODO add time to date if a manual time is entered...

		// insert in new data passing in the context of the class
		insertDataAsync = new InsertDataAsync(context);
		// Explicitly define the async task to this task (in case it gets
		// destroyed)
		insertDataAsync.activityAddFlight = (ActivityAddFlight) getActivity();
		insertDataAsync.execute(originText, destinationText, dateText,
				airlineText, flightText);
	}

	// helper function to convert edittext fields into text
	public String getEditTextString(EditText data) {
		return data.getText().toString();
	}
	
	public void showDialog() {
	}

	// async task to run insert operation on separate thread and post success
	// message on completion
	// we want an explicit pointer, thus we need a static class so the thread
	// will complete in case the activity is cancelled early (Screen Rotation)
	static class InsertDataAsync extends AsyncTask<String, Void, String> {

		Context mContext;
		FragmentDialog dialog;

		public InsertDataAsync(Context context) {
			mContext = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// show dialog fragment
			FragmentManager fm = activityAddFlight.getFragmentManager();
			dialog = new FragmentDialog();
			dialog.show(fm, "fragment_dialog");
			
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
			// Dismiss the dialog
			dialog.dismiss();
			if (activityAddFlight != null) {
				Toast.makeText(activityAddFlight, result, Toast.LENGTH_LONG)
						.show();

				if (isValid) {
					mContext.startActivity(new Intent(mContext,
							ActivityMyFlights.class));
					activityAddFlight.finish();
				}

				isValid = false;
			}

		}

		ActivityAddFlight activityAddFlight = null;

	}

	private InsertDataAsync insertDataAsync = null;

}
