package com.example.myflights;

import android.util.Log;

public class DataValidation {
	
	FlightInfo info;
	public DataValidation(FlightInfo info){this.info = info;}
	
	public String validate(){
		
		if(!validAirport1()) return "Not a valid departure airport.";
		else if(!validAirport2()) return "Not a valid arrival airport.";
		else if(!validAirline()) return "Not a valid airline";
		//else if(!validFlight(info.getFlight())) return "Not a valid flight.";
		else if(!validDate(info.getDepartTime())) return "Date cannot be in the past.";
		else if(!MyFlightsApp.flightData.queryForDup(info.getAirlineCode(),info.getFlight(),info.getDepartTime())) return "Cannot insert duplicate flight!";
		else return null;
	}
	
	
	private boolean validAirport1(){
		if(info.getOrigin().length() < 4) return false;
		info.setOrigin(info.getOrigin().substring(0, 4));
		Log.d("VALIDATION", info.getOrigin());
		int id = MyFlightsApp.flightData.queryEntityID(info.getOrigin(), "airports", "airport");
		info.setOriginCode(id);
		if (id == -1) return false;
		else return true;
		
	}
	
	private boolean validAirport2(){
		if(info.getDestination().length() < 4) return false;
		info.setDestination(info.getDestination().substring(0, 4));
		Log.d("VALIDATION", info.getDestination());
		int id = MyFlightsApp.flightData.queryEntityID(info.getDestination(), "airports", "airport");
		info.setDestinationCode(id);
		if (id == -1) return false;
		else return true;
		
	}
	
	private boolean validAirline() {
		if(info.getAirline().length() == 0) return true;
		if(info.getAirline().length() < 3) return false;
		info.setAirline(info.getAirline().substring(0, 3));
		int id = MyFlightsApp.flightData.queryEntityID(info.getAirline(), "airlines", "airline");
		info.setAirlineCode(id);
		if (id == -1) return false;
		else return true;
	}
	
	
	//TODO validate again if an actual time is found in API
	private boolean validDate(String text) {
		boolean valid = false;
		long currentDateEpoch = CurrentDate.currentDate(-86400000);
		currentDateEpoch /= 1000;
		long selectedDate = Long.valueOf(text).longValue();
		if (selectedDate > currentDateEpoch) valid = true;
		return valid;
	}
	
	
	
	
}
