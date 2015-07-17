package com.zhlt.g1.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.TextView;

public class WifiManagerUtil {
	public static final int WIFI_AP_STATE_DISABLING = 0;
	public static final int WIFI_AP_STATE_DISABLED = 11;
	public static final int WIFI_AP_STATE_ENABLING = 12;
	public static final int WIFI_AP_STATE_ENABLED = 13;
	public static final int WIFI_AP_STATE_FAILED = 14;
	WifiManager wifiManager;
	WifiConfiguration netConfig = null;

	Logger log = Log4jUtil.getLogger("com.zhlt.g1.util.WifiManagerUtil");

	public WifiManagerUtil(Context ay) {
		wifiManager = (WifiManager) ay.getSystemService(Context.WIFI_SERVICE);

	}

	public void open() {
		int wifi = getWifiApState(wifiManager);
		if (wifi == WIFI_AP_STATE_ENABLED) {
			if (InitUtil.DEBUG)
				log.info("wifl已经打开");
		} else {
			MyApplication.getInstance().getFixedThreadPool()
					.execute(new Runnable() {

						@Override
						public void run() {

							Method method1 = null;
							try {
								method1 = wifiManager.getClass().getMethod(
										"setWifiApEnabled",
										WifiConfiguration.class, boolean.class);
								netConfig = new WifiConfiguration();

								// wifi
								netConfig.SSID = InitUtil.WIFIPNAME;
								netConfig.allowedAuthAlgorithms
										.set(WifiConfiguration.AuthAlgorithm.OPEN);
								netConfig.allowedProtocols
										.set(WifiConfiguration.Protocol.RSN);
								netConfig.allowedProtocols
										.set(WifiConfiguration.Protocol.WPA);
								netConfig.allowedKeyManagement
										.set(WifiConfiguration.KeyMgmt.WPA_PSK);
								netConfig.allowedPairwiseCiphers
										.set(WifiConfiguration.PairwiseCipher.CCMP);
								netConfig.allowedPairwiseCiphers
										.set(WifiConfiguration.PairwiseCipher.TKIP);
								netConfig.allowedGroupCiphers
										.set(WifiConfiguration.GroupCipher.CCMP);
								netConfig.allowedGroupCiphers
										.set(WifiConfiguration.GroupCipher.TKIP);

								netConfig.preSharedKey = InitUtil.WIFIPWD;

								method1.invoke(wifiManager, netConfig, true);

								Thread.sleep(5000);

								String ip = "192.168.43.2";
								int networkPrefixLength = 24;
								InetAddress intetAddress = InetAddress
										.getByName(ip);
								setIpAddress(intetAddress, networkPrefixLength,
										netConfig);
								wifiManager.updateNetwork(netConfig);

							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchFieldException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InstantiationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

		}
	}

	public boolean close() {
		boolean ret = false;

		try {
			Method method = wifiManager.getClass().getMethod(
					"getWifiApConfiguration");
			method.setAccessible(true);
			WifiConfiguration config = (WifiConfiguration) method
					.invoke(wifiManager);
			Method method2 = wifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, boolean.class);
			ret = (Boolean) method2.invoke(wifiManager, config, false);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public int getWifiApState(WifiManager wifiManager) {
		try {
			Method method = wifiManager.getClass().getMethod("getWifiApState");
			int i = (Integer) method.invoke(wifiManager);
			// Log.i("wifi state:  " + i);
			return i;
		} catch (Exception e) {
			// Log.i("Cannot get WiFi AP state" + e);
			e.printStackTrace();
			return WIFI_AP_STATE_FAILED;
		}
	}

	public static void setIpAddress(InetAddress addr, int prefixLength,
			WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, NoSuchMethodException,
			ClassNotFoundException, InstantiationException,
			InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class laClass = Class.forName("android.net.LinkAddress");
		Constructor laConstructor = laClass.getConstructor(new Class[] {
				InetAddress.class, int.class });
		Object linkAddress = laConstructor.newInstance(addr, prefixLength);
		ArrayList mLinkAddresses = (ArrayList) getDeclaredField(linkProperties,
				"mLinkAddresses");
		mLinkAddresses.clear();
		mLinkAddresses.add(linkAddress);


	}

	public static void setGateway(InetAddress gateway,
			WifiConfiguration wifiConf) throws SecurityException,
			IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			InvocationTargetException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		Class routeInfoClass = Class.forName("android.net.RouteInfo");
		Constructor routeInfoConstructor = routeInfoClass
				.getConstructor(new Class[] { InetAddress.class });
		Object routeInfo = routeInfoConstructor.newInstance(gateway);
		ArrayList mRoutes = (ArrayList) getDeclaredField(linkProperties,
				"mRoutes");
		mRoutes.clear();
		mRoutes.add(routeInfo);
	}

	public static void setDNS(InetAddress dns, WifiConfiguration wifiConf)
			throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException {
		Object linkProperties = getField(wifiConf, "linkProperties");
		if (linkProperties == null)
			return;
		ArrayList<InetAddress> mDnses = (ArrayList<InetAddress>) getDeclaredField(
				linkProperties, "mDnses");
		mDnses.clear(); // or add a new dns address , here I just want to
						// replace DNS1
		mDnses.add(dns);
	}

	public static Object getField(Object obj, String name)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getField(name);
		Object out = f.get(obj);
		return out;
	}

	public static Object getDeclaredField(Object obj, String name)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		Object out = f.get(obj);
		return out;
	}
}
