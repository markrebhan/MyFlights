package com.example.myflights;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;

public class FragmentDatePicker extends DialogFragment {

	public final static String TAG = "FragmentDatePicker";
	OnDateSetListener mCallback;

	
	String date = "date";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnDateSetListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnDateSetListener Listener");
		}

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// get bundle with the current date selected in button
		long dateInMilli = getArguments().getLong(date);
		
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(dateInMilli);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);

		return new DatePickerDialog(getActivity(), mCallback, year, month, day);
	}

}
