package com.anderspersson.xbmcwidget;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class AboutPreferenceActivity extends Activity {	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v =  getLayoutInflater().inflate(R.layout.about, null);
		TextView message = (TextView)v.findViewById(R.id.about_message);
		message.setText(Html.fromHtml(getString(R.string.message_about)));	
		setContentView(v);
	}
	
}
