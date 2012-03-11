package com.anderspersson.xbmcwidget.recenttv;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;


public class FanArtDownloaderIntentService extends IntentService {
	 
	public static final String FANART_DOWNLOADED = "com.anderspersson.xbmcwidget.recenttv.FANART_DOWNLOADED";
	
	public FanArtDownloaderIntentService() {
		super("FanArtDownloaderIntentService");
	}	
      
	@Override
	protected void onHandleIntent(Intent intent) {
		String url = intent.getStringExtra("path");
		
		CachedTvShowFanArtDownloader cachedFanArtDownloader = new CachedTvShowFanArtDownloader(this.getApplicationContext());
		Bitmap fanArt = cachedFanArtDownloader.download(url);
		
		if(fanArt == null) return;
		
		Intent fanArtDownloadedIntent = new Intent(this, RecentTvWidgetRenderIntentService.class);
		fanArtDownloadedIntent.setAction(FANART_DOWNLOADED);
		startService(fanArtDownloadedIntent);
	}
}