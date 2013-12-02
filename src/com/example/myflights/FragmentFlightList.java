package com.example.myflights;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentFlightList extends Fragment {
	public static final String TAG = "FragmentFlightList";

	static final String[] FROM = { FlightData.C_ORIGIN,
			FlightData.C_ORIGIN_NAME, FlightData.C_DESTINATION,
			FlightData.C_DESTINATION_NAME, FlightData.C_DEPART_TIME,
			FlightData.C_ARRIVAL_TIME, FlightData.C_AIRLINE,
			FlightData.C_FLIGHT, FlightData.C_STATUS,
			FlightData.C_FLIGHTXML_ENABLED};
	static final int[] TO = { R.id.from, R.id.from_name, R.id.to, R.id.to_name,
			R.id.depart, R.id.arrive, R.id.airline_logo, R.id.flight,
			R.id.status, R.id.flightaware};

	public static Cursor cursor;
	public static SimpleCursorAdapter adapter;
	
	ImageView airlineLogo;
	ListView list;
	
	// create callback Interface object to listen for onLongClick to pass data to Activity
	static OnDeleteFlightSelectedListener mCallback;
	// create callback interface to listener for when onClick occurs
	static OnFlightSelectedListener mCallback2;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// make sure that the container activity has implemented the callback interface
		// when attaching to the activity
		try {
			mCallback = (OnDeleteFlightSelectedListener) activity;
			mCallback2 = (OnFlightSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnFlightSelected Listener");
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_flights_list, container,
				false);
		
		list = (ListView) view.findViewById(R.id.flight_lists);
		
		return view;
	}
	
	// set adapter to the cursor query and call view binder to display items in desired form
	@Override
	public void onResume() {
		super.onResume();
		boolean viewAll = PreferenceManager.getDefaultSharedPreferences(
				getActivity().getApplicationContext()).getBoolean("viewAllFlights", false);
		cursor = MyFlightsApp.flightData.query(viewAll);
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.row, cursor, FROM, TO, 0) {

			// alternating colors this way makes it laggy
			/*@Override 
			public View getView(int position, View view, ViewGroup viewGroup) {
				final View row = super.getView(position, view, viewGroup);
				if(position % 2 == 0) row.setBackgroundColor(getResources().getColor(R.color.my_blue2));
				else row.setBackgroundColor(getResources().getColor(R.color.my_blue3));
				return row;
					
			}*/
			
		};
		adapter.setViewBinder(VIEW_BINDER);
		list.setAdapter(adapter);
		
		// set up a listener for click and hold and normal click
		list.setOnItemLongClickListener(new ListenerDeleteItem());
		list.setOnItemClickListener(new ListenerViewItem());

			
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
				if (XMLEnabled == 1) ((TextView) view).setText(R.string.row_flightaware);
				return true;

			}

			return false;
		}

	};

}
