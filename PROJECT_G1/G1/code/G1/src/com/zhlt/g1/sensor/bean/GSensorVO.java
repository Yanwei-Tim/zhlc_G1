package com.zhlt.g1.sensor.bean;

import java.io.Serializable;

public class GSensorVO implements Serializable {

	private String id;
	private String sensorX;
	private String sensorY;
	private String sensorZ;
	private String time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSensorX() {
		return sensorX;
	}
	public void setSensorX(String sensorX) {
		this.sensorX = sensorX;
	}
	@Override
	public String toString() {
		return "GSensorVO [id=" + id + ", sensorX=" + sensorX + ", sensorY="
				+ sensorY + ", sensorZ=" + sensorZ + ", time=" + time + "]";
	}
	public String getSensorY() {
		return sensorY;
	}
	public void setSensorY(String sensorY) {
		this.sensorY = sensorY;
	}
	public String getSensorZ() {
		return sensorZ;
	}
	public void setSensorZ(String sensorZ) {
		this.sensorZ = sensorZ;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
