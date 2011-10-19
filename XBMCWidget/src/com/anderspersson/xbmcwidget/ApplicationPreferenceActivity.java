package com.anderspersson.xbmcwidget;

import java.util.List;

import android.preference.PreferenceActivity;
public class ApplicationPreferenceActivity extends PreferenceActivity {
	
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.layout.preference_headers, target);
	}
}