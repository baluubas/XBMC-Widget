package com.anderspersson.xbmcwidget.xbmc;

import org.json.JSONObject;

import android.content.SharedPreferences;

//
// { "jsonrpc": "2.0", "method": "Player.Open", "params": { "item": { "file": "smb://HP-MEDIA-SERVER/Photos/test3" } }, "id": 1 }
public class PlayVideoCommand extends XbmcRequest {

	public PlayVideoCommand(SharedPreferences preferences) {
		super(preferences);
	}

	public Boolean execute(String path) throws Exception {
		
		JSONObject request = new JSONObject();
		JSONObject item = new JSONObject();
		JSONObject parameters = new JSONObject();
		
		item.put("file", path);
		parameters.put("item", item);
		request.put("params", parameters);
		request.put("method", "Player.Open");
		request.put("id", 2);
		request.put("jsonrpc", "2.0");
		
		JSONObject reponse = super.jsonRequest(request);
		JSONObject error = reponse.optJSONObject("error");
		if(error != null)
			return false;
		return true;
	}
}
