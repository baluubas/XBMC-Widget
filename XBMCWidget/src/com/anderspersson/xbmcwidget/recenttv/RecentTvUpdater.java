package com.anderspersson.xbmcwidget.recenttv;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.recentvideo.VideoUpdaterService;
import com.anderspersson.xbmcwidget.xbmc.GetRecentEpisodesCommand;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

public class RecentTvUpdater extends VideoUpdaterService {

	public RecentTvUpdater(Context ctx) {
		super(ctx);
	}

	@Override
	protected Intent createRecentVideoRefreshIntent(boolean isSuccess) {
		return new RecentTvRefreshedIntent(_ctx, isSuccess);
	}
	
	@Override
	protected boolean tryRefreshData() {
		List<TvShowEpisode> episodes = getEpisodes();
		
		if(episodes == null)
			return false;
		
		XbmcWidgetApplication app = (XbmcWidgetApplication)_ctx.getApplicationContext();
		app.updateDownloadedEpisodes(episodes);
		
		return true;
	}
	
	private List<TvShowEpisode> getEpisodes() {
		GetRecentEpisodesCommand getEpisodesCmd = new GetRecentEpisodesCommand(PreferenceManager.getDefaultSharedPreferences(_ctx));
		
		try {
			return getEpisodesCmd.execute();
		} catch (Exception ex) {
			Log.v("RefreshRecentTvIn..", "Failed to download episodes", ex);
			return null;
		}
	}
	 
}
