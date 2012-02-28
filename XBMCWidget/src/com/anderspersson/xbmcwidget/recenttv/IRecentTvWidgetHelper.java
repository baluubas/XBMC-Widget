package com.anderspersson.xbmcwidget.recenttv;

import android.appwidget.AppWidgetManager;
import android.content.Context;

public interface IRecentTvWidgetHelper {

	void onWidgetUpdate(Context context);

	void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
			int widgetId);

}
