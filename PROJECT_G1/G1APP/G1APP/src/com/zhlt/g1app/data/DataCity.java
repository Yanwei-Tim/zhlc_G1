package com.zhlt.g1app.data;

import java.util.List;

public class DataCity {
	public String city;
	List<DataCounty> county_list;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List<DataCounty> getCounty_list() {
		return county_list;
	}
	public void setCounty_list(List<DataCounty> county_list) {
		this.county_list = county_list;
	}
	
	@Override
	public String toString() {
		return "CityModel [city=" + city + ", county_list=" + county_list + "]";
	}
	
	
	
}
