package com.anderspersson.xbmcwidget.recenttv;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.common.Timer;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

public class RecentTvWidgetHelperHC implements IRecentTvWidgetHelper {
	private Timer refreshTimer = new Timer(); 
    
	public void onWidgetUpdate(Context context) {
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
	}

	public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		Intent intent = new Intent(context, RecentTvRemoteViewsService.class);
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

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(XbmcService.PLAY_EPISODE_ACTION)) {
            createPlayIntent(context, intent);
        }
        else if(refreshTimer.isTickIntent(intent)) {
            onWidgetUpdate(context);
        }
	}

	private void createPlayIntent(Context context, Intent intent) {
		String filePath = intent.getStringExtra(XbmcService.EXTRA_ITEM);
		Toast.makeText(context, "Sent to XBMC", Toast.LENGTH_SHORT).show();
		Intent playIntent = new Intent(context, XbmcService.class);
		playIntent.setAction(XbmcService.PLAY_EPISODE_ACTION);
		playIntent.putExtra(XbmcService.EXTRA_ITEM, filePath);
		context.startService(playIntent);
	}
	
	public void onEnabled(Context context) {
		refreshTimer.enable(context); 
	}

	public void onDisabled(Context context) {
		refreshTimer.disable(context);
	}
}
