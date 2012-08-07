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
		JSONArray movies = json.getJSONObject("result").getJSONArray("movies");
		
		ArrayList<Movie> result = new ArrayList<Movie>(); 
		for(int i = 0; i < movies.length(); i++) {
			JSONObject movie = movies.getJSONObject(i);
			result.add(new Movie(
				movie.getString("title"),  
				movie.getString("thumbnail"),
				movie.getString("file"),
				movie.getInt("playcount"),
				movie.getString("imdbnumber")));
		}
		
		return result;
	}
}
