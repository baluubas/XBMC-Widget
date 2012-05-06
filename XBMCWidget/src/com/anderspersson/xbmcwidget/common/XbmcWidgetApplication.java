package com.anderspersson.xbmcwidget.common;

import java.util.ArrayList;
import java.util.List;

import com.anderspersson.xbmcwidget.xbmc.Movie;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

import android.app.Application;

public class XbmcWidgetApplication extends Application {

	private List<TvShowEpisode> lastDownloadedEpisodes = new ArrayList<TvShowEpisode>();
	private List<Movie> lastDownloadedMovies = new ArrayList<Movie>();
	
	private int currentEpisodeIndex = -1;
	private int currentMovieIndex = -1;
	
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
	
	public void setCurrentEpisodeIndex(int episodeIndex) {
		currentEpisodeIndex = episodeIndex;
	}
	
	public int getCurrentEpisodeIndex() {
		return currentEpisodeIndex;
	}

	public void updateDownloadedMovies(List<Movie> movies) {
		lastDownloadedMovies = movies;
	}

	public List<Movie> getLastDownloadedMovies() {
		return lastDownloadedMovies;
	}

	public int getCurrentMovieIndex() {
		return currentMovieIndex;
	}
	
	public void setCurrentMovieIndex(int movieIndex) {
		currentMovieIndex = movieIndex;
	}
}
