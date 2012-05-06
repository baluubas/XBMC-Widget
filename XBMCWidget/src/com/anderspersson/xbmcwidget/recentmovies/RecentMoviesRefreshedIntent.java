package com.anderspersson.xbmcwidget.recentmovies;

import com.anderspersson.xbmcwidget.recentvideo.RecentVideoIntentActions;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RecentMoviesRefreshedIntent extends Intent {
	
	public RecentMoviesRefreshedIntent(Context context, Boolean isSuccess) {
		super(context, getWidgetClass());
		setAction(isSuccess ? RecentVideoIntentActions.REFRESHED : RecentVideoIntentActions.REFRESH_FAILED);
	}

	private static Class<?> getWidgetClass() {
		int honeycombVersion = 11;
		return Build.VERSION.SDK_INT < honeycombVersion 
				? RenderWidgetIntentService.class
				: RenderWidgetIntentServiceHC.class; 
	}
}
