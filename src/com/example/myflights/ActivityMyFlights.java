package com.example.myflights;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ActivityMyFlights extends Activity implements OnDeleteFlightSelectedListener, OnFlightSelectedListener{

	public static final String TAG = "ActivityMyFlights";
	public static final String name = "com.example.myflights.flightID";
	ReceiverRefreshListData receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_flights);
		startService(new Intent(this, RefreshFlightDataService.class));
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (receiver == null)
			receiver = new ReceiverRefreshListData();
		registerReceiver(receiver,
				new IntentFilter(MyFlightsApp.ACTION_REFRESH));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent refreshIntent = new Intent(this, RefreshFlightDataService.class);
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			startService(refreshIntent);
			return true;
		case R.id.menu_add:
			startActivity(new Intent(this, ActivityAddFlight.class));
			return true;
		case R.id.menu_prefs:
			startActivity(new Intent(this, TEMPPrefActivity.class));
			/*PreferencesFragment prefFrag = new PreferencesFragment();
			FragmentTransaction trans = getFragmentManager().beginTransaction();
			trans.replace(R.id.fragment_body, prefFrag);
			trans.addToBackStack("Flight List");
			trans.commit();*/
			
			return true;
		///case R.id.menu_view_all:
			//startActivity(new Intent(this, ActivityFlightInfo.class));
		default:
			return false;
		}

	}

	// OnFlightsSelected listener callback
	@Override
	public void onDeleteFlightSelected(int id, String airline, String flight) {
		
		// create a bundle to send data to dialog fragment
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		bundle.putStringArray("StringArray", new String [] {airline, flight});
		
		FragmentManager fm = getFragmentManager();
		FragmentDialogDelete dialog = new FragmentDialogDelete();
		dialog.setArguments(bundle);
		dialog.show(fm, "fragment_dialog_delete");
		
	}

	@Override
	public void onFlightSelectedListener(int dbID) {		
		
		startActivity(new Intent(this, ActivityFlightInfo.class).putExtra(name, dbID));
		
	}

	

}
