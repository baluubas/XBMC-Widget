package com.anderspersson.xbmcwidget.recenttv;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.xbmc.GetRecentEpisodesCommand;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

public class RecentTvRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<TvShowEpisode> downloadedEpisodes = new ArrayList<TvShowEpisode>();

    private Context mContext;
	private int maxNumberOfViews = 15;
	private Boolean hadErrorsOnLastUpdate = false;
	private CachedTvShowFanArtDownloader fanArtDownloader;		

    public RecentTvRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        fanArtDownloader = new CachedTvShowFanArtDownloader(context);
    }

    public void onCreate() {
    }

    public void onDestroy() {
    	downloadedEpisodes.clear();
    }

    public int getCount() {
        return downloadedEpisodes.size() > maxNumberOfViews 
        		? maxNumberOfViews 
        		: downloadedEpisodes.size();
    }

    public RemoteViews getViewAt(int position) {
    	TvShowEpisode episode = downloadedEpisodes.get(position);
    	
    	RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recent_tv_item);
    	
        rv.setTextViewText(R.id.item_header, episode.getTvShowTitle());
        rv.setTextViewText(R.id.default_header, episode.getTvShowTitle());
        rv.setTextViewText(R.id.item_subheader, makeEpisodeTitle(episode));
        rv.setTextViewText(R.id.age, "Aired\n" + episode.getAge());
        rv.setInt(R.id.fanArt, "setBackgroundColor", getBorderColor());
        rv.setViewVisibility(R.id.new_icon, getNewIconVisibility(episode));
        
        Bundle extras = new Bundle();
        extras.putString(XbmcService.EXTRA_ITEM, episode.getFile());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.recent_tv_item, fillInIntent);

       	Bitmap fanArt = fanArtDownloader.download(episode.getFanArtPath());
        
       	if(fanArt != null) {
       		rv.setImageViewBitmap(R.id.fanArt, fanArt);
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

	private String makeEpisodeTitle(TvShowEpisode episode) {
		return episode.getEpisodeTitle() + " - S" + episode.getSeason() + "E" + episode.getEpisode();
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
    	GetRecentEpisodesCommand command = 
    			new GetRecentEpisodesCommand(PreferenceManager.getDefaultSharedPreferences(mContext));
    	try {
    		downloadedEpisodes = command.execute();	
    		hadErrorsOnLastUpdate = false;
		} catch (Exception ex) {
			hadErrorsOnLastUpdate = true;
			Log.v(RecentTvRemoteViewsFactory.class.toString(), "Failed to download episodes", ex);
			return;
		}
    }
}