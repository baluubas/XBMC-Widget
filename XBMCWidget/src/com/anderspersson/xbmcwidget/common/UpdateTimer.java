package com.anderspersson.xbmcwidget.common;

import android.content.Context;

import com.commonsware.cwac.wakeful.*;

public class UpdateTimer  {

	public void reset(Context ctx) {
		WakefulIntentService.cancelAlarms(ctx);
		WakefulIntentService.scheduleAlarms(new TimerAlarmListener(), ctx);	
	}

	public void addCallback(Context ctx, ITimerCallback timerCallback) {
		TimerCallbacks timerCallbacks = new TimerCallbacks(ctx);
		timerCallbacks.add(timerCallback.getClass()); 
		
		reset(ctx);
	}

	public void removeCallback(Context context, ITimerCallback timerCallback) {
		TimerCallbacks timerCallbacks = new TimerCallbacks(context);
		timerCallbacks.remove(timerCallback.getClass());
	}
}
