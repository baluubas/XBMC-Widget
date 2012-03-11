package com.anderspersson.xbmcwidget.common;

import java.util.Date;

public class TimeAgo {
	public static String toFriendlyString(long time) {
		long now = new Date().getTime();
		
		long diffMinutes = (now - time) / (1000 * 60);
		
		
		if(diffMinutes <= 1) {
			return "just now";
		}
		
		if(diffMinutes < 60) {
			return diffMinutes + " minutes ago";
		}
		
		long diffHours = diffMinutes / 60;
		if(diffHours < 24) {
			return diffHours + " hours ago";
		}
		
		long diffDays = diffHours / 24; 
		
		if(diffDays < 7) {
			return diffDays + " days ago";
		}
		
		return "ages ago";
	} 
}
