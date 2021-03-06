package com.anderspersson.xbmcwidget.recentvideo;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.RemoteViews;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.common.FileLog;
import com.anderspersson.xbmcwidget.common.ToastRunnable;
import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

public abstract class RenderWidgetIntentServiceHC extends IntentService {
	
	private static final String RETRY = "com.anderspersson.xbmcwidget.recentvideo.RETRY";
 
	private Handler handler;

	public RenderWidgetIntentServiceHC(String name) {
		super(name);
	}
	
	@Override 
	public void onCreate() {
		super.onCreate();
		handler = new Handler();
	}
	
	protected abstract Class<?> getWidgetClass();
	protected abstract Class<?> getRemoveViewsService();
	protected abstract boolean hasWidgetData();
	protected abstract int getLoadingViewId();
	protected abstract int getFailedViewId();
	protected abstract Class<?> getRefreshRecentIntentService();
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		
		FileLog.appendLog("Render - " + action);
		
		if(action.equals(RecentVideoWidget.RECENT_VIDEO_UPDATE_WIDGET)) {
			createWidget(intent.getIntExtra("widgetId", 0));
		}
		else if(action.equals(RecentVideoIntentActions.REFRESHED)) {
			refreshWidgets();
		}
		else if(action.equals(RecentVideoIntentActions.REFRESH_FAILED)) {
			refreshFailed();
		}
		else if(action.equals(XbmcService.PLAY_EPISODE_ACTION)) {
            handlePlayClick(intent.getStringExtra(XbmcService.EXTRA_ITEM));
        }
		else if(action.equals(Intent.ACTION_VIEW)) {
			Intent browseIntent = new Intent(Intent.ACTION_VIEW)
				.setData(Uri.parse(intent.getDataString()))
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(browseIntent);
		}
		else if(action.equals(RETRY)) {
            retry();
        }
	}
	
	protected XbmcWidgetApplication getWidgetApplication() {
		return (XbmcWidgetApplication)getApplicationContext();
	}
	
	private void retry() {
		createLoadingView();
		startRefreshService();
	}

	private void startRefreshService() {
		Intent refreshIntentService = new Intent(this, getRefreshRecentIntentService());
		startService(refreshIntentService);
	}

	private void refreshFailed() {
		
		if(hasWidgetData()) 
			return;
		
		createFailedView();
	}
	
	@SuppressLint("NewApi")
	private void refreshWidgets() {
		
		if(hasWidgetData() == false) 
		{
			createFailedView();
			return;
		}
		
		int ids[] = getWidgetIds();
		
		for(int i = 0; i < ids.length; i++)
			updateRemoteView(getWidgetManager(), ids[i]);
	
		getWidgetManager().notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
	}
	
	private int[] getWidgetIds() {
        return getWidgetManager().getAppWidgetIds(getComponentName());
	}
	
	private AppWidgetManager getWidgetManager() {
        return AppWidgetManager.getInstance(this);
	}
	
	private ComponentName getComponentName() {
		return new ComponentName(getPackageName(), getWidgetClass().getName() );
	}

	private void createWidget(int appWidgetId) {
		if(hasWidgetData()) {
			updateRemoteView(getWidgetManager(), appWidgetId);
			return;
		}	
	}

	private void handlePlayClick(String filePath) {
		handler.post(new ToastRunnable(this, "Playing on XBMC"));
		
		Intent playIntent = new Intent(this, XbmcService.class);
		playIntent.setAction(XbmcService.PLAY_EPISODE_ACTION);
		playIntent.putExtra(XbmcService.EXTRA_ITEM, filePath);
		
		startService(playIntent);	
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void updateRemoteView(AppWidgetManager appWidgetManager, int appWidgetId) {
		Intent intent = new Intent(this, getRemoveViewsService());
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		
		RemoteViews rv = new RemoteViews(this.getPackageName(), R.layout.recent_video_widget);
		rv.setRemoteAdapter(appWidgetId, R.id.stack_view, intent);

		Intent playIntent = new Intent(this, this.getClass());
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
			RemoteViews rv = new RemoteViews( this.getPackageName(), getFailedViewId());
			ComponentName recentTvWidget = new ComponentName( this, getWidgetClass() );
			
			Intent retryIntent = new Intent(this, this.getClass());
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
			RemoteViews rv = new RemoteViews( this.getPackageName(), getLoadingViewId());
			ComponentName recentTvWidget = new ComponentName( this, getWidgetClass());	
		    appWidgetManager.updateAppWidget( recentTvWidget, rv );
		}
	}
}
