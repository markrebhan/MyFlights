package com.example.myflights;

public class WeatherInfo {
	
	String cloudiness, conditions;
	int temperature, windSpeed, windDirection;
	long updateTime;
	
	public WeatherInfo(int temp, String cloud, String cond, int windS, int windD, long time){
		this.temperature = temp;
		this.cloudiness = cloud;
		this.conditions = cond;
		this.windSpeed = windS;
		this.windDirection = windD;
		// convert from unix to java time
		this.updateTime = time * 1000;
		
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public String getCloudiness() {
		return cloudiness;
	}

	public void setCloudiness(String cloudiness) {
		this.cloudiness = cloudiness;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public int getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}

	public int getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	

}
