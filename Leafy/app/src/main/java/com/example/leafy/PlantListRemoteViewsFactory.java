package com.example.leafy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public class PlantListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private List<String> plantList;

    public PlantListRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        // Initialize the plant list
        plantList = new ArrayList<>();
        plantList.add("Aloe Vera");
        plantList.add("Money Plant");
        plantList.add("Spider Plant");
    }

    @Override
    public void onDataSetChanged() {
        // Update data here if needed (e.g., from a database)
    }

    @Override
    public void onDestroy() {
        plantList.clear();
    }

    @Override
    public int getCount() {
        return plantList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        @SuppressLint("RemoteViewLayout") RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        views.setTextViewText(R.id.widget_item_text, plantList.get(position));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("plant_name", plantList.get(position));
        views.setOnClickFillInIntent(R.id.widget_item_text, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
