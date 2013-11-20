package com.example.myflights;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ActivityAddFlight extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_flight);

	}
	
	public void onClick(View v) {

		// use fragmentManager to find the fragment class to execute onclick stuff
		FragmentAddFlight fragment = (FragmentAddFlight) getFragmentManager().findFragmentById(R.id.fragment_add_flight);
		fragment.onClicked();

	}

}
