package com.anderspersson.xbmcwidget.recenttv;

import java.util.List;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.RemoteViews;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

public class RecentTvWidgetRenderIntentServiceHC extends IntentService {
	
	private static final String RETRY = "com.anderspersson.xbmcwidget.recenttv.RETRY";
 
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
		else if(action.equals(RecentTvRefreshedIntent.REFRESH_FAILED)) {
			refreshFailed();
		}
		else if(action.equals(XbmcService.PLAY_EPISODE_ACTION)) {
            handlePlayClick(intent.getStringExtra(XbmcService.EXTRA_ITEM));
        }
		else if(action.equals(RETRY)) {
            retry();
        }
	}

	private void retry() {
		createLoadingView();
		startRefreshService();
	}

	private void startRefreshService() {
		Intent fanArtDownloadIntent = new Intent(this, RefreshRecentTvIntentService.class);
		startService(fanArtDownloadIntent);
	}

	private void refreshFailed() {
		
		if(hasEpisodes()) 
			return;
		
		createFailedView();
	}
	
	private void refreshWidgets() {
		
		if(hasEpisodes() == false) 
		{
			createFailedView();
			return;
		}
		
		int ids[] = getWidgetIds();
		
		for(int i = 0; i < ids.length; i++)
			updateRemoteView(getWidgetManager(), ids[i]);
	
		getWidgetManager().notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
	}

	private boolean hasEpisodes() {
		return getEpisodes().size() > 0;
	}
	
	private int[] getWidgetIds() {
        return getWidgetManager().getAppWidgetIds(getComponentName());
	}
	
	private AppWidgetManager getWidgetManager() {
        return AppWidgetManager.getInstance(this);
	}
	
	private ComponentName getComponentName() {
		return new ComponentName(getPackageName(), RecentTvWidget.class.getName() );
	}

	private void createWidget(int appWidgetId) {
		if(hasEpisodes()) {
			updateRemoteView(getWidgetManager(), appWidgetId);
			return;
		}	
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

		Intent playIntent = new Intent(this, RecentTvWidgetRenderIntentServiceHC.class);
		playIntent.setAction(XbmcService.PLAY_EPISODE_ACTION);
		playIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent toastPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, rv);
	}
	
	private void createFailedView() {
		AppWidgetManager appWidgetManager = getWidgetManager();
		int[] widgetIds = getWidgetIds();
		
		for(int i = 0; i < widgetIds.length; i++) {
			RemoteViews rv = new RemoteViews( this.getPackageName(), R.layout.recent_tv_widget_failed);
			ComponentName recentTvWidget = new ComponentName( this, RecentTvWidget.class );
			
			Intent retryIntent = new Intent(this, RecentTvWidgetRenderIntentServiceHC.class);
			retryIntent.setAction(RETRY);
			PendingIntent toastPendingIntent = PendingIntent.getService(this, 0, retryIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	        rv.setOnClickPendingIntent(R.id.retry_button, toastPendingIntent);
			
		    appWidgetManager.updateAppWidget( recentTvWidget, rv );
		}
	}
	
	private void createLoadingView() {
		AppWidgetManager appWidgetManager = getWidgetManager();
		int[] widgetIds = getWidgetIds();
		
		for(int i = 0; i < widgetIds.length; i++) {
			RemoteViews rv = new RemoteViews( this.getPackageName(), R.layout.recent_tv_widget_loading);
			ComponentName recentTvWidget = new ComponentName( this, RecentTvWidget.class );	
		    appWidgetManager.updateAppWidget( recentTvWidget, rv );
		}
	}
	
	private List<TvShowEpisode> getEpisodes() {
		XbmcWidgetApplication app = getWidgetApplication();
		return app.getLastDownloadedEpisodes();
	}
	
	private XbmcWidgetApplication getWidgetApplication() {
		return (XbmcWidgetApplication)getApplicationContext();
	}
}
