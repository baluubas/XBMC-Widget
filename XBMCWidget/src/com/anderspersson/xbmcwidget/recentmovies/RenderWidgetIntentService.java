package com.anderspersson.xbmcwidget.recentmovies;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.recentvideo.FanArtSize;
import com.anderspersson.xbmcwidget.xbmc.Movie;

public class RenderWidgetIntentService extends com.anderspersson.xbmcwidget.recentvideo.RenderWidgetIntentService {
	
	private RecentMoviesCache _moviesCache;
	
	public RenderWidgetIntentService() {
		super("RecentVideoWidgetRenderIntentService (Movies)");
		_moviesCache = new RecentMoviesCache(this);
	}
	
	@Override
	protected Class<?> getWidgetClass() {
		return Widget.class;
	}
	
	@Override
	protected int getLoadingViewId() {
		return R.layout.recent_movies_widget_loading;
	}
	
	@Override
	protected int getFailedViewId() {
		return R.layout.recent_movies_widget_failed;
	}

	@Override
	protected boolean hasWidgetData() {
		return getMovies().size() > 0;
	}

	@Override
	protected void refreshCurrent() {
		moveTo(getWidgetApplication().getCurrentMovieIndex());
	}

	@Override
	protected void moveTo(int toIndex) {

		List<Movie> movies = getMovies();
		
		if(movies == null || toIndex < 0 || toIndex >= movies.size()) {
			createFailedView();
			return;
		}
		
		setCurrentMovieIndex(toIndex);
		createAndUpdateView(toIndex, REFRESH_STATE.UNCHANGED);
	}

	@Override
	protected void setupViewData(int episodeIndex, RemoteViews rv, REFRESH_STATE state) {
		setupNavigationButtons(episodeIndex, rv);
		
		Movie movie = getMovies().get(episodeIndex);
		
		rv.setViewVisibility(R.id.new_icon, movie.hasBeenSeen() ?  View.INVISIBLE : View.VISIBLE);
		
		setupBorderColor(rv, state);
		setupTexts(movie, rv);
        setupClick(movie.getFile(), rv);
		setupFanArt(rv, movie.getFanArtPath(), R.drawable.default_movie_fan_art);
	}

	@Override
	protected int getMaxIndex() {
		return getMovies().size()-1;
	}
	
	@Override
	protected FanArtSize getFanArtSize() {
		return new MovieFanArtSize(this);
	}
	
	private void setupTexts(Movie movie, RemoteViews rv) {
		rv.setTextViewText(R.id.default_header, movie.getTitle());
	}
	
	private List<Movie> getMovies() {
		try {
			return _moviesCache.get();
		} catch (Exception e) {
			Log.e("RenderWidget", "Unable to get movies from cache.", e);
			return new ArrayList<Movie>();
		}
	}
	
	private void setCurrentMovieIndex(int movieIndex) {
		getWidgetApplication().setCurrentMovieIndex(movieIndex);
	}
}
