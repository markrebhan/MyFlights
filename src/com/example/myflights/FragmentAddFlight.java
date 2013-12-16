package com.example.myflights;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.google.android.gms.internal.gc;

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
	// reference interface back to activity to pass the data.
	OnAddFlightListener mCallbackAdd;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (ListenerSetDate) activity;
			mCallbackAdd = (OnAddFlightListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ListenerSetDate or OnFlightSelectedListener");
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
		String formattedDate = new SimpleDateFormat("EEE, MMM dd yyyy",
				Locale.US).format(new Date(date));
		dateButton.setText(formattedDate);

		return view;
	}

	// called before onResume so button change reflected
	public void setDate(int year, int month, int day) {
		final Calendar c = Calendar.getInstance();
		c.set(year, month, day, 0, 0, 0);
		date = c.getTimeInMillis();
		mCallback.setDate(date);
		String formattedDate = new SimpleDateFormat("EEE, MMM dd yyyy",
				Locale.US).format(new Date(date));
		dateButton.setText(formattedDate);
	}

	public void onClicked() {
		String originText = getEditTextString(origin);
		String destinationText = getEditTextString(destination);
		String airlineText = getEditTextString(airline);
		String flightText = getEditTextString(flight);
		// take substring of long to get rid of last 3 numbers to convert to
		// seconds after 1970 for API

		String dateText = Long.toString(date / 1000);

		// TODO add time to date if a manual time is entered...

		// insert in new data passing in the context of the class
		getDataAsync = new GetDataAsync(context, mCallbackAdd);
		// Explicitly define the async task to this task (in case it gets
		// destroyed)
		getDataAsync.activityAddFlight = (ActivityAddFlight) getActivity();
		getDataAsync.execute(originText, destinationText, dateText,
				airlineText, flightText);
	}

	// helper function to convert edittext fields into text
	public String getEditTextString(EditText data) {
		return data.getText().toString();
	}

	public void showDialog() {
	}

	/* 
	 * An asynctask to make the REST call and save the results in a list and pass to host activity for
	 * further processing
	 */
	// we want an explicit pointer, thus we need a static class so the thread
	// will complete in case the activity is cancelled early (Screen Rotation)
	static class GetDataAsync extends AsyncTask<String, Void, String> {

		Context mContext;
		FragmentDialog dialog;
		List<FlightInfo> allPossibleFlights;
		FlightInfoExtended info;
		OnAddFlightListener mCallbackAdd;
		DataValidation valid;
		boolean isValid = false;
		
		public GetDataAsync(Context context, OnAddFlightListener mCallbackAdd) {
			this.mContext = context;
			this.mCallbackAdd = mCallbackAdd;
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

				info = new FlightInfoExtended(params[0],
						params[1], params[2], null, params[3], params[4], 0);
				// validate data
				valid = new DataValidation(info);
				String badData = valid.validate();

				if (badData == null) {

					// valid entry we can move to next activity
					isValid = true;

					// if (info.getDepartTime() )
					// make webservice call
					// returns a json object with an array of size one in it,
					// there is no array (IE no "[" ) if there is an error or no
					// data
					RESTfulCalls webcall = new RESTfulCalls();

					allPossibleFlights = JSONParsing
							.parseScheduledFlight(webcall.findFlightXML(
									info.getOrigin(), info.getDestination(),
									info.getDepartTime(), info.getFlight()));

					/*if (allPossibleFlights.size() > 0) {
						FlightInfo TEMP = allPossibleFlights.get(0);
						info.setFlightXMLEnabled(1);
						info.setAirline(TEMP.getAirline());
						info.setFlight(TEMP.getFlight());
						info.setDepartTime(TEMP.getDepartTime());
						info.setArrivalTime(TEMP.getArrivalTime());
						// TODO THIS WILL MOVE TO NEW DIALOG TO PICK CORRECT
						// FLIGHT;
						// set airline and flight from json response;
						int id = MyFlightsApp.flightData.queryEntityID(
								info.getAirline(), "airlines", "airline");
						info.setAirlineCode(id);
					}

					MyFlightsApp.flightData.insertData(info.getOriginCode(),
							info.getDestinationCode(), info.getDepartTime(),
							info.getArrivalTime(), info.getAirlineCode(),
							info.getFlight(), info.getFlightXMLEnabled());*/
					// return null if there were no errors
					
					
					return null;
				} else {
					return badData;
				}
			} catch (Exception e) {
				e.printStackTrace();
				// return error
				return e.toString();
			}

		}

		// UI Thread
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Dismiss the dialog
			dialog.dismiss();

			if (activityAddFlight != null) {
				
				// callback to activity if no errors have occurred
				if (isValid) {
					
					mCallbackAdd.onAddFlightListener(allPossibleFlights, info);
					
				}
				else{
					// toast the error to UI for user to digest
					Toast.makeText(activityAddFlight, result, Toast.LENGTH_LONG)
					.show();
				}

				isValid = false;
			}

		}

		ActivityAddFlight activityAddFlight = null;

	}

	private GetDataAsync getDataAsync = null;

}
