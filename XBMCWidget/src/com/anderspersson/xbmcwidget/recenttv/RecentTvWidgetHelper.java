package com.anderspersson.xbmcwidget.recenttv;

import java.util.List;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.common.Timer;
import com.anderspersson.xbmcwidget.common.XbmcApplication;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

public class RecentTvWidgetHelper implements IRecentTvWidgetHelper {

	private static final String NAVIGATE = "com.anderspersson.xbmcwidget.recenttv.NAVIGATE";
	
	private Timer refreshTimer = new Timer();
	
	public void onWidgetUpdate(Context context) {
		Intent intent = new Intent(context, RefreshRecentTvIntentService.class);
        context.startService(intent);
	}

	public void updateAppWidget(Context context,AppWidgetManager appWidgetManager, int widgetId) {
		createAndUpdateView(context, appWidgetManager, -1, -1, null);	
	    onWidgetUpdate(context);
	}

	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(NAVIGATE)) {
			moveTo(intent.getIntExtra("toIndex", -1), context);
		}
		else if(intent.getAction().equals(RefreshRecentTvIntentService.SHOWS_UPDATED)) {
			updateShows(context);
		}
	}

	public void onEnabled(Context context) {
		refreshTimer.enable(context);		
	}

	public void onDisabled(Context context) {
		refreshTimer.disable(context);		
	}
	
	private void updateShows(Context context) {
		List<TvShowEpisode> episodes = getEpisodes(context);
		
		if(episodes.size() == 0) {
			return;
		}
		
		createAndUpdateView(context, AppWidgetManager.getInstance(context), 0, episodes.size(), episodes.get(0));
	}
	
	private void moveTo(int toIndex, Context context) {
		List<TvShowEpisode> episodes = getEpisodes(context);
		
		if(episodes == null || toIndex < 0 || toIndex >= episodes.size()) {
			return;
		}
		
		createAndUpdateView(context, AppWidgetManager.getInstance(context), toIndex, episodes.size()-1, episodes.get(toIndex));
	}
	
	private void createAndUpdateView(Context context, AppWidgetManager appWidgetManager, int episodeIndex, int maxIndex, TvShowEpisode episode) {
		RemoteViews rv = new RemoteViews( context.getPackageName(), R.layout.recent_tv_widget );
		ComponentName recentTvWidget = new ComponentName( context, RecentTvWidget.class );
	    
		if(episode != null) {
			setupViewData(context, episodeIndex, maxIndex, episode, rv);
		}
		
	    appWidgetManager.updateAppWidget( recentTvWidget, rv );
	}

	private void setupViewData(Context context, int episodeIndex, int maxIndex, TvShowEpisode episode, RemoteViews rv) {
		Boolean leftArrowEnabled = episodeIndex != 0;
		Boolean rightArrowEnabled = episodeIndex != maxIndex;
		
		setupNavigationButton(context, rv, R.id.next_show, episodeIndex+1,  rightArrowEnabled);
		setupNavigationButton(context, rv, R.id.prev_show, episodeIndex-1, leftArrowEnabled);
		
		rv.setTextViewText(R.id.default_header, episode.getTvShowTitle());
		rv.setTextViewText(R.id.item_header, episode.getTvShowTitle());
		rv.setTextViewText(R.id.item_subheader, episode.getFullEpisodeTitle());
		rv.setTextViewText(R.id.age, "Aired\n" + episode.getAge());
		rv.setViewVisibility(R.id.new_icon, episode.hasBeenSeen() ?  View.INVISIBLE : View.VISIBLE);
	}
	
	private List<TvShowEpisode> getEpisodes(Context context) {
		XbmcApplication app = (XbmcApplication)context.getApplicationContext();
		
		List<TvShowEpisode> episodes = app.<List<TvShowEpisode>>getState("episodes");
		return episodes;
	}

	private void setupNavigationButton(Context context, RemoteViews remoteViews, int id, int toIndex, Boolean enabled) {
		
		Intent playIntent = new Intent(context, RecentTvWidget.class);
		playIntent.setAction(NAVIGATE);
		playIntent.putExtra("toIndex", toIndex);
		playIntent.setData(Uri.parse(playIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, 0);
        remoteViews.setOnClickPendingIntent(id, pendingIntent);
	}
}
