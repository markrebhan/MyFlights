package com.example.myflights;

public class TemperatureConvert {

	public static int cToF(int degreeC){
		
		float C = degreeC;
		// convert to F
		float F =  1.8f * C + 32;
		int degreeF = Math.round(F);
		return degreeF;
	}
	
}
