package com.anderspersson.xbmcwidget.recentmovies;

import android.content.Context;

import com.anderspersson.xbmcwidget.common.ITimerCallback;
import com.anderspersson.xbmcwidget.recentvideo.RecentVideoWidget;



public class Widget extends RecentVideoWidget {

	@Override
	protected Class<?> getPreHCRenderIntentService() {
		return RenderWidgetIntentService.class;
	}

	@Override
	protected Class<?> getHCRenderIntentService() {
		return RenderWidgetIntentServiceHC.class;
	}

	@Override
	protected ITimerCallback getTimerCallback(Context ctx) {
		return new RecentMoviesUpdater(ctx);
	}

}