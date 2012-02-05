package com.anderspersson.xbmcwidget.configuration;

import java.util.List;

import com.anderspersson.xbmcwidget.R;

import android.preference.PreferenceActivity;

public class ApplicationPreferenceActivityHC extends PreferenceActivity {
	@Override
	public void onBuildHeaders(List<Header> target) {	
		loadHeadersFromResource(R.layout.preference_headers_hc, target);
	}
}