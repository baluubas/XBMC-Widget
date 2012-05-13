package com.anderspersson.xbmcwidget.recenttv;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.anderspersson.xbmcwidget.recentvideo.VideoUpdaterService;
import com.anderspersson.xbmcwidget.xbmc.GetRecentEpisodesCommand;

public class RecentTvUpdater extends VideoUpdaterService {

	private RecentTvCache _recentTvCache;
	
	public RecentTvUpdater(Context ctx) {
		super(ctx);
		_recentTvCache = new RecentTvCache(ctx);
	}

	@Override
	protected Intent createRecentVideoRefreshIntent(UpdateResult result) {
		return new RecentTvRefreshedIntent(_ctx, result);
	}
	
	@Override
	protected UpdateResult tryRefreshData() {
		JSONObject episodeData = getEpisodes();
		
		if(episodeData == null)
			return UpdateResult.Failed;
		
		boolean isNewContentIdentical;
		try {
			isNewContentIdentical = _recentTvCache.put(episodeData);
		} catch (Exception e) {
			Log.e("RecentTvUpdater", "Failed to store episodes in cache.", e);
			return UpdateResult.Failed;
		}
		
		return isNewContentIdentical ? UpdateResult.NoChange : UpdateResult.Updated;
	}
	
	private JSONObject getEpisodes() {
		GetRecentEpisodesCommand getEpisodesCmd = new GetRecentEpisodesCommand(PreferenceManager.getDefaultSharedPreferences(_ctx));
		
		try {
			return getEpisodesCmd.execute();
		} catch (Exception ex) {
			Log.v("RefreshRecentTvIn..", "Failed to download episodes", ex);
			return null;
		}
	}
	 
}
