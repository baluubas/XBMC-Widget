package com.anderspersson.xbmcwidget.recentmovies;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.anderspersson.xbmcwidget.recentvideo.VideoUpdaterService;
import com.anderspersson.xbmcwidget.xbmc.GetRecentMoviesCommand;

public class RecentMoviesUpdater extends VideoUpdaterService {

	public RecentMoviesCache _moviesCache;
	
	public RecentMoviesUpdater(Context ctx) {
		super(ctx);
		
		_moviesCache = new RecentMoviesCache(ctx);
	}
	
	@Override
	protected Intent createRecentVideoRefreshIntent(UpdateResult result) {
		return new RecentMoviesRefreshedIntent(_ctx, result);
	}

	@Override
	protected UpdateResult tryRefreshData() {
		JSONObject movieData = getMoviesData();
		
		if(movieData == null)
			return UpdateResult.Failed;
		
		boolean isNewContentIdentical = false;
		try {
			isNewContentIdentical = _moviesCache.put(movieData);
		} catch (Exception e) {
			Log.e("RecentMoviesUpdater", "Unable to store movies in cache", e);
			return UpdateResult.Failed;
		}
		
		return isNewContentIdentical ? UpdateResult.NoChange : UpdateResult.Updated;
	}
	
	private JSONObject getMoviesData() {
		GetRecentMoviesCommand getMoviesCmd = new GetRecentMoviesCommand(
				PreferenceManager.getDefaultSharedPreferences(_ctx));
		
		try {
			return getMoviesCmd.execute();
		} catch (Exception ex) {
			Log.v("RefreshRecentMoviesIn..", "Failed to download movies", ex);
			return null;
		}
	}
}
