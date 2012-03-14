package com.anderspersson.xbmcwidget.recenttv;

import android.content.Context;
import android.widget.Toast;

public class SentToXBMCToast  implements Runnable 
{
	private Context context;

	public SentToXBMCToast(Context context) {
		this.context = context; 
	}
	
	public void run(){
		Toast.makeText(context, "Playing on XBMC", Toast.LENGTH_SHORT).show();
	}
}
