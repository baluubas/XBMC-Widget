package com.anderspersson.xbmcwidget.configuration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class PreferenceChangedListener implements OnSharedPreferenceChangeListener {

	public static final String PREFERENCE_CHANGED = "com.anderspersson.xbmcwidget.common.PREF_CHANGED";
	private Context context;

	public PreferenceChangedListener(Context context) {
		this.context = context;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Intent i = new Intent();
		i.setAction(PREFERENCE_CHANGED);
		context.sendBroadcast(i);
	}
}
