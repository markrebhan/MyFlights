package com.example.myflights;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

public class ActivityAddFlight extends Activity  implements OnDateSetListener, ListenerSetDate{
	
	long date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_flight);

	}
	
	public void onClick(View v) {

		// use fragmentManager to find the fragment class to execute onclick post
		FragmentAddFlight fragment = (FragmentAddFlight) getFragmentManager().findFragmentById(R.id.fragment_add_flight);
		fragment.onClicked();
	}
	
	public void showDatePickerDialog(View v){
		
		// set the value of the dialog date to what is currently showing on date button
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
		FragmentAddFlight fragment = (FragmentAddFlight) getFragmentManager().findFragmentById(R.id.fragment_add_flight);
		fragment.setDate(year, monthOfYear, dayOfMonth);
	}

	
	// user listener to set the date for dialog to show
	@Override
	public void setDate(long date) {
		this.date = date;
	}

}
