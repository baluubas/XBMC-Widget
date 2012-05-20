package com.anderspersson.xbmcwidget.common;

import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class TimerIntentService extends WakefulIntentService {

	public TimerIntentService() {
		super("TimerIntentService");
	}

	@Override
	protected void doWakefulWork(Intent intent) {
		TimerCallbacks timerCallbacks = new TimerCallbacks(this);
		FileLog.appendLog("Timer tick");
		for(ITimerCallback callback : timerCallbacks.getCallbackInstances()) {
			FileLog.appendLog("performUpdate: " + 
					callback.getClass().getSimpleName());	
			callback.performUpdate();
		}
	}

}
