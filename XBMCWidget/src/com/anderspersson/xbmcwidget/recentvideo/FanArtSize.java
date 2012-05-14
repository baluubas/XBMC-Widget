package com.anderspersson.xbmcwidget.recentvideo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public abstract class FanArtSize {
	
	private int _width = -1;
	private int _height = -1;
	private final Context context;
	private final int widthDp;
	private final int heightDp;
	
	public FanArtSize(Context context, int widthDp, int heightDp) {
		this.context = context;
		this.widthDp = widthDp;
		this.heightDp = heightDp;
	}
	
	public int getHeight() {
		calculateDimensions();
		return _height;
	}
	
	public int getWidth() {
		calculateDimensions();
		return _width;
	}
	
	private void calculateDimensions() {
		
		if(_width != -1)
			return;
		
		 DisplayMetrics metrics = new DisplayMetrics();
		 WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		 wm.getDefaultDisplay().getMetrics(metrics);
		 
		 _width = (int) (widthDp * metrics.density);
		 _height = (int) (heightDp * metrics.density);
		 
		int screenWidth =  metrics.widthPixels;
		int screenHeight = metrics.heightPixels;
		 
		boolean scaleToScreenSize = _width > screenWidth || _height > screenHeight;
		
		if(scaleToScreenSize == false) 
			return;
	    
		boolean isLandscape = widthDp > heightDp;
		
		if(isLandscape) {
			float ratio = (float)heightDp / (float)widthDp;
			_width = (int)(screenWidth * metrics.density);
			_height = (int)(Math.round(screenWidth * ratio) * metrics.density);
		}
		else {
			float ratio = (float)widthDp / (float)heightDp;
			_height = (int)(screenHeight * metrics.density);
			_width = (int)(Math.round(screenHeight * ratio) * metrics.density);
		}	 
	}
}
