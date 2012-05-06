package com.anderspersson.xbmcwidget.recenttv;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.anderspersson.xbmcwidget.common.CachedJson;
import com.anderspersson.xbmcwidget.xbmc.TvShowEpisode;

import android.content.Context;

public class RecentTvCache extends  CachedJson<TvShowEpisode> {

	public RecentTvCache(Context context) {
		super(context, "recent_tv_cache.txt");
	}

	public List<TvShowEpisode> parseJson(JSONObject jsonObject) throws JSONException {
		JSONArray episodes = jsonObject.getJSONObject("result").getJSONArray("episodes");
		
		ArrayList<TvShowEpisode> result = new ArrayList<TvShowEpisode>(); 
		for(int i = 0; i < episodes.length(); i++) {
			JSONObject episode = episodes.getJSONObject(i);
			result.add(new TvShowEpisode(
				episode.getString("file"),
				episode.getString("showtitle"), 
				episode.getString("title"), 
				episode.getInt("season"), 
				episode.getInt("episode"),
				episode.getString("fanart"),
				episode.getString("firstaired"),
				episode.getInt("playcount")));
		}
		
		return result;
	}
}
