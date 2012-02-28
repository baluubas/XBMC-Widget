package com.anderspersson.xbmcwidget.xbmc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.anderspersson.xbmcwidget.common.ErrorNotifier;
import com.anderspersson.xbmcwidget.remote.RemoteWidget;
import android.app.IntentService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class XbmcService extends IntentService 
{	
    public static final String PLAYPAUSE_ACTION = "com.anderspersson.xbmcwidget.PLAY";
    public static final String PAUSE_ACTION = "com.anderspersson.xbmcwidget.PAUSE";
    public static final String UP_ACTION = "com.anderspersson.xbmcwidget.UP";
    public static final String DOWN_ACTION = "com.anderspersson.xbmcwidget.DOWN";
    public static final String LEFT_ACTION = "com.anderspersson.xbmcwidget.LEFT";
    public static final String RIGHT_ACTION = "com.anderspersson.xbmcwidget.RIGHT";
    public static final String SELECT_ACTION = "com.anderspersson.xbmcwidget.SELECT";
    public static final String BACK_ACTION = "com.anderspersson.xbmcwidget.BACK";
    public static final String STOP_ACTION = "com.anderspersson.xbmcwidget.STOP";
    public static final String TOGGLE_FULLSCREEN_ACTION = "com.anderspersson.xbmcwidget.TOGGLEFULLSCREEN";
    public static final String TOGGLE_OSD_ACTION = "com.anderspersson.xbmcwidget.TOGGLEOSD";
    public static final String HOME_ACTION = "com.anderspersson.xbmcwidget.HOME";
    public static final String CONTEXT_ACTION = "com.anderspersson.xbmcwidget.CONTEXT";
    public static final String PLAY_EPISODE_ACTION = "com.anderspersson.xbmcwidget.PLAY_EPISODE_ACTION";
    public static final String EXTRA_ITEM = "com.anderspersson.xbmcwidget.EXTRA_ITEM";
	
	public XbmcService() {
		super("XBMCService");
	}

	@Override
	public void onHandleIntent(Intent intent) {		
		XbmcRequest command = getCommandForIntent(intent);
		
		if(command == null)
			return;
		
		try {			
			command.executeObject();
		} catch(Exception e) 
		{
			Log.w(XbmcService.class.getSimpleName(), e.getMessage() == null ? "Unknown error." : e.getMessage());
			ErrorNotifier.notify(this);	
		}		
	}	
	
    private XbmcRequest getCommandForIntent(Intent intent)  {
    	
    	String action = intent.getAction();
		
    	if(intent.getAction() == null)
			return null;
    	
    	if(action.equals(PLAYPAUSE_ACTION)) return sendActionCommand("12");
    	if(action.equals(STOP_ACTION)) return sendActionCommand("229");
    	if(action.equals(UP_ACTION)) return sendKeyCommand("270");
    	if(action.equals(DOWN_ACTION)) return sendKeyCommand("271");
    	if(action.equals(RIGHT_ACTION)) return sendKeyCommand("273");
    	if(action.equals(LEFT_ACTION)) return sendKeyCommand("272");
    	if(action.equals(BACK_ACTION)) return sendKeyCommand("275");
    	if(action.equals(SELECT_ACTION)) return sendKeyCommand("256");
    	if(action.equals(CONTEXT_ACTION)) return sendCommand("ExecBuiltIn(Action(ContextMenu))");
    	if(action.equals(HOME_ACTION)) return sendCommand("ExecBuiltIn(ActivateWindow(10000))");
    	if(action.equals(TOGGLE_FULLSCREEN_ACTION)) return sendActionCommand("18");
    	if(action.equals(TOGGLE_OSD_ACTION)) return sendKeyCommand("0xF04D");
    	if(action.equals(PLAY_EPISODE_ACTION)) 
    		return new PlayVideoCommand(getPrefManager(), intent.getExtras().getString(EXTRA_ITEM));
        
    	return null;
    }
    
	private XbmcRequest sendActionCommand(String actionKey) {
		return new ActionCommand(getPrefManager(), actionKey);
	}

	private XbmcRequest sendKeyCommand(String key) {
		return new SendKeyCommand(getPrefManager(), key);
	}
	
	private XbmcRequest sendCommand(String command) {
		return new CustomCommand(getPrefManager(), command);
	}
	
	private SharedPreferences getPrefManager() {
		return PreferenceManager.getDefaultSharedPreferences(this);
	}
}
