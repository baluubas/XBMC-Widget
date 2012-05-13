package com.anderspersson.xbmcwidget.recenttv;

import com.anderspersson.xbmcwidget.recentvideo.RecentVideoIntentActions;
import com.anderspersson.xbmcwidget.recentvideo.VideoUpdaterService.UpdateResult;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RecentTvRefreshedIntent extends Intent {
	
	public RecentTvRefreshedIntent(Context context, UpdateResult updateResult) {
		super(context, getWidgetClass());
		String action = RecentVideoIntentActions.REFRESH_FAILED;
		
		if(updateResult == UpdateResult.NoChange) {
			action = RecentVideoIntentActions.NO_CHANGE;
		}
		else if(updateResult == UpdateResult.Updated) {
			action = RecentVideoIntentActions.REFRESHED;
		}
		setAction(action);
	}

	private static Class<?> getWidgetClass() {
		int honeycombVersion = 11;
		return Build.VERSION.SDK_INT < honeycombVersion 
				? RenderWidgetIntentService.class
				: RenderWidgetIntentServiceHC.class; 
	}
}
