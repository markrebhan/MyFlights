package com.example.myflights;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class FragmentMap extends Fragment{

	public static final String TAG = "FragmentMap";
	GoogleMap map;
	SupportMapFragment mapFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container);
        
		return view;
	}

	
	
	

}
