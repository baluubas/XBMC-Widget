package com.anderspersson.xbmcwidget.recenttv;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

public class RecentTvWidgetHelperHC implements IRecentTvWidgetHelper {

	public void onWidgetUpdate(Context context) {
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
		
	}

	public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
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
}
