package com.example.myflights;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParsing {
	public static final String TAG = "JSONParsing";

	public static FlightInfo parseScheduledFlight(JSONObject response,
			FlightInfo info) {
		// add any or change info if flightXML requires it (source of truth)

		try {
			
			//JSON object is AirlineFLightScheduleResult -> data -> array of data
			response = response.getJSONObject("AirlineFlightSchedulesResult");
			JSONArray FlightXMLInfo = response.getJSONArray("data");

			if (FlightXMLInfo.length() > 0) {
				info.setFlightXMLEnabled(1);
				
				JSONObject obj = FlightXMLInfo.getJSONObject(0);
				//add 3 zeros to keep all timestamp values consistent with Android 
				info.setDepartTime(obj.getString("departuretime"));
				info.setArrivalTime(obj.getString("arrivaltime"));
				
				String temp =obj.getString("actual_ident");
				if(temp.equals("")) temp = obj.getString("ident"); 
				info.setAirline(temp.substring(0, 3));
				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return info;
	}
	
	
	public static List<String> ParseAirlineCodes(JSONObject response){
		List<String> airlines = new ArrayList<String>();
		try{
			response = response.getJSONObject("AllAirlinesResult");
			JSONArray FlightXMLAirport = response.getJSONArray("data");
			for (int i = 0; i < FlightXMLAirport.length(); i++) {
				String airportCode = FlightXMLAirport.getString(i);
				airlines.add(airportCode); 
				
			}
			
		}catch (JSONException e) {
			e.printStackTrace();
		}

		return airlines;
		
	}

}
