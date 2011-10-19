package com.anderspersson.xbmcwidget;

public class Title {

	private int mTextId;
	private int mIconId;
	private int mFragmentId;
	
	public Title(int textId, int iconId, int fragmentId) {
		mTextId = textId;
		mIconId = iconId;
		mFragmentId = fragmentId;
	}
	
	public int getTextId() {
		return mTextId;
	}
	
	public int getIconId() {
		return mIconId;
	}
	
	public int getFragmentId() {
		return mFragmentId;
	}

}
