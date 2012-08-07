package com.anderspersson.xbmcwidget.xbmc;

public class Movie {

	private String _title;
	private String _fanart;
	private String _file;
	private String _imdbId;
	private int _playCount;

	public Movie(String title, String fanart, String file, int playCount, String imdbId) {
		this._title = title;
		this._file = file;
		this._fanart = fanart;
		this._playCount = playCount;
		this._imdbId = imdbId; 
	}

	public CharSequence getTitle() {
		return _title;
	}

	public String getFile() {
		return _file;
	}

	public String getFanArtPath() {
		return _fanart;
	}

	public boolean hasBeenSeen() {
		return _playCount > 0;
	}	
	
	public String getImdbId() {
		return _imdbId;
	}
}
