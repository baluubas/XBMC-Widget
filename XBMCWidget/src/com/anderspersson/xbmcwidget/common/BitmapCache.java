package com.anderspersson.xbmcwidget.common;

import java.util.HashMap;
import java.util.Iterator;

import android.graphics.Bitmap;

public class BitmapCache {
	private int MaxItems = 10;

	private HashMap<String, Bitmap> _cache = new HashMap<String, Bitmap>();
	
	public Boolean has(String key) {
		return _cache.containsKey(key);
	}
	
	public Bitmap get(String key) {
		return _cache.get(key);
	}
	
	public void Put(String key, Bitmap bitmap) {
		if(_cache.size() >= MaxItems) {
			Iterator<String> iterator = _cache.keySet().iterator();
		    _cache.remove(iterator.next());
		}
		
		_cache.put(key, bitmap);
	}
}
