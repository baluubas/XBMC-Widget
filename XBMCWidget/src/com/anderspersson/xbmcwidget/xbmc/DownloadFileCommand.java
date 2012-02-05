package com.anderspersson.xbmcwidget.xbmc;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class DownloadFileCommand extends XbmcRequest {

	public DownloadFileCommand(SharedPreferences preferences) {
		super(preferences);
	}

	public Bitmap downloadFile(String fileUrl){
		try {
			HttpEntity entity = getRequest("/vfs/"+Uri.encode(fileUrl));
			
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			InputStream is = bufHttpEntity.getContent();	
			
			BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inSampleSize=4;
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, o);
			is.close();
			return bitmap;
		} catch (Exception e) {
			Log.w(DownloadFileCommand.class.getSimpleName(), e.getMessage());
			return null;
		}
	}
}
