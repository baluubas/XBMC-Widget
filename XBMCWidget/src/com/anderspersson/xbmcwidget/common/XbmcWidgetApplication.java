package com.anderspersson.xbmcwidget.common;

import android.app.Application;

public class XbmcWidgetApplication extends Application {
	
	private int currentEpisodeIndex = 0;
	private int currentMovieIndex = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public void setCurrentEpisodeIndex(int episodeIndex) {
		currentEpisodeIndex = episodeIndex;
	}
	
	public int getCurrentEpisodeIndex() {
		return currentEpisodeIndex;
	}

	public int getCurrentMovieIndex() {
		return currentMovieIndex;
	}
	
	public void setCurrentMovieIndex(int movieIndex) {
		currentMovieIndex = movieIndex;
	}
}
