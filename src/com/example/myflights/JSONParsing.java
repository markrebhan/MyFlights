package com.example.myflights;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParsing {
	public static final String TAG = "JSONParsing";

	// get a list of flightinfo
	public static List<FlightInfo> parseScheduledFlight(JSONObject response) {
		// add any or change info if flightXML requires it (source of truth)

		List<FlightInfo> flightList = new ArrayList<FlightInfo>();
		try {

			// JSON object is AirlineFLightScheduleResult -> data -> array of
			// data
			response = response.getJSONObject("AirlineFlightSchedulesResult");
			JSONArray FlightXMLInfo = response.getJSONArray("data");

			
			for (int i = 0; i < FlightXMLInfo.length(); i++) {
				// get the ith JSONobject in the array
				JSONObject obj = FlightXMLInfo.getJSONObject(i);
				
				// parse ident data into flight and number
				String ident = obj.getString("actual_ident");
				if (ident.equals(""))
					ident = obj.getString("ident");
				// create a new flightinfo base object and add the entry into it
				FlightInfo info = new FlightInfo(obj.getString("origin"),
						obj.getString("destination"),
						obj.getString("departuretime"),
						obj.getString("arrivaltime"), ident.substring(0, 3),
						ident.substring(4));
				
				flightList.add(info);

				

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return flightList;
	}

	public static WeatherInfo parseWeatherData(JSONObject response) {

		WeatherInfo weatherInfo = null;

		try {
			response = response.getJSONObject("MetarExResult");
			JSONArray rawWeatherData = response.getJSONArray("metar");

			JSONObject obj = rawWeatherData.getJSONObject(0);

			weatherInfo = new WeatherInfo(obj.getInt("temp_air"),
					obj.getString("cloud_friendly"),
					obj.getString("conditions"), obj.getInt("wind_speed"),
					obj.getInt("wind_direction"), obj.getLong("time"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return weatherInfo;
	}

}
