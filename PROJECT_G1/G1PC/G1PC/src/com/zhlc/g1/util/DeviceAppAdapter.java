package com.zhlc.g1.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

public class  DeviceAppAdapter {
    /* 车载端 日志 */
    static Logger logger_car = Logger.getLogger("server_car_7776");
    /* APP端日志 */
    static Logger logger_app = Logger.getLogger("server_app_7777");

	private static ConcurrentMap<String, ChannelHandlerContext> activeCarClientMap = new ConcurrentHashMap<String, ChannelHandlerContext>();
	  
    /**
     * 待激活 Car 
     */
	private static ConcurrentMap<String,ChannelHandlerContext>    pendingDevices=new  ConcurrentHashMap<String, ChannelHandlerContext>();
	/**
	 * 存放未激活设备
	 * @return
	 */
	public static synchronized ConcurrentMap<String, ChannelHandlerContext> getpendingDevices() {
		return pendingDevices;
	}
	/**
	 * 添加未激活设备
	 * @param key
	 */
	public static synchronized void addpendingDevice(String id,
    		ChannelHandlerContext ct){
		pendingDevices.put(id, ct);
	}



	/**
	     * 设备管理池， 存放在线设备 APP
	     */
	 private static ConcurrentMap<String, ChannelHandlerContext> activeAPPClientMap = new ConcurrentHashMap<String, ChannelHandlerContext>();
		public  static synchronized ConcurrentMap<String, ChannelHandlerContext> getActiveCarClientMap() {
			return activeCarClientMap;
		}
	
		public static synchronized ConcurrentMap<String, ChannelHandlerContext> getActiveAPPClientMap() {
			return activeAPPClientMap;
		}

	    /**
	     * 添加在线设备 APP
	     *
	     * @param id
	     * @param ct
	     */
	    public static synchronized  void addAPPClient(String id,
	    		ChannelHandlerContext ct) {
	        activeAPPClientMap.putIfAbsent(id, ct);
	        if (InitUtil.DEBUG)
	            logger_car.info("on Line app:" + activeAPPClientMap.size());
	    }

	    /**
	     * 移出在线client APP
	     *
	     * @param id
	     */
	    public static  synchronized void delAPPClient(String id) {
	        activeAPPClientMap.remove(id);
	    }
	    
	    /**
	     * 添加在线设备 Car
	     *
	     * @param id
	     * @param ct
	     */
	    public  static synchronized void addCarClient(String id, ChannelHandlerContext ct) {
	        activeCarClientMap.putIfAbsent(id, ct);
	        if (InitUtil.DEBUG)
	            logger_car.info("on  Line car:" + activeCarClientMap.size());
	    }

	    /**
	     * 移出在线client Car
	     *
	     * @param id
	     */
	    public  static synchronized void delCarClient(String id) {
	        activeCarClientMap.remove(id);
	    }

}
