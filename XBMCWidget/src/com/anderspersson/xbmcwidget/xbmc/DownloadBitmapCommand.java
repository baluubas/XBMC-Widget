package com.anderspersson.xbmcwidget.xbmc;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;

import com.anderspersson.xbmcwidget.common.FlushedInputStream;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class DownloadBitmapCommand extends XbmcRequestBase<Bitmap> {

	private String _url;
	private final int _targetHeight;
	private final int _targetWidth;
	
	public DownloadBitmapCommand(SharedPreferences preferences, String url, int targetHeight, int targetWidth) {
		super(preferences);
		this._url = url;
		this._targetHeight = targetHeight;
		this._targetWidth = targetWidth;
	}
	
	public Bitmap execute(){
		try {
			
			int sampleSize = determineSampleSize();
			
			InputStream is = getBitmapStream();
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inSampleSize = sampleSize;
	
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, o);
			is.close();
			return bitmap;
		} catch (Exception e) {
			Log.w(DownloadBitmapCommand.class.getSimpleName(), e.getMessage());
			return null;
		}
	}
	
	private InputStream getBitmapStream() throws Exception {
		HttpEntity entity = getRequest(_url);
		
		BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
		return new FlushedInputStream(bufHttpEntity.getContent());	
	}

	private int determineSampleSize() throws Exception {
		
		Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(getBitmapStream(), null, options);
		Boolean scaleByHeight = Math.abs(options.outHeight - _targetHeight) >= Math.abs(options.outWidth - _targetWidth);

		if(options.outHeight * options.outWidth * 2 >= 200*200*2){
		         // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
		        double sampleSize = scaleByHeight
		              ? options.outHeight / _targetHeight
		              : options.outWidth / _targetWidth;
		        return (int)Math.pow(2d, Math.floor(Math.log(sampleSize)/Math.log(2d)));
		}
		
		return 0;
	}
}
