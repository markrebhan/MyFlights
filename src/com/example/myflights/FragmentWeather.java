package com.example.myflights;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentWeather extends Fragment{
	
	TextView temperature, updateTime;
	
	
	public static final String TAG = "FragmentWeather";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_weather, container, false);
		
		temperature = (TextView) view.findViewById(R.id.weather_temp);
		updateTime = (TextView) view.findViewById(R.id.weather_updated);
		
		return view;
	}

	public void refreshWeatherData(String airport){
		
		RefreshWeatherAsync refreshWeatherAsync = new RefreshWeatherAsync();
		
		refreshWeatherAsync.execute(airport);
		
	}
	
	class RefreshWeatherAsync extends AsyncTask<String,Void,WeatherInfo>{

		
		@Override
		protected WeatherInfo doInBackground(String... args) {
			
			RESTfulCalls webCall = new RESTfulCalls();
			JSONObject response = webCall.MetarEx(args[0]);
			return JSONParsing.parseWeatherData(response);
			
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			super.onPostExecute(result);
			
			// find this fragment and set the text to correct values
			FragmentWeather fragment = (FragmentWeather) getFragmentManager().findFragmentById(R.id.fragment_weather);
			//C or F with class to calculate
			// get temp in C and calculate if needed
			int temp = result.getTemperature();
			// get preference value for temperature type
			boolean tempType = PreferenceManager.getDefaultSharedPreferences(
					getActivity().getApplicationContext()).getBoolean("temperature", true);
			String typeSymbol = "°C";
			
			// tempType of true signifies F
			if (tempType){
				temp = TemperatureConvert.cToF(temp);
				typeSymbol = "°F";
			}
			fragment.temperature.setText(temp + typeSymbol);
			
			
			// display last update time
			fragment.updateTime.setText(new SimpleDateFormat("hh:mm aaa",Locale.US).format(new Date(result.getUpdateTime())));
			
		}
		
		
		
	}
}
