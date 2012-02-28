/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anderspersson.xbmcwidget.recenttv;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

public class RecentTvWidget extends AppWidgetProvider {

    private static final String CLOCK_WIDGET_UPDATE = "com.anderspersson.xbmcwidget.recenttv.CLOCK_UPDATE";
	private int updateIntervalMilliseconds = 30 * 60 * 1000;

	@Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context));
    }

    @Override
    public void onEnabled(Context context) {
       super.onEnabled(context);
       AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
       alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
    		   System.currentTimeMillis() + updateIntervalMilliseconds, 
    		   updateIntervalMilliseconds, 
    		   createClockTickIntent(context)); 
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(XbmcService.PLAY_EPISODE_ACTION)) {
            createPlayIntent(context, intent);
        }
        else if(CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
        }
        super.onReceive(context, intent);
    }

	private void createPlayIntent(Context context, Intent intent) {
		String filePath = intent.getStringExtra(XbmcService.EXTRA_ITEM);
		Toast.makeText(context, "Sent to XBMC", Toast.LENGTH_SHORT).show();
		Intent playIntent = new Intent(context, XbmcService.class);
		playIntent.setAction(XbmcService.PLAY_EPISODE_ACTION);
		playIntent.putExtra(XbmcService.EXTRA_ITEM, filePath);
		context.startService(playIntent);
	}
	 
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
        	updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

	private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		Intent intent = new Intent(context, RecentTvService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recent_tv_widget);
		rv.setRemoteAdapter(appWidgetId, R.id.stack_view, intent);
		rv.setEmptyView(R.id.stack_view, R.id.empty_view);

		Intent playIntent = new Intent(context, RecentTvWidget.class);
		playIntent.setAction(XbmcService.PLAY_EPISODE_ACTION);
		playIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, rv);
	}
    
    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}