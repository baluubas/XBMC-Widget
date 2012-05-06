package com.anderspersson.xbmcwidget.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class BitmapCache {
	private static int MaxCachedBitmaps = 20;
	private static String Suffix = ".bitmapcache";
	private Context _context;
	private IPublicStorage storage;
	
	public BitmapCache(Context context) {
		_context = context;
		
		storage = isExternalStorageAvailable() 
				? new SdCardStorage(context)
				: new InteralStorage(context);
				
		for(File f : storage.listFiles(new OnlyExtensionFilter(Suffix)))
			f.delete();
	}
	
	public Boolean has(String key) {	purgeOverflow();	
		return storage.hasFile(getFilenameForKey(key));
	}
	
	public String get(String key) {
		return storage.getAbsolutePath(getFilenameForKey(key));
	}
	
	public void put(String key, Bitmap bitmap) {
	
		purgeOverflow();
		
		OutputStream cacheStream = null;
		try {
			
			cacheStream = storage.getFileOuputStream(getFilenameForKey(key));
			bitmap.compress(CompressFormat.PNG, 100, cacheStream);
			
		} catch (Exception e) {
			Log.wtf("CachedFanArtDownloader", "Unable to create cache file.", e);
		} 
		finally {
			if(cacheStream == null)
				return;
			
			try {
				cacheStream.close();
			} catch (IOException e) {
				Log.wtf("CachedFanArtDownloader", "Failed to close stream.", e);
			}
		}
	}

	private void purgeOverflow() {
		File[] cachedFiles = storage.listFiles(new OnlyExtensionFilter(Suffix));
		
		if(cachedFiles.length < MaxCachedBitmaps)
			return;
		
		Arrays.sort(cachedFiles, new Comparator<File>(){
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()) * -1;
		    } });
		
		cachedFiles[0].delete();
	}
	
	private String getFilenameForKey(String key) {
		String keyFilename = key.replace('/', '.');
		return  keyFilename + Suffix;
	}
	
	private Boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) && _context.getExternalCacheDir() != null;
	}
}
