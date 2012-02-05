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
import com.anderspersson.xbmcwidget.remote.XbmcService;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

public class RecentTvWidget extends AppWidgetProvider {
    public static final String PLAY_EPISODE_ACTION = "com.anderspersson.xbmcwidget.PLAY_EPISODE_ACTION";
    public static final String EXTRA_ITEM = "com.anderspersson.xbmcwidget.EXTRA_ITEM";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(PLAY_EPISODE_ACTION)) {
            String filePath = intent.getStringExtra(EXTRA_ITEM);
        	Toast.makeText(context, "Sent to XBMC", Toast.LENGTH_SHORT).show();
        	Intent playIntent = new Intent(context, XbmcService.class);
    		playIntent.setAction(PLAY_EPISODE_ACTION);
    		playIntent.putExtra(EXTRA_ITEM, filePath);
        	context.startService(playIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            
        	Intent intent = new Intent(context, RecentTvService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recent_tv_widget);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

            Intent playIntent = new Intent(context, RecentTvWidget.class);
            playIntent.setAction(RecentTvWidget.PLAY_EPISODE_ACTION);
            playIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}