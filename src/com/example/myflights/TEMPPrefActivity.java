package com.example.myflights;

import android.app.Activity;
import android.os.Bundle;

public class TEMPPrefActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
	}
	
	

}
