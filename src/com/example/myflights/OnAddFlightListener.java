package com.example.myflights;

import java.util.List;

/* This callback interface routes List of Flights back to 
 * parent activity for processing
 */
public interface OnAddFlightListener {
	public void onAddFlightListener(List<FlightInfo> flights, FlightInfoExtended info);
}
