package com.anderspersson.xbmcwidget.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public abstract class CachedJson<SerializedType> {
	private static String _filename;
	
	private List<SerializedType> _inmemoryCache;

	private final Context _context;
	
	public CachedJson(Context context, String filename) {
		_context = context;
		_filename = filename;
	}

	public abstract List<SerializedType> parseJson(JSONObject jsonObject) throws JSONException;	
	
	public boolean isEmpty() {
		if(_inmemoryCache != null && _inmemoryCache.size() > 0) {
			return false;
		}
		
		return hasCacheFile() == false;
	}

	public List<SerializedType> get() throws IOException, JSONException {
		
		if(hasCacheFile() == false)
			return new ArrayList<SerializedType>();
		
		if(_inmemoryCache != null) 
			return _inmemoryCache;
		
		_inmemoryCache =  _loadCachedData();
		return _inmemoryCache;
	}
	
	public boolean put(JSONObject dataToStore) throws JSONException, IOException {
		
		List<SerializedType> newSerializedData = parseJson(dataToStore);
		
		boolean isIdentical = storeEpisodes(dataToStore);
		_inmemoryCache = newSerializedData;
		return isIdentical;
	}
	
	private boolean hasCacheFile() {
		return _context.getFileStreamPath(_filename).exists();
	}

	private List<SerializedType> _loadCachedData() throws IOException, JSONException {
		String jsontext = getFileAsString();
		return parseJson(new JSONObject(jsontext));
	}

	private String getFileAsString() throws FileNotFoundException, IOException {
		InputStream is = _context.openFileInput(_filename);
		byte [] buffer = new byte[is.available()];
		while (is.read(buffer) != -1);
		is.close();
		String jsontext = new String(buffer);
		return jsontext;
	}
	
	private boolean storeEpisodes(JSONObject json) throws IOException {
		
		String previousJson = "";
		
		if(hasCacheFile()) {
			previousJson = getFileAsString();
		}
		
		String newJson = json.toString();
		
		if(previousJson.equals(newJson)) {
			return true;
		}
		
		FileOutputStream fos = _context.openFileOutput(_filename, Context.MODE_PRIVATE);
		fos.write(newJson.getBytes());
	    fos.close();
	    
	    return false;
	}
}
