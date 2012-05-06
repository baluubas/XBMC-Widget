package com.anderspersson.xbmcwidget.recentmovies;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anderspersson.xbmcwidget.common.CachedJson;
import com.anderspersson.xbmcwidget.xbmc.Movie;

import android.content.Context;

public class RecentMoviesCache extends CachedJson<Movie> {
	
	public RecentMoviesCache(Context context) {
		super(context, "recent_movies_cache.txt");
	}

	public List<Movie> parseJson(JSONObject json) throws JSONException {
		JSONArray episodes = json.getJSONObject("result").getJSONArray("movies");
		
		ArrayList<Movie> result = new ArrayList<Movie>(); 
		for(int i = 0; i < episodes.length(); i++) {
			JSONObject episode = episodes.getJSONObject(i);
			result.add(new Movie(
				episode.getString("title"),  
				episode.getString("thumbnail"),
				episode.getString("file"),
				episode.getInt("playcount")));
		}
		
		return result;
	}
}
