package com.zhlt.g1app.data;

import java.util.List;

public class DataProvince {
	public String province;
	List<DataCity> city_list;
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public List<DataCity> getCity_list() {
		return city_list;
	}

	public void setCity_list(List<DataCity> city_list) {
		this.city_list = city_list;
	}

	@Override
	public String toString() {
		return "ProvinceModel [province=" + province + ", city_list="
				+ city_list + "]";
	}
	
	
	
	

	
}
