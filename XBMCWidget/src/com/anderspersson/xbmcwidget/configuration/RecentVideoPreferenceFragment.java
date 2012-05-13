package com.anderspersson.xbmcwidget.configuration;

import com.anderspersson.xbmcwidget.R;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

public class RecentVideoPreferenceFragment extends PreferenceFragment {	
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.recentvideo_setting);
	 }
	
	@Override
	public void onResume() {
		super.onResume();
		final EditTextPreference pref = (EditTextPreference)findPreference("recentvideo_last_refresh");
		new RecentVideoRefreshPrefUpdater(pref);
	}
}