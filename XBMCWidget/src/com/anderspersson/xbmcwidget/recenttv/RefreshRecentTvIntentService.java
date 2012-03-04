package com.anderspersson.xbmcwidget.recenttv;

import java.util.List;

import com.anderspersson.xbmcwidget.common.XbmcApplication;
import com.anderspersson.xbmcwidget.xbmc.GetRecentEpisodesCommand;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class RefreshRecentTvIntentService extends IntentService {
	 
	public static final String REFRESH = "com.anderspersson.xbmcwidget.recenttv.REFRESH_SHOWS";
	public static final String SHOWS_UPDATED =  "com.anderspersson.xbmcwidget.recenttv.SHOWS_UPDATED";
	
    public RefreshRecentTvIntentService() {
        super("RecentTvIntentService");
    }	
    
    
    @Override
    protected void onHandleIntent(Intent intent) {
		refreshShows();
    }

	private void refreshShows() {
		
		List<TvShowEpisode> episodes = getEpisodes();

		if(episodes == null) return;
		
		XbmcApplication app = (XbmcApplication)getApplication();
		
		app.setState("episodes", episodes);
		
		Intent i = new Intent(SHOWS_UPDATED);
		sendBroadcast(i);
	}


	private List<TvShowEpisode> getEpisodes() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		GetRecentEpisodesCommand getEpisodesCmd = new GetRecentEpisodesCommand(sharedPrefs);
		
		try {
			return getEpisodesCmd.execute();
		} catch (Exception ex) {
			Log.v(RefreshRecentTvIntentService.class.toString(), "Failed to download episodes", ex);
			return null;
		}
	}
}
