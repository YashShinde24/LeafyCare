package com.example.leafy;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

public class PlantWidgetProvider extends AppWidgetProvider {

    private static final Handler handler = new Handler();
    private static int currentIndex = 0;
    private static Runnable updateRunnable;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // Use demo images for testing
        List<Integer> plantImages = getDemoImages();

        for (int appWidgetId : appWidgetIds) {
            if (plantImages.isEmpty()) {
                updateWidgetWithEmptyState(context, appWidgetManager, appWidgetId);
            } else {
                scheduleAutoSliding(context, appWidgetManager, appWidgetId, plantImages);
            }
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        if (updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    private List<Integer> getDemoImages() {
        List<Integer> demoImages = new ArrayList<>();
        demoImages.add(R.drawable.gulab); // Replace with your drawable resource
        demoImages.add(R.drawable.parijat); // Replace with your drawable resource
        demoImages.add(R.drawable.jasmin1); // Replace with your drawable resource
        return demoImages;
    }

    private void updateWidgetWithEmptyState(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.widget_empty_message, "No plants in your garden");
        views.setViewVisibility(R.id.widget_image, android.view.View.GONE);
        views.setViewVisibility(R.id.widget_empty_message, android.view.View.VISIBLE);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void updateWidgetWithPlantImage(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int imageResId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setImageViewResource(R.id.widget_image, imageResId);
        views.setViewVisibility(R.id.widget_image, android.view.View.VISIBLE);
        views.setViewVisibility(R.id.widget_empty_message, android.view.View.GONE);

        // Open GardenActivity on click
        Intent intent = new Intent(context, GardenFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void scheduleAutoSliding(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId, final List<Integer> plantImages) {
        if (updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (plantImages.isEmpty()) return;

                int imageResId = plantImages.get(currentIndex);
                updateWidgetWithPlantImage(context, appWidgetManager, appWidgetId, imageResId);

                currentIndex = (currentIndex + 1) % plantImages.size(); // Loop back to the first image
                handler.postDelayed(this, 5000); // Slide every 3 seconds
            }
        };

        handler.post(updateRunnable); // Start sliding
    }
}
