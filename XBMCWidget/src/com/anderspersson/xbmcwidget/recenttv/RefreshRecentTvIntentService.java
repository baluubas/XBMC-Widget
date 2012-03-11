package com.anderspersson.xbmcwidget.recenttv;

import java.util.Date;
import java.util.List;

import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.xbmc.GetRecentEpisodesCommand;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

public class RefreshRecentTvIntentService extends IntentService {
	 
    public RefreshRecentTvIntentService() {
        super("RecentTvIntentService");
    }	    
    
    @Override
    protected void onHandleIntent(Intent intent) {
		refreshShows();
    }

	private void refreshShows() {
		if(isConnectedToWifi() == false) {
			return;
		}
		
		List<TvShowEpisode> episodes = getEpisodes();

		if(episodes == null) return;
		
		XbmcWidgetApplication app = (XbmcWidgetApplication)getApplication();
		
		app.updateDownloadedEpisodes(episodes);
		setLastUpdateTime();
		
		
		startService(new RecentTvRefreshedIntent(this));
	}

	private void setLastUpdateTime() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		editor.putString("recenttv_last_refresh", new Date().getTime() + "");
		editor.commit();
	}

	private List<TvShowEpisode> getEpisodes() {
		setLastUpdateTime();
		GetRecentEpisodesCommand getEpisodesCmd = new GetRecentEpisodesCommand(PreferenceManager.getDefaultSharedPreferences(this));
		
		try {
			return getEpisodesCmd.execute();
		} catch (Exception ex) {
			Log.v(RefreshRecentTvIntentService.class.toString(), "Failed to download episodes", ex);
			return null;
		}
	}

	private boolean isConnectedToWifi() {
		
		if(android.os.Debug.isDebuggerConnected()) {
			return true;
		}
		
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi != null && wifi.isAvailable() && wifi.isConnected();
	}
}
