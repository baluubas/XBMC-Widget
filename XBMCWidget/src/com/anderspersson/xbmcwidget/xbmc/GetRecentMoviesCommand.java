package com.anderspersson.xbmcwidget.xbmc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

public class GetRecentMoviesCommand extends XbmcRequestBase<JSONObject> {

	public GetRecentMoviesCommand(SharedPreferences preferences) {
		super(preferences);
	}

	public JSONObject execute() throws Exception {
		
		JSONObject response = TryGetRecentMovies(true);
		response = response == null ? TryGetRecentMovies(false) : response;
		return response;
	}

	private JSONObject TryGetRecentMovies(Boolean isDharmaAttempt) throws JSONException, Exception {
		
		JSONObject request = new JSONObject();
		JSONObject parameters = new JSONObject();
		JSONArray properties = new JSONArray();
		properties.put("title");
		properties.put("thumbnail");
		properties.put("file");
		properties.put("playcount");

		
		// Seems that the 2.0 json-rpc api changed somewhere after 
		// 10.1 Dharma release and fields was renamed to properties.
		if(isDharmaAttempt) 
			parameters.put("fields", properties);
		else
			parameters.put("properties", properties);
		
		request.put("method", "VideoLibrary.GetRecentlyAddedMovies");
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
