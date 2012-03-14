package com.anderspersson.xbmcwidget.recenttv;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.RemoteViews;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

public class RecentTvWidgetRenderIntentServiceHC extends IntentService {
	private Handler handler;

	public RecentTvWidgetRenderIntentServiceHC() {
		super("RecentTvWidgetRenderIntentServiceHC");
	}
	
	@Override 
	public void onCreate() {
		super.onCreate();
		handler = new Handler();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		
		if(action.equals(RecentTvWidget.RECENT_TV_UPDATE_WIDGET)) {
			createWidget(intent.getIntExtra("widgetId", 0));
		}
		else if(action.equals(RecentTvRefreshedIntent.REFRESHED)) {
			refreshWidgets();
		}
		else if(action.equals(XbmcService.PLAY_EPISODE_ACTION)) {
            handlePlayClick(intent.getStringExtra(XbmcService.EXTRA_ITEM));
        }
	}

	private void refreshWidgets() {
		ComponentName thisAppWidget = new ComponentName(getPackageName(), RecentTvWidget.class.getName() );
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
	}
	
	private void createWidget(int appWidgetId) {
		updateRemoteView(AppWidgetManager.getInstance(this), appWidgetId);
	}

	private void handlePlayClick(String filePath) {
		handler.post(new SentToXBMCToast(this));
		
		Intent playIntent = new Intent(this, XbmcService.class);
		playIntent.setAction(XbmcService.PLAY_EPISODE_ACTION);
		playIntent.putExtra(XbmcService.EXTRA_ITEM, filePath);
		
		startService(playIntent);	
	}

	private void updateRemoteView(AppWidgetManager appWidgetManager, int appWidgetId) {
		Intent intent = new Intent(this, RecentTvRemoteViewsService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		
		RemoteViews rv = new RemoteViews(this.getPackageName(), R.layout.recent_tv_widget);
		rv.setRemoteAdapter(appWidgetId, R.id.stack_view, intent);
		rv.setEmptyView(R.id.stack_view, R.id.empty_view);

		Intent playIntent = new Intent(this, RecentTvWidgetRenderIntentServiceHC.class);
		playIntent.setAction(XbmcService.PLAY_EPISODE_ACTION);
		playIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent toastPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, rv);
	}
}
