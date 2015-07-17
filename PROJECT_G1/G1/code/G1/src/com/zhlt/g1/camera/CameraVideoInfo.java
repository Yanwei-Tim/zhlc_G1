package com.zhlt.g1.camera;

import java.io.Serializable;
import java.util.Date;

import io.netty.channel.Channel;

/**
 * Created by user on 7/10/15.
 */

public class CameraVideoInfo implements Serializable {
    private Channel channel;
    private  boolean isrecord = true;
    private String downloadurl;
    private Date starttime;
    private Date endtime;
    public String getSaveDir() {
        return downloadurl;
    }

    public void setSaveDir(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isIsrecord() {
        return isrecord;
    }

    public void setIsrecord(boolean isrecord) {
        this.isrecord = isrecord;
    }












}