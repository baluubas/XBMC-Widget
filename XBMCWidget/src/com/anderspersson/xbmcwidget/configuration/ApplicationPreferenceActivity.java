package com.anderspersson.xbmcwidget.configuration;

import com.anderspersson.xbmcwidget.R;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class ApplicationPreferenceActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  	super.onCreate(savedInstanceState);
	  	addPreferencesFromResource(R.layout.settings_pre_v11);
	  	setUsername();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setLastRefreshed();
	}
	
	private void setUsername() {
		final EditTextPreference pref = (EditTextPreference)findPreference("username_preference");
	 	pref.setSummary(pref.getText());
	 	pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	          public boolean onPreferenceChange(Preference preference, Object newValue) {
	             final EditTextPreference pref = (EditTextPreference)findPreference("username_preference");
	             pref.setSummary((String)newValue);
	             return true;
	          }});
	}

	private void setLastRefreshed() {
		final EditTextPreference pref = (EditTextPreference)findPreference("recenttv_last_refresh");
		new RecentTvRefreshPrefUpdater(pref);
	}
}