package com.anderspersson.xbmcwidget.remote;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class RemoteWidget extends AppWidgetProvider 
{	    
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
        RemoteViews remoteViews;
        ComponentName remoteWidget;

        remoteViews = new RemoteViews( context.getPackageName(), R.layout.remote_widget );
        remoteWidget = new ComponentName( context, RemoteWidget.class );
        
        Intent intent = new Intent(context, XbmcService.class);
        context.startService(intent);
        
        setupButton(context, remoteViews, XbmcService.PLAYPAUSE_ACTION, R.id.playpause);
        setupButton(context, remoteViews, XbmcService.UP_ACTION, R.id.up);
        setupButton(context, remoteViews, XbmcService.DOWN_ACTION, R.id.down);
        setupButton(context, remoteViews, XbmcService.LEFT_ACTION, R.id.left);
        setupButton(context, remoteViews, XbmcService.RIGHT_ACTION, R.id.right);
        setupButton(context, remoteViews, XbmcService.SELECT_ACTION, R.id.select);
        setupButton(context, remoteViews, XbmcService.BACK_ACTION, R.id.back);
        setupButton(context, remoteViews, XbmcService.TOGGLE_FULLSCREEN_ACTION, R.id.togglefullscreen);
        setupButton(context, remoteViews, XbmcService.TOGGLE_OSD_ACTION, R.id.toggleosd);
        setupButton(context, remoteViews, XbmcService.HOME_ACTION, R.id.home);
        setupButton(context, remoteViews, XbmcService.CONTEXT_ACTION, R.id.context);
        
        appWidgetManager.updateAppWidget( remoteWidget, remoteViews );	
	}

	private void setupButton(Context context, RemoteViews remoteViews, String action, int id) {
		
		Intent playIntent = new Intent(context, XbmcService.class);
		playIntent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, playIntent, 0);
        remoteViews.setOnClickPendingIntent(id, pendingIntent);
	}
}
