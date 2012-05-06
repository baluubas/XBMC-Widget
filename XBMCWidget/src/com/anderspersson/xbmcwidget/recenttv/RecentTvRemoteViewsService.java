package com.anderspersson.xbmcwidget.recenttv;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecentTvRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecentTvRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

