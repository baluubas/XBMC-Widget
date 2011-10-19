package com.anderspersson.xbmcwidget;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutPreferenceFragment extends Fragment {	
	 
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View aboutView = inflater.inflate(R.layout.about, container, false);
		
		TextView message = (TextView)aboutView.findViewById(R.id.about_message);
		message.setText(Html.fromHtml(getString(R.string.message_about)));   
		
		return aboutView;
	}
}