package com.anderspersson.xbmcwidget.recenttv;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RecentTvRefreshedIntent extends Intent {
	
	public static final String REFRESHED =  "com.anderspersson.xbmcwidget.recenttv.RECENT_TV_REFRESHED";
	
	public RecentTvRefreshedIntent(Context context) {
		super(context, getWidgetClass());
		setAction(REFRESHED);
	}

	private static Class<?> getWidgetClass() {
		int honeycombVersion = 11;
		return Build.VERSION.SDK_INT < honeycombVersion 
				? com.anderspersson.xbmcwidget.recenttv.RecentTvWidgetRenderIntentService.class
				: com.anderspersson.xbmcwidget.recenttv.RecentTvWidgetRenderIntentServiceHC.class; 
	}
}
