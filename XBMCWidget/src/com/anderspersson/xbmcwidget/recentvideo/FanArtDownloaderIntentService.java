package com.anderspersson.xbmcwidget.recentvideo;

import com.anderspersson.xbmcwidget.recenttv.TvFanArtSize;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class FanArtDownloaderIntentService extends IntentService {
	
	public FanArtDownloaderIntentService() {
		super("FanArtDownloaderIntentService");
	}	
      
	@Override
	protected void onHandleIntent(Intent intent) {
		String path = intent.getStringExtra("path");
		
		CachedFanArtDownloader cachedFanArtDownloader = new CachedFanArtDownloader(this.getApplicationContext(), new TvFanArtSize(this));
		String fanartPathCachedOnStorage = cachedFanArtDownloader.download(path);
		
		if(fanartPathCachedOnStorage == null) return;
		
		StartRenderService(intent);
	}

	private void StartRenderService(Intent intent) {
		
		String replyToClassString = intent.getStringExtra("replyTo");
		Class<?> replayToClass;
		try {
			replayToClass = Class.forName(replyToClassString);
		} catch(ClassNotFoundException e) {
			Log.w("FanArtDownloadIntentService", "Unable to reply to class with name " + replyToClassString);
			return;
		}
		
		Intent fanArtDownloadedIntent = new Intent(this, replayToClass);
		fanArtDownloadedIntent.setAction(RecentVideoIntentActions.FANART_DOWNLOADED);
		startService(fanArtDownloadedIntent);
	}
}