package com.anderspersson.xbmcwidget.xbmc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

public class GetRecentEpisodesCommand extends XbmcRequestBase<JSONObject> {

	public GetRecentEpisodesCommand(SharedPreferences preferences) {
		super(preferences);
	}

	public JSONObject execute() throws Exception {
		JSONObject response = TryGetRecentEpisodes(true);
		response = response == null ? TryGetRecentEpisodes(false) : response;
		return response;
	}

	private JSONObject TryGetRecentEpisodes(Boolean isDharmaAttempt) throws JSONException, Exception {
		
		JSONObject request = new JSONObject();
		JSONObject parameters = new JSONObject();
		JSONArray properties = new JSONArray();
		properties.put("title");
		properties.put("showtitle");
		properties.put("episode");
		properties.put("fanart");
		properties.put("season");
		properties.put("file");
		properties.put("firstaired");
		properties.put("playcount");

		
		// Seems that the 2.0 json-rpc api changed somewhere after 
		// 10.1 Dharma release and fields was renamed to properties.
		if(isDharmaAttempt) 
			parameters.put("fields", properties);
		else
			parameters.put("properties", properties);
		
		request.put("method", "VideoLibrary.GetRecentlyAddedEpisodes");
		request.put("id", 1);
		request.put("jsonrpc", "2.0");
		request.put("params", parameters);
		
		JSONObject reponse = super.jsonRequest(request);
		
		JSONObject error = reponse.optJSONObject("error");
		if(error != null)
			return null;
		
		return reponse;
	}
}
