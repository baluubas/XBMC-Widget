package com.anderspersson.xbmcwidget.recenttv;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.recentvideo.CachedFanArtDownloader;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

public class RecentTvRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    
    private Context _context;
	private CachedFanArtDownloader _fanArtDownloader;
	private RecentTvCache _recentEpisodesCache;
	
    private int maxNumberOfViews = 15;
	private Boolean hadErrorsOnLastUpdate = false;
	private List<TvShowEpisode> downloadedEpisodes = new ArrayList<TvShowEpisode>();
	
    public RecentTvRemoteViewsFactory(Context context, Intent intent) {
        _context = context;
        _fanArtDownloader = new CachedFanArtDownloader(context, new TvFanArtSize(context));
        _recentEpisodesCache = new RecentTvCache(context);
    }

    public void onCreate() {
    }

    public void onDestroy() {
    }

    public int getCount() {
        return downloadedEpisodes.size() > maxNumberOfViews 
        		? maxNumberOfViews 
        		: downloadedEpisodes.size();
    }

    public RemoteViews getViewAt(int position) {
    	TvShowEpisode episode = downloadedEpisodes.get(position);
    	
    	RemoteViews rv = new RemoteViews(_context.getPackageName(), R.layout.recent_tv_item);
    	
        rv.setTextViewText(R.id.item_header, episode.getTvShowTitle());
        rv.setTextViewText(R.id.default_header, episode.getTvShowTitle());
        rv.setTextViewText(R.id.item_subheader, episode.getFullEpisodeTitle());
        rv.setTextViewText(R.id.age, "Aired\n" + episode.getAge(_context));
        rv.setInt(R.id.fanArt, "setBackgroundColor", getBorderColor());
        rv.setViewVisibility(R.id.new_icon, getNewIconVisibility(episode));
        
        Bundle extras = new Bundle();
        extras.putString(XbmcService.EXTRA_ITEM, episode.getFile());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.recent_tv_item, fillInIntent);

       	String fanArtPathOnStorage = _fanArtDownloader.download(episode.getFanArtPath());
        
       	if(fanArtPathOnStorage != null) {
       		rv.setImageViewUri(R.id.fanArt, Uri.parse(fanArtPathOnStorage));
       		rv.setViewVisibility(R.id.default_header, View.INVISIBLE);
       	}
       	
        return rv;
    }

	private int getNewIconVisibility(TvShowEpisode episode) {
		return episode.hasBeenSeen() ?  View.INVISIBLE : View.VISIBLE;
	}

	private int getBorderColor() {
		return this.hadErrorsOnLastUpdate ? 
				Color.RED : 
				Color.rgb(169, 169, 169);
	}

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
    	try {
			downloadedEpisodes = _recentEpisodesCache.get();
		} catch (Exception e) {
			Log.e("RecentTvRemoteViewsFactory", "Unable to get cached episodes.", e);
			downloadedEpisodes = new ArrayList<TvShowEpisode>();
		}
    }
}