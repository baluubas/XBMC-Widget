package com.anderspersson.xbmcwidget.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.OutputStream;

public interface IPublicStorage {

	Boolean hasFile(String filename);

	String getAbsolutePath(String filenameForKey);

	OutputStream getFileOuputStream(String filenameForKey) throws FileNotFoundException;

	File[] listFiles(FilenameFilter filenameFilter);

}
