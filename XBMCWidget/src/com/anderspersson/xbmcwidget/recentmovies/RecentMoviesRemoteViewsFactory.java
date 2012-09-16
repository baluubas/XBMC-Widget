package com.anderspersson.xbmcwidget.recentmovies;

import java.util.ArrayList;
import java.util.List;


import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.recentvideo.CachedFanArtDownloader;
import com.anderspersson.xbmcwidget.xbmc.Movie;
import com.anderspersson.xbmcwidget.xbmc.XbmcService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class RecentMoviesRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

	private int MaxNumberOfViews = 15;

	private List<Movie> movies = new ArrayList<Movie>();
	private Context _context;
	private CachedFanArtDownloader _fanArtDownloader;
	private RecentMoviesCache recentMoviesCache;
	
	public RecentMoviesRemoteViewsFactory(Context context, Intent intent) {
		this._context = context;
        this._fanArtDownloader = new CachedFanArtDownloader(context, new MovieFanArtSize(context));
        this.recentMoviesCache = new RecentMoviesCache(context);
	}

    public void onCreate() {
    }

    public void onDestroy() {
    }

    public int getCount() {
    	return movies.size() > MaxNumberOfViews 
        		? MaxNumberOfViews 
        		: movies.size();
    }

    @SuppressLint("NewApi")
	public RemoteViews getViewAt(int position) {
    	Movie movie = movies.get(position);
    	
    	RemoteViews rv = new RemoteViews(_context.getPackageName(), R.layout.recent_movie_item);
    	
    	SetImdbIntent(rv, movie.getImdbId());
    	
        rv.setViewVisibility(R.id.new_icon, getNewIconVisibility(movie));
        
        Bundle extras = new Bundle();
        extras.putString(XbmcService.EXTRA_ITEM, movie.getFile());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        fillInIntent.setAction(XbmcService.PLAY_EPISODE_ACTION);
        rv.setOnClickFillInIntent(R.id.recent_tv_item, fillInIntent);

       	String fanartPathCachedOnStorage = _fanArtDownloader.download(movie.getFanArtPath());
        
       	if(fanartPathCachedOnStorage != null) {
       		rv.setImageViewUri(R.id.fanArt, Uri.parse(fanartPathCachedOnStorage));
       		rv.setViewVisibility(R.id.default_header, View.INVISIBLE);
       	}
       	
        return rv;
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
			movies = recentMoviesCache.get();
		} catch (Exception e) {
			Log.e("RecentMoviesRemoteViewsFactory", 
				"Unable to get recent movies from cache", 
				e);
			movies = new ArrayList<Movie>();
		}
    }
    
	private int getNewIconVisibility(Movie movie) {
		return movie.hasBeenSeen() ?  View.INVISIBLE : View.VISIBLE;
	}
	
	@SuppressLint("NewApi")
	private void SetImdbIntent(RemoteViews rv, String imdbId) {
		String imdbUrl = "http://www.imdb.com/title/" + imdbId;
		Intent intent = new Intent().setData(Uri.parse(imdbUrl));
		intent.setAction(Intent.ACTION_VIEW);
		rv.setOnClickFillInIntent(R.id.imdb_button, intent);
	}
}