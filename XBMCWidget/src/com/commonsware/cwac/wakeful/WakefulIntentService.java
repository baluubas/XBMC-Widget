/***
  Copyright (c) 2009-11 CommonsWare, LLC

  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.commonsware.cwac.wakeful;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

abstract public class WakefulIntentService extends IntentService {
	abstract protected void doWakefulWork(Intent intent);

	static final String NAME="com.commonsware.cwac.wakeful.WakefulIntentService";
	static final String LAST_ALARM="lastAlarm";
	private static volatile PowerManager.WakeLock lockStatic=null;
	private static WifiManager.WifiLock lockWifi = null;
	
	public static void acquireStaticLock(Context context) {
		if (!getCpuLock(context).isHeld()) {
			getCpuLock(context).acquire();
		}

		WifiManager.WifiLock lock = getWifiLock(context);
		try {
			if (!lock.isHeld()) {
				lock.acquire();
			}
		}
		catch (UnsupportedOperationException ex) {
			// too many wifi locks, couldn't acquire one
			// swallow it. oh well, no wifi lock this time.
		}
	}
	
	synchronized private static PowerManager.WakeLock getCpuLock(Context context) {
		if (lockStatic==null) {
			PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
			lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, NAME);
			lockStatic.setReferenceCounted(true);
		}

		return(lockStatic);
	}
	
	synchronized protected static WifiManager.WifiLock getWifiLock(Context context) {
		if (lockWifi == null) {
			WifiManager mgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

			// wake up the WiFi
			lockWifi = mgr.createWifiLock(NAME);
			lockWifi.setReferenceCounted(true);
		}

		return lockWifi;
	}

	public static void sendWakefulWork(Context ctxt, Intent i) {
		acquireStaticLock(ctxt.getApplicationContext());
		ctxt.startService(i);
	}

	public static void sendWakefulWork(Context ctxt, Class<?> clsService) {
		sendWakefulWork(ctxt, new Intent(ctxt, clsService));
	}

	public static void scheduleAlarms(AlarmListener listener, Context ctxt) {
		scheduleAlarms(listener, ctxt, true);
	}

	public static void scheduleAlarms(AlarmListener listener, Context ctxt, boolean force) {

		SharedPreferences prefs=ctxt.getSharedPreferences(NAME, 0);
		long lastAlarm=prefs.getLong(LAST_ALARM, 0);

		if (lastAlarm==0 || force ||
				(System.currentTimeMillis()>lastAlarm &&
						System.currentTimeMillis()-lastAlarm>listener.getMaxAge())) {
			AlarmManager mgr=(AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
			Intent i=new Intent(ctxt, AlarmReceiver.class);
			PendingIntent pi=PendingIntent.getBroadcast(ctxt, 0, i, 0);

			listener.scheduleAlarms(mgr, pi, ctxt);
		}
	}

	public static void cancelAlarms(Context ctxt){
		AlarmManager mgr=(AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		Intent i=new Intent(ctxt, AlarmReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(ctxt, 0, i, 0);

		mgr.cancel(pi);
	}

	public WakefulIntentService(String name) {
		super(name);
		setIntentRedelivery(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if ((flags & START_FLAG_REDELIVERY)!=0) { // if crash restart or ...
			acquireStaticLock(this.getApplicationContext());  // ...then quick grab the lock
		}

		super.onStartCommand(intent, flags, startId);

		return(START_REDELIVER_INTENT);
	}

	@Override
	final protected void onHandleIntent(Intent intent) {
		try {
			doWakefulWork(intent);
		}
		finally {
			releaseStaticLock();
		}
	}
	
	@Override
	public void onDestroy() {
		releaseStaticLock();
		super.onDestroy();
	}

	private void releaseStaticLock() {
		if (getCpuLock(this).isHeld()) {
			getCpuLock(this).release();
		}
		if (getWifiLock(this).isHeld()) {
			getWifiLock(this).release();
		}
		
	}

	public interface AlarmListener {
		void scheduleAlarms(AlarmManager mgr, PendingIntent pi,
				Context ctxt);
		void sendWakefulWork(Context ctxt);
		long getMaxAge();
	}
}