package com.anderspersson.xbmcwidget.recenttv;

import java.util.List;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;


public class RenderWidgetIntentServiceHC extends com.anderspersson.xbmcwidget.recentvideo.RenderWidgetIntentServiceHC {
	public RenderWidgetIntentServiceHC() {
		super("RenderWidgetIntentServiceHC (TV)");
	}

	@Override
	protected boolean hasWidgetData() {
		return getEpisodes().size() > 0;
	}
	
	private List<TvShowEpisode> getEpisodes() {
		XbmcWidgetApplication app = getWidgetApplication();
		return app.getLastDownloadedEpisodes();
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
