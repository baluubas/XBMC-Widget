package com.anderspersson.xbmcwidget.xbmc;

import org.apache.http.HttpEntity;

import android.content.SharedPreferences;
import android.net.Uri;

public abstract class WebAPICommandBase extends XbmcRequest {

	public WebAPICommandBase(SharedPreferences preferences) {
		super(preferences);
	}
	
	protected abstract String getCommand();

	public HttpEntity execute() throws Exception {	
		return getRequest("/xbmcCmds/xbmcHttp?command="+ Uri.encode(getCommand()));
	}
}
