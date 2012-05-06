package com.anderspersson.xbmcwidget.recentmovies;

import com.anderspersson.xbmcwidget.R;

public class RenderWidgetIntentServiceHC extends com.anderspersson.xbmcwidget.recentvideo.RenderWidgetIntentServiceHC {
	
	private RecentMoviesCache _recentMoviesCache;
	
	public RenderWidgetIntentServiceHC() {
		super("RenderWidgetIntentServiceHC (Movie)");
		_recentMoviesCache = new RecentMoviesCache(this);
	}

	@Override
	protected boolean hasWidgetData() {
		return _recentMoviesCache.isEmpty() == false;
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
}
