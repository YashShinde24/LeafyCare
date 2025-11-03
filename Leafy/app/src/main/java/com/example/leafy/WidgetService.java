package com.example.leafy;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PlantListRemoteViewsFactory(this.getApplicationContext());
    }
}
