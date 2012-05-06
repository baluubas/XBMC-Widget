package com.anderspersson.xbmcwidget.recenttv;

import java.util.List;

import android.view.View;
import android.widget.RemoteViews;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

public class RenderWidgetIntentService extends com.anderspersson.xbmcwidget.recentvideo.RenderWidgetIntentService {
	public RenderWidgetIntentService() {
		super("RecentVideoWidgetRenderIntentService (TV)");
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
	protected boolean hasWidgetData() {
		return getEpisodes().size() > 0;
	}
	
	protected void refreshCurrent() {
		moveTo(getWidgetApplication().getCurrentEpisodeIndex());
	}
	
	protected void moveTo(int toIndex) {
		List<TvShowEpisode> episodes = getEpisodes();
		
		if(episodes == null || toIndex < 0 || toIndex >= episodes.size()) {
			return;
		}
		
		setCurrentEpisodeIndex(toIndex);
		createAndUpdateView(toIndex, REFRESH_STATE.UNCHANGED);
	}
	
	@Override
	protected int getMaxIndex() {
		return getEpisodes().size() - 1;
	}
	
	private List<TvShowEpisode> getEpisodes() {
		XbmcWidgetApplication app = getWidgetApplication();
		return app.getLastDownloadedEpisodes();
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
		rv.setTextViewText(R.id.age, "Aired\n" + episode.getAge());
	}
}
