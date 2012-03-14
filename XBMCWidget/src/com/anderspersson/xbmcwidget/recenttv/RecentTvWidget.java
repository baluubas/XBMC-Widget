package com.anderspersson.xbmcwidget.recenttv;

import com.anderspersson.xbmcwidget.configuration.PreferenceChangedListener;
import com.commonsware.cwac.wakeful.WakefulIntentService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RecentTvWidget extends AppWidgetProvider {

	public static final String RECENT_TV_UPDATE_WIDGET = "com.anderspersson.xbmcwidget.recenttv.UPDATE";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		WakefulIntentService.cancelAlarms(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		WakefulIntentService.scheduleAlarms(new RefreshRecentTvListener(), context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if(intent.getAction() == null || intent.getAction() != PreferenceChangedListener.PREFERENCE_CHANGED) {
			return;
		}
		WakefulIntentService.cancelAlarms(context);
		WakefulIntentService.scheduleAlarms(new RefreshRecentTvListener(), context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) { 
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int i = 0; i < appWidgetIds.length; ++i) {
			Intent updateIntent = createUpdateIntent(context);
			updateIntent.setAction(RECENT_TV_UPDATE_WIDGET);
			updateIntent.putExtra("widgetId", appWidgetIds[i]);
			context.startService(updateIntent);
		}   	
	}

	private Intent createUpdateIntent(Context context) {
		return new Intent(context, getRecentTvWidgetRenderIntentService());
	}

	private Class<?> getRecentTvWidgetRenderIntentService() {
		int honeycombVersion = 11;
		return Build.VERSION.SDK_INT < honeycombVersion 
				? RecentTvWidgetRenderIntentService.class
						: RecentTvWidgetRenderIntentServiceHC.class; 
	}
}