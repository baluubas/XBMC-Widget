package com.anderspersson.xbmcwidget.recentmovies;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecentMoviesRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecentMoviesRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

