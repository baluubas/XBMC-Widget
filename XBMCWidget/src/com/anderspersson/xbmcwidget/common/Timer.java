package com.anderspersson.xbmcwidget.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Timer {
    private PendingIntent pendingIntent;
	
    public Timer(PendingIntent pendingIntent) {
		this.pendingIntent = pendingIntent;
	}

	public void enable(Context context) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String refreshIntervalMinutesStr = prefs.getString("recenttv_refresh_interval_preference", "30");
		int refreshIntervalMinutes = Integer.valueOf(refreshIntervalMinutesStr);
		int intervalMilliseconds = refreshIntervalMinutes * 60 * 1000;
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		   alarmManager.setRepeating(
				   AlarmManager.RTC_WAKEUP, 
				   System.currentTimeMillis() + intervalMilliseconds, 
				   intervalMilliseconds, 
				   pendingIntent);
	}

	public void disable(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
	}
}
