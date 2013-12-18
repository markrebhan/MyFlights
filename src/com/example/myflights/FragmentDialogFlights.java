package com.example.myflights;

import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentDialogFlights extends DialogFragment {

	ListView list;
	List<FlightInfo> flights = MyFlightsApp.flights;
	OnSelectFlightAdd mCallback;

	public FragmentDialogFlights() {
	}; // Empty constructor required for DialogFragment

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// make sure that the container activity has implemented the callback
		// interface
		// when attaching to the activity
		try {
			mCallback = (OnSelectFlightAdd) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSelectFlightAdd Listener");
		}

	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dialog_flights,
				container);
		// TODO Make the title a custom view
		getDialog().setTitle(
				flights.get(0).getOrigin() + " -> "
						+ flights.get(0).getDestination());

		// use the custom array adapter to populate listview of dialog
		list = (ListView) view.findViewById(R.id.dialog_flight_lists);
		final CustomRowArrayAdapter arrayAdapter = new CustomRowArrayAdapter(
				getActivity(), R.layout.row2, flights);
		list.setAdapter(arrayAdapter);

		// TODO address setonclick
		list.setOnItemClickListener(new OnItemClickListener() {

			// Retreive all the the flight information on click and pass back to
			// activity via callback
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// the arrayadapter view corresponds to items in list in terms
				// of positions
				FlightInfo info = (FlightInfo) parent
						.getItemAtPosition(position);
				FlightInfoExtended finalInfo = new FlightInfoExtended(info.getOrigin(), info.getDestination(), info.getDepartTime(),
						info.getArrivalTime(), info.getAirline(), info.getFlight(), 1);
				//TODO MOVE THIS
				int id2 = MyFlightsApp.flightData.queryEntityID(info.getOrigin(), "airports", "airport");
				finalInfo.setOriginCode(id2);
				int id3 = MyFlightsApp.flightData.queryEntityID(info.getDestination(), "airports", "airport");
				finalInfo.setDestinationCode(id3);
				mCallback.onSelectFlightAdd(finalInfo);

			}

		});

		return view;
	}

}
