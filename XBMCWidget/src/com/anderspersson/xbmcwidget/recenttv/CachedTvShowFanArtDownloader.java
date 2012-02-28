package com.anderspersson.xbmcwidget.recenttv;

import java.util.HashMap;
import java.util.Iterator;

import com.anderspersson.xbmcwidget.xbmc.DownloadBitmapCommand;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

public class CachedTvShowFanArtDownloader {
	private Context context;
	private int MaxItems = 10;
	private HashMap<String, Bitmap> _cache = new HashMap<String, Bitmap>();
	
	public CachedTvShowFanArtDownloader(Context context) {
		this.context = context;
	}

	public Bitmap download(String url) {
		if(url == null)
			return null;
		
		if(has(url)) {
			return get(url);
		}
		
		try {
			SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			DownloadBitmapCommand downloadCommand = new DownloadBitmapCommand(defaultSharedPreferences, url);
			Bitmap result = downloadCommand.execute();
			
			if(result != null) {
				put(url, result);
			}
			
			return result;
			
		} catch(Exception ex) {
			Log.v(CachedTvShowFanArtDownloader.class.toString(), 
					String.format("Failed to download fanart '%s' - using default.", url), ex);
			return null;
		}
	}
		
	private Boolean has(String key) {
		return _cache.containsKey(key);
	}
	
	private Bitmap get(String key) {
		return _cache.get(key);
	}
	
	private void put(String key, Bitmap bitmap) {
		if(_cache.size() >= MaxItems) {
			Iterator<String> iterator = _cache.keySet().iterator();
		    _cache.remove(iterator.next());
		}
		
		_cache.put(key, bitmap);
	}
}
