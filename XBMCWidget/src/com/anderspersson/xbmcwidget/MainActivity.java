package com.anderspersson.xbmcwidget;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends Activity {
    
	@Override
	public void onCreate(Bundle save) {
		super.onCreate(save);
		
        Intent intent;
        
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) 
        	intent = new Intent(this, ApplicationPreferenceActivity.class);
        else 
        	intent = new Intent(this, ApplicationPreferenceActivityHC.class);
        
        startActivity(intent);
    }
}