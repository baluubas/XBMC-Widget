package com.anderspersson.xbmcwidget.common;

import java.io.File;
import java.io.FilenameFilter;

public class OnlyExtensionFilter implements FilenameFilter { 
	String _ext; 
	
	public OnlyExtensionFilter(String ext) { 
		_ext = ext; 
	}
	
	public boolean accept(File dir, String name) { 
		return name.endsWith(_ext); 
	} 
}
