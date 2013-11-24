package com.example.myflights;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class ActivityMyFlights extends Activity implements OnFlightSelectedListener{

	public static final String TAG = "ActivityMyFlights";
	ReceiverRefreshListData receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
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

	// onClick XML attribute from TitleBar Fragment
	public void onClick(View view) {
		startActivity(new Intent(this, ActivityAddFlight.class));

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
		case R.id.menu_prefs:
			startActivity(new Intent(this, TEMPPrefActivity.class));
		default:
			return false;
		}

	}

	// OnFlightsSelected listener callback
	@Override
	public void onFlightSelected(int id, String airline, String flight) {
		
		// create a bundle to send data to dialog fragment
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		bundle.putStringArray("StringArray", new String [] {airline, flight});
		
		FragmentManager fm = getFragmentManager();
		FragmentDialogDelete dialog = new FragmentDialogDelete();
		dialog.setArguments(bundle);
		dialog.show(fm, "fragment_dialog_delete");
		
	}

}
