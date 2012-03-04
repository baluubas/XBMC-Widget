package com.anderspersson.xbmcwidget.recenttv;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

public interface IRecentTvWidgetHelper {

	void onWidgetUpdate(Context context);

	void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
			int widgetId);

	void onReceive(Context context, Intent intent);

	void onEnabled(Context context);

	void onDisabled(Context context);

}
