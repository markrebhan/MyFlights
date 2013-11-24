package com.example.myflights;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class FragmentDialogDelete extends DialogFragment {

	public static final String TAG = "FragmentDialogDelete";
	
	public FragmentDialogDelete() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retrieve bunde data from parent activity
		String [] stringArray = getArguments().getStringArray("StringArray");
		final int id = getArguments().getInt("id");
		Log.d(TAG, Integer.toString(id));
		
		// build a dialog UI using builder class
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_delete_title);
		builder.setMessage("Permanently Delete " + stringArray[0] + stringArray[1] + "?" );
		builder.setPositiveButton(R.string.dialog_delete_delete, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MyFlightsApp.flightData.userDeletesRecord(id);
				getActivity().sendBroadcast(new Intent(MyFlightsApp.ACTION_REFRESH));
			}
		});
		
		// on a cancel, dismiss dialog.
		builder.setNegativeButton(R.string.dialog_delete_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
				
			}
		});
		
		return builder.create();
	};

}
