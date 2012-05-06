package com.anderspersson.xbmcwidget.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;

import android.content.Context;


public class SdCardStorage implements IPublicStorage {
	
	private final Context _context;

	public SdCardStorage(Context ctx) {
		this._context = ctx;
	}
	
	public Boolean hasFile(String filename) {
		File file = new File(getAbsolutePath(filename));
		return file.exists();
	}
	
	public OutputStream getFileOuputStream(String filename) throws FileNotFoundException {
		return new FileOutputStream(getAbsolutePath(filename));
	}
	
	@Override
	public String getAbsolutePath(String filenameForKey) {
		return _context.getExternalCacheDir().getAbsolutePath() + "/" + filenameForKey;
	}

	@Override
	public File[] listFiles(FilenameFilter filenameFilter) {
		 return _context.getExternalCacheDir().listFiles(filenameFilter);
	}
}
