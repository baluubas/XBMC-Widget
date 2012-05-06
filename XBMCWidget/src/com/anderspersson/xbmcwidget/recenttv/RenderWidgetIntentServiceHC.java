package com.anderspersson.xbmcwidget.recenttv;

import com.anderspersson.xbmcwidget.R;

public class RenderWidgetIntentServiceHC extends com.anderspersson.xbmcwidget.recentvideo.RenderWidgetIntentServiceHC {
	
	private RecentTvCache _recentTvCache;
	
	public RenderWidgetIntentServiceHC() {
		super("RenderWidgetIntentServiceHC (TV)");
		_recentTvCache = new RecentTvCache(this);
	}

	@Override
	protected boolean hasWidgetData() {
		return _recentTvCache.isEmpty() == false;
	}
	
	@Override
	protected Class<?> getWidgetClass() {
		return Widget.class;
	}

	@Override
	protected int getLoadingViewId() {
		return R.layout.recent_tv_widget_loading;
	}
	
	@Override
	protected Class<?> getRemoveViewsService() {
		return RecentTvRemoteViewsService.class;
	}

	@Override
	protected Class<?> getRefreshRecentIntentService() {
		return RecentTvUpdater.class;
	}
}
