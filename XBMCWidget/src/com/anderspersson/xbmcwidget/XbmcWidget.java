package com.anderspersson.xbmcwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class XbmcWidget extends AppWidgetProvider 
{	
    public static final String PLAYPAUSE_ACTION = "com.anderspersson.xbmcwidget.PLAY";
    public static final String PAUSE_ACTION = "com.anderspersson.xbmcwidget.PAUSE";
    public static final String UP_ACTION = "com.anderspersson.xbmcwidget.UP";
    public static final String DOWN_ACTION = "com.anderspersson.xbmcwidget.DOWN";
    public static final String LEFT_ACTION = "com.anderspersson.xbmcwidget.LEFT";
    public static final String RIGHT_ACTION = "com.anderspersson.xbmcwidget.RIGHT";
    public static final String SELECT_ACTION = "com.anderspersson.xbmcwidget.SELECT";
    public static final String BACK_ACTION = "com.anderspersson.xbmcwidget.BACK";
    public static final String STOP_ACTION = "com.anderspersson.xbmcwidget.STOP";
    public static final String TOGGLE_FULLSCREEN_ACTION = "com.anderspersson.xbmcwidget.TOGGLEFULLSCREEN";
    public static final String TOGGLE_OSD_ACTION = "com.anderspersson.xbmcwidget.TOGGLEOSD";
    public static final String HOME_ACTION = "com.anderspersson.xbmcwidget.HOME";
    public static final String CONTEXT_ACTION = "com.anderspersson.xbmcwidget.CONTEXT";
    
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews( context.getPackageName(), R.layout.widget );
        watchWidget = new ComponentName( context, XbmcWidget.class );
        
        Intent intent = new Intent(context, XbmcService.class);
        context.startService(intent);
        
        setupButton(context, remoteViews, PLAYPAUSE_ACTION, R.id.playpause);
        setupButton(context, remoteViews, UP_ACTION, R.id.up);
        setupButton(context, remoteViews, DOWN_ACTION, R.id.down);
        setupButton(context, remoteViews, LEFT_ACTION, R.id.left);
        setupButton(context, remoteViews, RIGHT_ACTION, R.id.right);
        setupButton(context, remoteViews, SELECT_ACTION, R.id.select);
        setupButton(context, remoteViews, BACK_ACTION, R.id.back);
        setupButton(context, remoteViews, TOGGLE_FULLSCREEN_ACTION, R.id.togglefullscreen);
        setupButton(context, remoteViews, TOGGLE_OSD_ACTION, R.id.toggleosd);
        setupButton(context, remoteViews, HOME_ACTION, R.id.home);
        setupButton(context, remoteViews, CONTEXT_ACTION, R.id.context);
        
        appWidgetManager.updateAppWidget( watchWidget, remoteViews );	
	}

	private void setupButton(Context context, RemoteViews remoteViews, String action, int id) {
		
		Intent playIntent = new Intent(context, XbmcService.class);
		playIntent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, playIntent, 0);
        remoteViews.setOnClickPendingIntent(id, pendingIntent);
	}
}
