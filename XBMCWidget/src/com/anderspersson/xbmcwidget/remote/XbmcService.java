package com.anderspersson.xbmcwidget.remote;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.anderspersson.xbmcwidget.common.ErrorNotifier;
import com.anderspersson.xbmcwidget.xbmc.*;

import android.app.IntentService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class XbmcService extends IntentService 
{	
	private Map<String, WebAPICommandBase> commandLookup;
	
	public XbmcService() {
		super("XBMCService");
	}

	@Override
	public void onHandleIntent(Intent intent) {		
		String action = intent.getAction();
		createCommandLookupTable();
		
		if(action == null)
			return;
		
		try {
			if(commandLookup.containsKey(action) == false) {
				return;
			}
			
			commandLookup.get(action).execute();
		} catch(Exception e) 
		{
			Log.w(XbmcService.class.getSimpleName(), e.getMessage() == null ? "Unknown error." : e.getMessage());
			ErrorNotifier.notify(this);	
		}		
	}	
	
    private void createCommandLookupTable() {
    	
    	if(commandLookup != null)
    		return;
    	
        Map<String, WebAPICommandBase> result = new HashMap<String, WebAPICommandBase>();
        result.put(RemoteWidget.PLAYPAUSE_ACTION, sendActionCommand("12"));
        result.put(RemoteWidget.STOP_ACTION, sendActionCommand("229"));
        result.put(RemoteWidget.UP_ACTION, sendKeyCommand("270"));
        result.put(RemoteWidget.DOWN_ACTION, sendKeyCommand("271"));
        result.put(RemoteWidget.RIGHT_ACTION, sendKeyCommand("273"));
        result.put(RemoteWidget.LEFT_ACTION, sendKeyCommand("272"));
        result.put(RemoteWidget.BACK_ACTION, sendKeyCommand("275"));
        result.put(RemoteWidget.SELECT_ACTION, sendKeyCommand("256"));
        result.put(RemoteWidget.CONTEXT_ACTION, sendCommand("ExecBuiltIn(Action(ContextMenu))"));
        result.put(RemoteWidget.HOME_ACTION, sendCommand("ExecBuiltIn(ActivateWindow(10000))"));
        result.put(RemoteWidget.TOGGLE_FULLSCREEN_ACTION, sendActionCommand("18"));
        result.put(RemoteWidget.TOGGLE_OSD_ACTION, sendKeyCommand("0xF04D"));
        // result.put(RemoteWidget.PLAY_EPISODE_ACTION, new PlayVideoCommand(getPrefManager(), intent.getExtras().getString(RecentTvWidget.EXTRA_ITEM)));
        
        commandLookup = Collections.unmodifiableMap(result);
    }
    
	private WebAPICommandBase sendActionCommand(String actionKey) {
		return new ActionCommand(getPrefManager(), actionKey);
	}

	private WebAPICommandBase sendKeyCommand(String key) {
		return new SendKeyCommand(getPrefManager(), key);
	}
	
	private WebAPICommandBase sendCommand(String command) {
		return new CustomCommand(getPrefManager(), command);
	}
	
	private SharedPreferences getPrefManager() {
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
}
