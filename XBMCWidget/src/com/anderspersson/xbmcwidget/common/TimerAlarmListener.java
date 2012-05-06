package com.anderspersson.xbmcwidget.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class TimerAlarmListener implements WakefulIntentService.AlarmListener {
	
	private int interval;
	
	public void scheduleAlarms(AlarmManager mgr, PendingIntent pi, Context ctxt) {
		
		interval = getInterval(ctxt);	
		mgr.setInexactRepeating(
				AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() - interval,
				interval, 
				pi);
	}

	private int getInterval(Context ctxt) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
		String refreshIntervalMinutesStr = prefs.getString("recenttv_refresh_interval_preference", "30");
		int refreshIntervalMinutes = Integer.valueOf(refreshIntervalMinutesStr);
		int intervalMilliseconds = refreshIntervalMinutes * 60 * 1000;
		return intervalMilliseconds;
	}

	public void sendWakefulWork(Context ctxt) {
		WakefulIntentService.sendWakefulWork(ctxt, TimerIntentService.class);
	}

	public long getMaxAge() {
		return interval == 0 ? AlarmManager.INTERVAL_HALF_HOUR*2 : interval * 2;
	}
}