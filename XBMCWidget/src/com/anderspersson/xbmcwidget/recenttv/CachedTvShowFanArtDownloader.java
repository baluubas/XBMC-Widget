package com.anderspersson.xbmcwidget.recenttv;

import com.anderspersson.xbmcwidget.common.BitmapCache;
import com.anderspersson.xbmcwidget.common.XbmcWidgetApplication;
import com.anderspersson.xbmcwidget.xbmc.DownloadBitmapCommand;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

public class CachedTvShowFanArtDownloader {
	private Context context;
	private BitmapCache _cache;
	
	public CachedTvShowFanArtDownloader(Context context) {
		this.context = context;
		this._cache = ((XbmcWidgetApplication)context.getApplicationContext()).getBitmapCache();
	}

	public Bitmap download(String url) {
		if(url == null)
			return null;
		
		if(_cache.has(url)) {
			return _cache.get(url);
		}
		
		try {
			SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			DownloadBitmapCommand downloadCommand = new DownloadBitmapCommand(defaultSharedPreferences, url);
			Bitmap result = downloadCommand.execute();
			
			if(result != null) {
				_cache.put(url, result);
			}
			
			return result;
			
		} catch(Exception ex) {
			Log.v(CachedTvShowFanArtDownloader.class.toString(), 
					String.format("Failed to download fanart '%s' - using default.", url), ex);
			return null;
		}
	}
}
