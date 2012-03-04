package com.anderspersson.xbmcwidget.common;

import java.util.HashMap;

import android.app.Application;

public class XbmcApplication extends Application {

	private HashMap<String, Object> stateMap = new HashMap<String, Object>();
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public Object setState(String key, Object value) {
		return stateMap.put(key, value);
	}
	
	public <T> T getState(String key) {
		return (T)stateMap.get(key);
	}
}
