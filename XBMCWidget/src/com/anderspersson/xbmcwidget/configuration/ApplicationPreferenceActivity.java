package com.anderspersson.xbmcwidget.configuration;

import com.anderspersson.xbmcwidget.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ApplicationPreferenceActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  	super.onCreate(savedInstanceState);
	  	addPreferencesFromResource(R.layout.settings_pre_v11);
	}
}