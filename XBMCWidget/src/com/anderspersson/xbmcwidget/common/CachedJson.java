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
	private String _filename;

	private final Context _context;
	
	public CachedJson(Context context, String filename) {
		_context = context;
		_filename = filename;
	}

	public abstract List<SerializedType> parseJson(JSONObject jsonObject) throws JSONException;	
	
	public boolean isEmpty() {
		boolean hasFile = hasCacheFile() == false;
		return hasFile;
	}

	public List<SerializedType> get() throws IOException, JSONException {
		if(hasCacheFile() == false) {
			FileLog.appendLog(_filename + " - Get - no file");
			return new ArrayList<SerializedType>();
		}
			
		List<SerializedType> deserializedData =  _loadCachedData();
		FileLog.appendLog(_filename + " - Get - from file");
		return deserializedData;
	}
	
	public boolean put(JSONObject dataToStore) throws JSONException, IOException {
		// just make sure its possible to serialize
		parseJson(dataToStore);
		return storeEpisodes(dataToStore);
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
		return new String(buffer);
	}
	
	private boolean storeEpisodes(JSONObject json) throws IOException {
		
		String previousJson = "";
		
		if(hasCacheFile()) {
			previousJson = getFileAsString();
		}
		
		String newJson = json.toString();
		
		if(previousJson.equals(newJson)) {
			FileLog.appendLog(_filename + " - identical");
			return true;
		}
		
		FileOutputStream fos = _context.openFileOutput(_filename, Context.MODE_PRIVATE);
		fos.write(newJson.getBytes());
	    fos.close();
	    
	    FileLog.appendLog(_filename + " - saved");
	    return false;
	}
}
