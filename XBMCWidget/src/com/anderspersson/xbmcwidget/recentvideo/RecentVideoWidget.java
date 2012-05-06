package com.anderspersson.xbmcwidget.recentvideo;

import com.anderspersson.xbmcwidget.common.ITimerCallback;
import com.anderspersson.xbmcwidget.common.UpdateTimer;
import com.anderspersson.xbmcwidget.configuration.PreferenceChangedListener;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public abstract class RecentVideoWidget extends AppWidgetProvider {

	public static final String RECENT_VIDEO_UPDATE_WIDGET = "com.anderspersson.xbmcwidget.recenttv.RECENT_TV_UPDATE_WIDGET";
	public UpdateTimer _timer = new UpdateTimer();
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		_timer.removeCallback(context, getTimerCallback(context));
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		_timer.addCallback(context, getTimerCallback(context));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if(intent.getAction() == null || intent.getAction().equals(PreferenceChangedListener.PREFERENCE_CHANGED) == false) {
			return;
		}
		
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), this.getClass().getName() );
		int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);		
		
		if(ids.length > 0) {
			_timer.reset(context);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) { 
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		for (int i = 0; i < appWidgetIds.length; ++i) {
			Intent updateIntent = createUpdateIntent(context);
			updateIntent.setAction(RECENT_VIDEO_UPDATE_WIDGET);
			updateIntent.putExtra("widgetId", appWidgetIds[i]);
			context.startService(updateIntent);
		}   	
	}
	
	protected abstract Class<?> getPreHCRenderIntentService();	
	protected abstract Class<?> getHCRenderIntentService() ;
	protected abstract ITimerCallback getTimerCallback(Context ctx);

	private Intent createUpdateIntent(Context context) {
		return new Intent(context, getRecentWidgetRenderIntentService());
	}

	private Class<?> getRecentWidgetRenderIntentService() {
		int honeycombVersion = 11;
		return Build.VERSION.SDK_INT < honeycombVersion 
				? getPreHCRenderIntentService() 
				: getHCRenderIntentService(); 
	}
}