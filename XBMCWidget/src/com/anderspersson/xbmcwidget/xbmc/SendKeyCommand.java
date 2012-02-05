package com.anderspersson.xbmcwidget.xbmc;

import android.content.SharedPreferences;

public class SendKeyCommand extends WebAPICommandBase {
	
	private String command;
	
	public SendKeyCommand(SharedPreferences sharedPref, String key) {
		super(sharedPref);
		command = "SendKey("+ key + ")";
	}

	@Override
	protected String getCommand() {
		return command;
	}
}
