package com.example.myflights;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshFlightDataService extends IntentService {
	public static final String TAG = "RefreshFlightDataService";

	public RefreshFlightDataService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		new Thread() {
			public void run() {
				//try {
					//while (true) {
						Log.d(TAG, "onHandleIntent");
						int rowsAffected = MyFlightsApp.flightData
								.updateDeleted();
						Log.d(TAG, Integer.toString(rowsAffected));
						if (rowsAffected > 0)
							sendBroadcast(new Intent(
									MyFlightsApp.ACTION_REFRESH));

						//Thread.sleep(300000);
					//}
				//} catch (InterruptedException e) {
				//	e.printStackTrace();
				//}
			}
		}.start();

	}

}
