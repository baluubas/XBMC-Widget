package com.anderspersson.xbmcwidget.xbmc;

import android.content.SharedPreferences;

public class CustomCommand extends WebAPICommandBase {

	private String command;
	
	public CustomCommand(SharedPreferences preferences, String command) {
		super(preferences);
		this.command = command;
	}

	@Override
	protected String getCommand() {
		return command;
	}

}
