package com.anderspersson.xbmcwidget.recenttv;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.xbmc.DownloadFileCommand;
import com.anderspersson.xbmcwidget.xbmc.GetRecentEpisodesCommand;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<TvShowEpisode> downloadedEpisodes = new ArrayList<TvShowEpisode>();
    private Context mContext;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    public void onCreate() { 
    }

    public void onDestroy() {
    	downloadedEpisodes.clear();
    }

    public int getCount() {
        return downloadedEpisodes.size() > 10 ? 10 : downloadedEpisodes.size();
    }

    public RemoteViews getViewAt(int position) {
        
    	TvShowEpisode episode = downloadedEpisodes.get(position);
    	
    	RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recent_tv_item);
        //rv.setTextViewText(R.id.widget_item, mWidgetItems.get(position).text);
        if(episode.getFanArt() != null) {
        	rv.setImageViewBitmap(R.id.fanArt, episode.getFanArt());
        }
        else
        	rv.setImageViewResource(R.id.fanArt, R.drawable.default_fan_art);
        
        rv.setTextViewText(R.id.item_header, episode.getTvShowTitle());
        rv.setTextViewText(R.id.item_subheader, makeEpisodeTitle(episode));
        rv.setTextViewText(R.id.age, episode.getAge());
        
        Bundle extras = new Bundle();
        extras.putString(RecentTvWidget.EXTRA_ITEM, episode.getFile());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.recent_tv_item, fillInIntent);

        return rv;
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
		} catch (Exception ex) {
			Log.v(StackRemoteViewsFactory.class.toString(), "Failed to download episodes", ex);
			return;
		}
    	
    	for(int i=0; i < getCount(); i++)
		{
    		TvShowEpisode episode = downloadedEpisodes.get(i);
    		episode.setFanArtBitmap(GetTvShowBitmap(episode));
		}
    }

	private Bitmap GetTvShowBitmap(TvShowEpisode episode) {
		
		try {
			SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
			DownloadFileCommand downloadCommand = new DownloadFileCommand(defaultSharedPreferences);
	    	String fanArtPath = episode.getFanArtPath();
			return downloadCommand.downloadFile(fanArtPath);
		} catch(Exception ex) {
			Log.v(StackRemoteViewsFactory.class.toString(), String.format("Failed to download fanart '%s' - using default.", episode.getFanArtPath()), ex);
			return null;
		}
	}
}