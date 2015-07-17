package com.zhlt.g1.camera.bean;

import java.io.Serializable;

/**
 * Created by user on 7/10/15.
 */

public class CameraVideo implements Serializable {
    private  int isupload = 0;
    private String path;
    private String starttime;
    private String endtime;
    private String id;
    public String getSavePath() {
        return path;
    }

    public void setSavePath(String path) {
        this.path = path;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getstarttime() {
        return starttime;
    }

    public void setstarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getendtime() {
        return endtime;
    }

    public void setendtime(String endtime) {
        this.endtime = endtime;
    }


    public int isIsrecord() {
        return isupload;
    }

    public void setIsrecord(int isrecord) {
        this.isupload = isrecord;
    }

    @Override
    public String toString() {
        return "CameraVideo [id=" + id + ", path=" + getSavePath()
                + ", getstarttime=" + getstarttime() + ", getendtime=" + getendtime() + "isupload = "+isIsrecord()+"]";
    }

}