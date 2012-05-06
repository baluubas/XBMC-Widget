package com.anderspersson.xbmcwidget.recentmovies;

import java.util.List;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.xbmc.Movie;

public class RenderWidgetIntentServiceHC extends com.anderspersson.xbmcwidget.recentvideo.RenderWidgetIntentServiceHC {
	public RenderWidgetIntentServiceHC() {
		super("RenderWidgetIntentServiceHC (Movie)");
	}

	@Override
	protected boolean hasWidgetData() {
		return getMovies().size() > 0;
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
	protected Class<?> getRemoveViewsService() {
		return RecentMoviesRemoteViewsService.class;
	}
	
	@Override
	protected Class<?> getRefreshRecentIntentService() {
		return RecentMoviesUpdater.class;
	}
	
	private List<Movie> getMovies() {
		XbmcWidgetApplication app = getWidgetApplication();
		return app.getLastDownloadedMovies();
	}
}
