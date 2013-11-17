package com.example.myflights;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RefreshDeletedReceiver extends BroadcastReceiver {
	public final static String TAG = "RefreshDeleted";

	@Override
	public void onReceive(Context context, Intent intent) {

		long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
		PendingIntent operation = PendingIntent.getService(context, -1,
				new Intent(context, RefreshFlightDataService.class),
				PendingIntent.FLAG_UPDATE_CURRENT); 
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				System.currentTimeMillis()/1000, interval, operation);
		//alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(), interval, operation);
		
		//context.startService( new Intent(context, RefreshFlightDataService.class));
		Log.d(TAG,"onReceived Receiver");

	}

}
