package com.anderspersson.xbmcwidget.recenttv;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.recentvideo.FanArtSize;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

public class RenderWidgetIntentService extends com.anderspersson.xbmcwidget.recentvideo.RenderWidgetIntentService {
	
	private RecentTvCache _recentTvCache;
	
	public RenderWidgetIntentService() {
		super("RecentVideoWidgetRenderIntentService (TV)");
		_recentTvCache = new RecentTvCache(this);
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
	protected int getFailedViewId() {
		return R.layout.recent_tv_widget_failed;
	}
	
	@Override
	protected boolean hasWidgetData() {
		return _recentTvCache.isEmpty() == false;
	}
	
	protected void refreshCurrent() {
		moveTo(getWidgetApplication().getCurrentEpisodeIndex());
	}
	
	protected void moveTo(int toIndex) {
		List<TvShowEpisode> episodes = getEpisodes();
		
		if(episodes == null || toIndex < 0 || toIndex >= episodes.size()) {
			createFailedView();
			return;
		}
		
		setCurrentEpisodeIndex(toIndex);
		createAndUpdateView(toIndex, REFRESH_STATE.UNCHANGED);
	}
	
	@Override
	protected int getMaxIndex() {
		return getEpisodes().size() - 1;
	}
	
	@Override
	protected FanArtSize getFanArtSize() {
		return new TvFanArtSize(this);
	}
	
	private List<TvShowEpisode> getEpisodes() {
		try {
			return _recentTvCache.get();
		} catch (Exception e) {
			Log.e("RenderWidget", "Unable to get recent tv from cache.", e);
			return new ArrayList<TvShowEpisode>();
		} 
	}
	
	private void setCurrentEpisodeIndex(int episodeIndex) {
		getWidgetApplication().setCurrentEpisodeIndex(episodeIndex);
	}
	
	protected void setupViewData(int episodeIndex, RemoteViews rv, REFRESH_STATE state) {
		setupNavigationButtons(episodeIndex, rv);
		
		TvShowEpisode episode = getEpisodes().get(episodeIndex);
		
		rv.setViewVisibility(R.id.new_icon, episode.hasBeenSeen() ?  View.INVISIBLE : View.VISIBLE);
		
		setupBorderColor(rv, state);
		setupTexts(episode, rv);
        setupClick(episode.getFile(), rv);
		setupFanArt(rv, episode.getFanArtPath(), R.drawable.default_tv_fan_art);
	}

	private void setupTexts(TvShowEpisode episode, RemoteViews rv) {
		rv.setTextViewText(R.id.default_header, episode.getTvShowTitle());
		rv.setTextViewText(R.id.item_header, episode.getTvShowTitle());
		rv.setTextViewText(R.id.item_subheader, episode.getFullEpisodeTitle());
		rv.setTextViewText(R.id.age, "Aired\n" + episode.getAge(this));
	}
}
