package com.example.myflights;

public class FlightInfoExtended extends FlightInfo{
	
	private int flightXMLEnabled;
	private int originCode;
	private int destinationCode;
	private int airlineCode;
	
	public FlightInfoExtended(String ori, String des, String dep, String arr, String air, String fli, int XML){
		super(ori, des, dep, arr, air, fli);
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
