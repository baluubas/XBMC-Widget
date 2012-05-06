package com.anderspersson.xbmcwidget.recentmovies;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.recentvideo.VideoUpdaterService;
import com.anderspersson.xbmcwidget.xbmc.GetRecentMoviesCommand;
import com.anderspersson.xbmcwidget.xbmc.Movie;

public class RecentMoviesUpdater extends VideoUpdaterService {

	public RecentMoviesUpdater(Context ctx) {
		super(ctx);
	}
	
	@Override
	protected Intent createRecentVideoRefreshIntent(boolean isSuccess) {
		return new RecentMoviesRefreshedIntent(_ctx, isSuccess);
	}

	@Override
	protected boolean tryRefreshData() {
		List<Movie> movies = getMovies();
		
		if(movies == null)
			return false;
		
		XbmcWidgetApplication app = (XbmcWidgetApplication)_ctx.getApplicationContext();
		app.updateDownloadedMovies(movies);
		
		return true;
	}
	
	private List<Movie> getMovies() {
		GetRecentMoviesCommand getEpisodesCmd = new GetRecentMoviesCommand(
				PreferenceManager.getDefaultSharedPreferences(_ctx));
		
		try {
			return getEpisodesCmd.execute();
		} catch (Exception ex) {
			Log.v("RefreshRecentMoviesIn..", "Failed to download movies", ex);
			return null;
		}
	}
}
