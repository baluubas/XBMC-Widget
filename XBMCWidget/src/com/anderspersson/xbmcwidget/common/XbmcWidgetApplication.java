package com.anderspersson.xbmcwidget.common;

import java.util.ArrayList;
import java.util.List;

import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

import android.app.Application;

public class XbmcWidgetApplication extends Application {

	private List<TvShowEpisode> lastDownloadedEpisodes = new ArrayList<TvShowEpisode>();
	private BitmapCache bitmapCache = new BitmapCache();
	private int currentEpisodeIndex = -1;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public List<TvShowEpisode> getLastDownloadedEpisodes() {
		return lastDownloadedEpisodes;
	}
	
	public void updateDownloadedEpisodes(List<TvShowEpisode> newEpisodes) {
		lastDownloadedEpisodes = newEpisodes;
	}
	
	public BitmapCache getBitmapCache() {
		return bitmapCache;
	}

	public void setCurrentEpisodeIndex(int episodeIndex) {
		currentEpisodeIndex = episodeIndex;
	}
	
	public int getCurrentEpisodeIndex() {
		return currentEpisodeIndex;
	}
}
