package com.example.myflights;

import java.util.Locale;

public class FlightInfo {
	
	private String origin;
	private String destination;
	private String departTime;
	private String arrivalTime;
	private String airline;
	private String flight;
	private int flightXMLEnabled;
	private int originCode;
	private int destinationCode;
	private int airlineCode;
	

	public FlightInfo(String ori, String des, String dep, String arr, String air, String fli, int XML){
		this.origin = ori.toUpperCase(Locale.US);
		this.destination = des.toUpperCase(Locale.US);
		this.departTime = dep;
		this.arrivalTime = arr;
		this.airline = air.toUpperCase(Locale.US);
		this.flight = fli;
		this.flightXMLEnabled = XML;
		this.originCode = -1;
		this.destinationCode = -1;
		this.airlineCode = -1;
	}

	public int getFlightXMLEnabled() {
		return flightXMLEnabled;
	}

	public void setFlightXMLEnabled(int flightXMLEnabled) {
		this.flightXMLEnabled = flightXMLEnabled;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDepartTime() {
		return departTime;
	}

	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}
	
	public int getOriginCode() {
		return originCode;
	}
	
	public void setOriginCode(int originCode) {
		this.originCode = originCode;
	}
	
	public int getDestinationCode() {
		return destinationCode;
	}
	
	public void setDestinationCode(int destinationCode) {
		this.destinationCode = destinationCode;
	}
	
	public int getAirlineCode() {
		return airlineCode;
	}
	
	public void setAirlineCode(int airlineCode) {
		this.airlineCode = airlineCode;
	}
}
