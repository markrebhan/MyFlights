package com.example.myflights;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class FragmentDialog extends DialogFragment{

	public FragmentDialog(){}; // Empty constructor required for DialogFragment
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dialog, container);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCancelable(false);
		return view;
	}
	
	

}
