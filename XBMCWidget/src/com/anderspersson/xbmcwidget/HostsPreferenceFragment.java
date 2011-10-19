package com.anderspersson.xbmcwidget;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class HostsPreferenceFragment extends PreferenceFragment {	
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.layout.host_setting);
   }
}


