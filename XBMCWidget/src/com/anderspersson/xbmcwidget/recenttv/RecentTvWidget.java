/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anderspersson.xbmcwidget.recenttv;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RecentTvWidget extends AppWidgetProvider {

	private IRecentTvWidgetHelper widgetHelper;
	

	public RecentTvWidget() {
		widgetHelper = getWidgetHelperBasedOnBuildVersion();
	}
	
	@Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        widgetHelper.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
       super.onEnabled(context);
       widgetHelper.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    	super.onReceive(context, intent);
        widgetHelper.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) { 
    	for (int i = 0; i < appWidgetIds.length; ++i) {
        	widgetHelper.updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
    	}
    	
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    
	private IRecentTvWidgetHelper getWidgetHelperBasedOnBuildVersion() {
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
	   		 return new RecentTvWidgetHelper();
	   	}   
	   	else {
	   		return new RecentTvWidgetHelperHC();
	   	}
	}
}