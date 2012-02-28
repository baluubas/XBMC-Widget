package com.anderspersson.xbmcwidget.xbmc;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;

import com.anderspersson.xbmcwidget.common.FlushedInputStream;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class DownloadBitmapCommand extends XbmcRequestBase<Bitmap> {

	private String fileUrl;
	
	public DownloadBitmapCommand(SharedPreferences preferences, String fileUrl) {
		super(preferences);
		this.fileUrl = fileUrl;
	}
	
	public Bitmap execute(){
		try {
			HttpEntity entity = getRequest("/vfs/"+Uri.encode(fileUrl));
			
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			InputStream is = new FlushedInputStream(bufHttpEntity.getContent());	
			
			BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inSampleSize=4;
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, o);
			is.close();
			return bitmap;
		} catch (Exception e) {
			Log.w(DownloadBitmapCommand.class.getSimpleName(), e.getMessage());
			return null;
		}
	}
}
