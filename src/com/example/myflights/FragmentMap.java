package com.example.myflights;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class FragmentMap extends Fragment {

	public static final String TAG = "FragmentMap";
	GoogleMap map, map2;
	MapFragment mapFragment;

	ActivityFlightInfo activityFlightInfo;

	List<AirportInfo> airportList = new ArrayList<AirportInfo>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container);

		setupMapIfNeeded();

		return view;
	}

	public void setupMapIfNeeded() {
		// if map does not exist, create it
		if (map == null) {

			mapFragment = (MapFragment) getFragmentManager().findFragmentById(
					R.id.map);
			map = mapFragment.getMap();

			// setup map if the map was successfully created
			if (map != null) {
				setupMap();
			}

		}
	}

	public void setupMap() {

		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		;
	}

	// ON ACTIVITY CREATE
	// AYNSROUNOUS UI TASK
	// draw points on the map where the airports are located.
	public void onDBLoaded(String origin, String destinatinon) {

		airportList.add(new AirportInfo(origin));
		airportList.add(new AirportInfo(destinatinon));

		// clear current markers if the already exist;
		map.clear();
		// draw the markers
		drawMarkers();
		// draw line(s)
		drawLines();
		
		// Pan to see all markers in view.
	    // Cannot zoom to bounds until the map has a size.
		final View mapView = getFragmentManager().findFragmentById(R.id.map).getView();
		// if the view layout is observed to be alive, add a listener to build bounds of map
		if (mapView.getViewTreeObserver().isAlive()){
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				
				@Override
				public void onGlobalLayout() {
					LatLngBounds.Builder build = new LatLngBounds.Builder();
					for (AirportInfo a : airportList) {
						build.include(new LatLng(a.getLatitude(), a.getLongitude()));
					}
					// build the bounds now
					LatLngBounds bounds= build.build();
					// now move the map camera to where relative to the bounds
					map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
					// remove the listener to prevent multiple triggers
					mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
			});
		}

	}

	private void drawMarkers() {

		for (AirportInfo a : airportList) {
			// get the lat and long and add markers to the maps
			LatLng position = new LatLng(a.getLatitude(), a.getLongitude());
			MarkerOptions options = new MarkerOptions();
			options.position(position);
			options.draggable(false);
			map.addMarker(options);
		}

	}

	private void drawLines() {

		PolylineOptions options = new PolylineOptions();
		options.geodesic(true);
		// iterate through each airport and get positions to draw lines between all points

		for (int i = 0; i < airportList.size() - 1; i++) {
			options.add(new LatLng(airportList.get(i).getLatitude(),
					airportList.get(i).getLongitude()), new LatLng(airportList
					.get(i + 1).getLatitude(), airportList.get(i + 1)
					.getLongitude()));
			map.addPolyline(options);
		}

	}

}
