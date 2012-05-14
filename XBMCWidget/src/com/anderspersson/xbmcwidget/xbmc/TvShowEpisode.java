package com.anderspersson.xbmcwidget.xbmc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.anderspersson.xbmcwidget.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class TvShowEpisode {
	
	private String tvShowTitle;
	private String episodeTitle;
	private int season;
	private int episode;
	private String fanArtPath;
	private String file;
	private Bitmap fanArt;
	private Date firstAired;
	private int playCount;
	
	public TvShowEpisode(
		String file, 
		String tvShowTitle, 
		String episodeTitle, 
		int season, 
		int episode, 
		String fanArtPath,
		String firstAired,
		int playCount) {
		
		this.file = file;
		this.tvShowTitle = tvShowTitle;
		this.episodeTitle = episodeTitle;
		this.season = season;
		this.episode = episode;
		this.fanArtPath = fanArtPath;
		this.playCount = playCount;
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("y-M-d");
			this.firstAired = formatter.parse(firstAired);
		}
		catch(Exception e) {
			Log.w(TvShowEpisode.class.getSimpleName(), String.format("Unable to parse %s as a date for '%s'", firstAired, file));
		}
	}
	
	public String getTvShowTitle() {
		return tvShowTitle;
	}
	public String getEpisodeTitle() {
		return episodeTitle;
	}
	public int getSeason() {
		return season;
	}
	public int getEpisode() {
		return episode;
	}
	public String getFanArtPath() {
		return fanArtPath;
	}

	public Bitmap getFanArt() {
		return fanArt;
	}
	
	public void setFanArtBitmap(Bitmap fanartBitmap) {
		fanArt = fanartBitmap;
	}

	public String getFile() {
		return file;
	}
	
	public String getAge(Context ctx) {
		
		if(firstAired == null) {
			return "";
		}
		
		int minutesOld = (int)((new Date().getTime() - firstAired.getTime()) / (60 * 1000));
		int minutesSinceMidnight = minutesSinceMidnight();
		
		if(minutesOld < minutesSinceMidnight)
			return ctx.getString(R.string.lbl_today); 
		if(minutesOld < minutesSinceMidnight + 60 * 24) {
			return ctx.getString(R.string.lbl_yesterday);
		}
		
		int daysOld = toDays(minutesOld);
		if(daysOld < 7)
		{
			return String.format(ctx.getString(R.string.lbl_d_ago), daysOld);
		}
		
		return String.format(ctx.getString(R.string.lbl_w_ago), daysOld / 7);	
	}
	
	private int toDays(int minutesOld) {
		return minutesOld / (60 * 24);
	}

	private int minutesSinceMidnight() {		
		DateFormat dateFormat = new SimpleDateFormat();      
		java.util.Date date = new java.util.Date();

		dateFormat = new SimpleDateFormat("HH");
		date = new java.util.Date();
		int hour = Integer.parseInt(dateFormat.format(date));         

		dateFormat = new SimpleDateFormat("mm");
		date = new java.util.Date();
		int minute = Integer.parseInt(dateFormat.format(date));

		return (hour * 60) + minute;       
	}

	public boolean hasBeenSeen() {
		return playCount > 0;
	}

	public String getFullEpisodeTitle() {
		return getEpisodeTitle() + " - S" + getSeason() + "E" + getEpisode();
	}
}
