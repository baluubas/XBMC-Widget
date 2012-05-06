package com.anderspersson.xbmcwidget.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.OutputStream;

import android.content.Context;

public class InteralStorage implements IPublicStorage {

	private final Context _context;

	public InteralStorage(Context context) {
		this._context = context;
	}
	
	public Boolean hasFile(String filename) {
		return _context.getFileStreamPath(filename).exists();
	}
	
	public OutputStream getFileOuputStream(String filename) throws FileNotFoundException {
		return _context.openFileOutput(filename,  Context.MODE_WORLD_READABLE);
	}
	
	@Override
	public String getAbsolutePath(String filenameForKey) {
		return _context.getFileStreamPath(filenameForKey).getAbsolutePath();
	}

	@Override
	public File[] listFiles(FilenameFilter filenameFilter) {
		 return _context.getFilesDir().listFiles(filenameFilter); 
	}
}
