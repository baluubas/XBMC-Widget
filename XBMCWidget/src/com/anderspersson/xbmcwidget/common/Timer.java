package com.anderspersson.xbmcwidget.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Timer {
	private int updateIntervalMilliseconds = 30 * 60 * 1000;
    
	public static final String CLOCK_TICK = "com.anderspersson.xbmcwidget.recenttv.CLOCK_UPDATE";
    
    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(CLOCK_TICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

	public void enable(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		   alarmManager.setRepeating(
				   AlarmManager.RTC_WAKEUP, 
				   System.currentTimeMillis() + updateIntervalMilliseconds, 
				   updateIntervalMilliseconds, 
				   createClockTickIntent(context));
	}

	public void disable(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context));
	}

	public boolean isTickIntent(Intent intent) {
		return CLOCK_TICK.equals(intent.getAction());
	}
}
