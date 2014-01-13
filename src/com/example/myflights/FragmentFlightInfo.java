package com.example.myflights;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentFlightInfo extends Fragment {

	public final static String TAG = "FragmentFlightInfo";

	TextView origin, originName, destination, destinationName, departTime,
			departDate, arriveTime, arriveDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_flight_info, container,
				false);

		// define text views in code
		origin = (TextView) view.findViewById(R.id.origin_flight_info);
		originName = (TextView) view.findViewById(R.id.origin_name_flight_info);
		destination = (TextView) view
				.findViewById(R.id.destination_flight_info);
		destinationName = (TextView) view
				.findViewById(R.id.destination_name_flight_info);
		departTime = (TextView) view.findViewById(R.id.depart_flight_info);
		departDate = (TextView) view.findViewById(R.id.date_flight_info);
		arriveTime = (TextView) view.findViewById(R.id.arrive_flight_info);
		arriveDate = (TextView) view.findViewById(R.id.date_arrive_flight_info);

		return view;
	}

	public void refreshViews(Cursor cursor) {
		
		boolean hasData = cursor.moveToFirst();
		if (hasData) {
			origin.setText(cursor.getString(cursor
					.getColumnIndex(FlightData.C_ORIGIN)));
			originName.setText(cursor.getString(cursor
					.getColumnIndex(FlightData.C_ORIGIN_NAME)));
			destination.setText(cursor.getString(cursor
					.getColumnIndex(FlightData.C_DESTINATION)));
			destinationName.setText(cursor.getString(cursor
					.getColumnIndex(FlightData.C_DESTINATION_NAME)));
			
			long depart = cursor.getLong(cursor.getColumnIndex(FlightData.C_DEPART_TIME))*1000;
			long arrive = cursor.getLong(cursor.getColumnIndex(FlightData.C_ARRIVAL_TIME))*1000;
			
			// get timezone info that was added to DB query 1/12
			String timezoneOrigin = cursor.getString(cursor.getColumnIndex(AirportData.C_TIMEZONE));
			String timezoneDestination = cursor.getString(cursor.getColumnIndex(AirportData.C_TIMEZONE + "2"));
			
			String dDate = new TimeConvert(depart, "EEEE, MM/dd/yy", timezoneOrigin).convertTime();
			String aDate = new TimeConvert(arrive, "EEEE, MM/dd/yy", timezoneDestination).convertTime();
			
			
			departTime.setText(new TimeConvert(depart, "hh:mm aaa z", timezoneOrigin).convertTime());
			departDate.setText(dDate);
			arriveTime.setText(new TimeConvert(arrive, "hh:mm aaa z", timezoneDestination).convertTime());
			// if the arrival date is different than the departure date, display the arrival date
			if(!dDate.equals(aDate)) arriveDate.setText(aDate);
			
			
		}
		else origin.setText("Error");

	}
}
