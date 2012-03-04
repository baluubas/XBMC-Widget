package com.anderspersson.xbmcwidget.xbmc;

import java.text.SimpleDateFormat;
import java.util.Date;


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
	private Date fileDate;
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
			fileDate = formatter.parse(firstAired);
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
	
	public String getAge() {
		
		if(fileDate == null) {
			return "";
		}
		
		int daysOld = (int)((new Date().getTime() - fileDate.getTime()) / (1000 * 60 * 60 * 24));
	
		if(daysOld > 7) 
			return daysOld / 7 + "w ago";
		if(daysOld > 2) {
			return daysOld + "d ago";
		}
		if(daysOld > 0) {
			return "yesterday";
		}
		
		return "today";
	}

	public boolean hasBeenSeen() {
		return playCount > 0;
	}

	public String getFullEpisodeTitle() {
		return getEpisodeTitle() + " - S" + getSeason() + "E" + getEpisode();
	}
}
