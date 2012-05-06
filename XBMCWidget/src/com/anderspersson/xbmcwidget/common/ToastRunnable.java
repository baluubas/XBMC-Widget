package com.anderspersson.xbmcwidget.common;

import android.content.Context;
import android.widget.Toast;

public class ToastRunnable  implements Runnable 
{
	private Context context;
	private String message;

	public ToastRunnable(Context context, String message) {
		this.context = context; 
		this.message = message;
	}
	
	public void run(){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
