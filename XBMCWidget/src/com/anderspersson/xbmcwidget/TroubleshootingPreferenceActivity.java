package com.anderspersson.xbmcwidget;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TroubleshootingPreferenceActivity extends Activity {	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v =  getLayoutInflater().inflate(R.layout.troubleshooting, null);
		TextView message = (TextView)v.findViewById(R.id.troubleshooting_message);
		message.setText(Html.fromHtml(getString(R.string.message_troubleshooting)));	
		setContentView(v);
	}
}
