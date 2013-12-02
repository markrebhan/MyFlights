package com.example.myflights;

import org.json.JSONObject;

import android.util.Log;

public class RESTfulCalls {
	public static final String TAG = "RESTful Calls";
	public static final String URL = "http://flightxml.flightaware.com/json/FlightXML2/";

	public RESTfulCalls() {
	}

	// look for flight with API
	public JSONObject findFlightXML(String origin, String destination,
			String date, String flight) {
		// leave airline out for now

		// using AirlineFlightSchedules method
		/*
		 * startDate (Required) endDate (Required) origin (Optional Yes)
		 * destination (Optional Yes) airline (Optional No) flightno (Optional
		 * Yes) howMany (1) offset (0)
		 */

		// set up proper date format for webcall
		long sDate = Long.parseLong(date);
		long eDate = sDate + 100000;

		String sDateS = Long.toString(sDate);
		String eDateS = Long.toString(eDate);

		String method = "AirlineFlightSchedules";
		String[] inName = { "startDate", "endDate", "origin", "destination",
				"flightno", "howMany", "offset" };
		String[] inValue = { sDateS, eDateS, origin, destination, flight, "1",
				"0" };

		String finalUrL = buildURL(method, inName, inValue);

		JSONObject response = RequestWebService.requestWebService(finalUrL);

		return response;

	}

	public JSONObject flightInfoEx(String airline, String flightno){
		/*FlightInfoEx
		 * Input - identity (airline + flightno) 
		 * howMany -> offset of how many previous flights to query for (1)
		 * Output (FlightInfoExStruct)
		 */
		
		String method = "FlightInfoEx";
		String[] inName = {"ident", "howMany", "offset"};
		String[] inValue = { airline + flightno, "5", "0"};
		
		String finalUrl = buildURL(method, inName, inValue);
		
		JSONObject response = RequestWebService.requestWebService(finalUrl);
		
		return response;
		
		
	}
	
	// get latest weather conditions
	public JSONObject MetarEx(String airport){
		String method = "MetarEx";
		String [] inName = {"airport", "startTime", "howMany", "offset"};
		String [] inValue = {airport, "0", "1", "0"};
		
		String finalUrl = buildURL(method, inName, inValue);
		return RequestWebService.requestWebService(finalUrl);
	}

	// method to build REST URLs
	private String buildURL(String method, String[] inputName,
			String[] inputValue) {
		StringBuilder finalURL = new StringBuilder(URL);
		finalURL.append(method);

		if (inputName != null) {
			finalURL.append("?");

			for (int i = 0; i < inputName.length; i++) {
				finalURL.append(inputName[i]);
				finalURL.append("=");
				finalURL.append(inputValue[i]);
				if (i < inputName.length - 1)
					finalURL.append("&");
			}
		}

		Log.d(TAG, "URL used: " + finalURL.toString());

		return finalURL.toString();

	}

}
