package com.anderspersson.xbmcwidget.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceActivity;

import com.anderspersson.xbmcwidget.R;
import com.anderspersson.xbmcwidget.configuration.MainActivity;

public class ErrorNotifier {
	private static final int ERROR_ID = 1;
	private static String troubleshootingActivity = "com.anderspersson.xbmcwidget.configuration.TroubleshootingPreferenceFragment";
	
	public static void notify(Context context) {

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(
				R.drawable.ic_notification, 
				context.getText(R.string.error_ticker_text),
				System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL; 
		
		Intent notificationIntent = new Intent(
				Intent.ACTION_VIEW, 
				null, 
				context, 
				MainActivity.class);

		notificationIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, troubleshootingActivity);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		
		notification.setLatestEventInfo(
				context, 
				context.getText(R.string.error_content_title), 
				context.getText(R.string.error_content_text), 
				contentIntent);
		
		notificationManager.notify(ERROR_ID, notification);
	}
}
