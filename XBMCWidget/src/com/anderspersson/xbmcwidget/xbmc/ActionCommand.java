package com.anderspersson.xbmcwidget.xbmc;

import android.content.SharedPreferences;

public class ActionCommand extends WebAPICommandBase {

	private String command;
	
	public ActionCommand(SharedPreferences sharedPref, String action) {
		super(sharedPref);
		command = "Action("+ action + ")";
	}

	@Override
	protected String getCommand() {
		return command;
	}
}
