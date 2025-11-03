package com.example.leafy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationReceiverforTask extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            return; // Safety check
        }

        // Extract details from the intent
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        int taskId = intent.getIntExtra("taskId", (int) System.currentTimeMillis()); // Fallback task ID
        int plantImageResId = intent.getIntExtra("plantImageResId", R.drawable.navbar_header_logo); // Default image if not provided

        // Create NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification Channel for API 26+ (Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "TASK_REMINDER",
                    "Task Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminders for tasks and plants");
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // PendingIntent to open a specific activity
        Intent notificationIntent = new Intent(context, TaskActivity.class); // Replace with your target activity
        notificationIntent.putExtra("taskId", taskId);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);// Pass task ID to the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                taskId,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification with the plant image as the icon
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TASK_REMINDER")
                .setSmallIcon(plantImageResId) // Use the plant image as the notification icon
                .setContentTitle(title != null ? title : "Reminder")
                .setContentText(description != null ? description : "You have a pending task!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description != null ? description : "You have a pending task!")) // For longer text
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // Open activity when tapped
                .setAutoCancel(true);

        // Show the notification
        if (notificationManager != null) {
            notificationManager.notify(taskId, builder.build());
        }
    }
}
