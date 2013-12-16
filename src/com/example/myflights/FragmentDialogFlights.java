package com.example.myflights;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentDialogFlights extends DialogFragment {

	ListView list;
	
	public FragmentDialogFlights() {}; // Empty constructor required for DialogFragment

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_flights_list, container);
		list = (ListView) view.findViewById(R.id.flight_lists);
		list.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int positions, View convertView, ViewGroup parent) {
				View row = null;
				if (convertView == null){
					row = inflater.inflate(R.layout.row2, parent, false);
					
				}
				
				
				else row = convertView;
				TextView textView = (TextView) row.findViewById(R.id.flight2);
				switch (positions) {	
			case 0: textView.setText("Random"); break;
	        case 1: textView.setText("Community favourites"); break;
	        case 2: textView.setText("Change image"); break;
	        case 3: textView.setText("Share"); break;
	        case 4: textView.setText("Informations"); break;
				}
				return row;
			}
			
			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		return view;
	}

}
