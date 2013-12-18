package com.example.myflights;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomRowArrayAdapter extends ArrayAdapter<FlightInfo> {


	private final LayoutInflater inflater;

	// inner class to cache views of the item
	private static class ViewHolder {
		private TextView flight;
		private TextView departTime;
		private ImageView logo;

		ViewHolder() {
		};
	}

	public CustomRowArrayAdapter(Context context, int resource, List<FlightInfo> objects) {
		super(context, resource, objects);
		// get the inflated view from the context
		this.inflater = LayoutInflater.from(context);
	}
	
	// override the getView function to map views to data
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		ViewHolder holder = null;
		// get the flightinfo item from arrayAdapter
		final FlightInfo flightInfo = getItem(position);

		if (itemView == null) {
			itemView = this.inflater.inflate(R.layout.row2, parent, false);

			holder = new ViewHolder();
			holder.flight = (TextView) itemView.findViewById(R.id.flight2);
			holder.departTime = (TextView) itemView.findViewById(R.id.depart2);
			holder.logo = (ImageView) itemView.findViewById(R.id.airline_logo2);

			// TAG this view with a holder object
			itemView.setTag(holder);
		}
		// get the tag of the holder if it already exists in the adapter
		else
			holder = (ViewHolder) itemView.getTag();
		
		

		// set the text to the appropriate info in flightInfo object
		holder.flight.setText(flightInfo.getAirline() + flightInfo.getFlight());
		// get the date from object and convert to correct date format
		holder.departTime.setText(new SimpleDateFormat("MM/dd hh:mm aaa", Locale.US)
				.format(new Date(
						Long.parseLong(flightInfo.getDepartTime()) * 1000)));
		
		

		// find the image in the drawable folder based on airline and draw, else
		// use default image
		int resID = getContext().getResources().getIdentifier(
				flightInfo.getAirline().toLowerCase(Locale.US), "drawable",
				getContext().getPackageName());
		if (resID > 0)
			holder.logo.setImageResource(resID);
		// if unable to find image for airline, set to default
		else
			holder.logo.setImageResource(R.drawable.ic_launcher);

		return itemView;
	}

}
