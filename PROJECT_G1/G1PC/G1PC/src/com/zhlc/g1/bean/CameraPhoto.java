package com.zhlc.g1.bean;



import java.io.Serializable;



public class CameraPhoto implements Serializable{
	private String id;

	private String path;
    private  String imei;

	private String savePcPath;
    private byte[] pictureinfo;
    private int fileSize;
    
    private static final long serialVersionUID = 1L;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getPictureinfo() {
		return pictureinfo;
	}

	public void setPictureinfo(byte[] pictureinfo) {
		this.pictureinfo = pictureinfo;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getBearing() {
		return bearing;
	}

	public void setBearing(String bearing) {
		this.bearing = bearing;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String latitude;
    private String longitude;
    private String time;
    private String altitude;
    private String bearing;
    private String speed;
   
    private String key;
    private String accuracy;

    public void setFileSize(int fileSize){
        this.fileSize = fileSize;
    }

    public int getFileSize() {
        return this.fileSize;
    }
    public byte[] getpPctureinfo() {
        return pictureinfo;
    }

    public String getImei() {
        return imei;
    }
    public void setImei(String imei) {
        this.id = imei;
    }
    public String getSavePcPath() {
		return savePcPath;
	}

	public void setSavePcPath(String savePcPath) {
		this.savePcPath = savePcPath;
	}
	public String getPath() {
		return path;
	}
    public void setpictureinfo(byte[] pictureinfo){
        this.pictureinfo = pictureinfo.clone();
    }

	public void setPath(String path) {
		this.path = path;
	}
    @Override
    public String toString() {
        return "CameraPhoto [id=" + id + ", latitude=" + getAltitude()
                + ", longitude=" + getLongitude() + ", time=" + getTime() + ", path="
                + path + "]";
    }

}
