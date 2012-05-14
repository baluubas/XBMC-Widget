package com.anderspersson.xbmcwidget.common;

import java.util.Date;

import com.anderspersson.xbmcwidget.R;

import android.content.Context;

public class TimeAgo {
	public static String toFriendlyString(Context ctx, long time) {
		long now = new Date().getTime();
		
		long diffMinutes = (now - time) / (1000 * 60);		
		
		if(diffMinutes <= 2) {
			return ctx.getString(R.string.lbl_just_now);
		}
		
		if(diffMinutes < 60) {
			return String.format(ctx.getString(R.string.lbl_minutes_ago), diffMinutes);
		}
		
		if(diffMinutes < 120) {
			return String.format(ctx.getString(R.string.lbl_hour_ago), 1);
		}
		
		long diffHours = diffMinutes / 60;
		if(diffHours < 24) {
			return String.format(ctx.getString(R.string.lbl_hours_ago), diffHours);
		}
		
		long diffDays = diffHours / 24; 
		
		if(diffDays == 1) {
			return ctx.getString(R.string.lbl_yesterday);
		}
		
		if(diffDays < 7) {
			return String.format(ctx.getString(R.string.lbl_days_ago), diffDays);
		}
		
		return ctx.getString(R.string.lbl_ages_ago);
	} 
}
