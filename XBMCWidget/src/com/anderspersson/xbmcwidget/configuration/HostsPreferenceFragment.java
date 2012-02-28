package com.anderspersson.xbmcwidget.configuration;

import com.anderspersson.xbmcwidget.R;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;


public class HostsPreferenceFragment extends PreferenceFragment {	
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.layout.host_setting);
       
       final EditTextPreference pref = (EditTextPreference)findPreference("username_preference");
   		pref.setSummary(pref.getText());
   		pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
   			public boolean onPreferenceChange(Preference preference, Object newValue) {
   				final EditTextPreference pref = (EditTextPreference)findPreference("username_preference");
   				pref.setSummary(pref.getText());
   				return true;
   			}
   		});
   }
}


