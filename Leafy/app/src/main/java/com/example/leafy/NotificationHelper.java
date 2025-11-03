package com.example.leafy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    // Existing createNotification method
    public void createNotification(String title, String message, int taskId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a Notification Channel for Android 8.0+ (API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "TASK_REMINDER",
                    "Task Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for task reminders");
            notificationManager.createNotificationChannel(channel);
        }

        // PendingIntent for action button (Mark as Done)
        Intent doneIntent = new Intent(context, MarkAsDoneReceiver.class); // Receiver to handle the action
        doneIntent.putExtra("taskId", taskId); // Pass task ID
        PendingIntent donePendingIntent = PendingIntent.getBroadcast(
                context,
                taskId,
                doneIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TASK_REMINDER")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification) // Replace with your app's notification icon
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message)) // For longer messages
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_check, "Mark as Done", donePendingIntent); // Add action button

        // Trigger the Notification
        notificationManager.notify(taskId, builder.build());
    }

    // New method for customized plant notifications
    public void showCustomPlantNotification(String plantName, String taskPurpose, int taskId, int plantImageRes) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a Notification Channel for Android 8.0+ (API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "PLANT_REMINDER",
                    "Plant Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for plant reminders");
            notificationManager.createNotificationChannel(channel);
        }

        // PendingIntent for tapping the notification (navigate to app)
        Intent intent = new Intent(context, TaskActivity.class); // Replace with your target activity
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Get the large image for the notification
        Bitmap plantImage = BitmapFactory.decodeResource(context.getResources(), plantImageRes);

        // Build the Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "PLANT_REMINDER")
                .setContentTitle(plantName) // Plant name as the title
                .setContentText(taskPurpose) // Task purpose as the message
                .setSmallIcon(R.drawable.notification) // Replace with your app's small icon
                .setLargeIcon(plantImage) // Large icon shown on the notification
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(plantImage)
                        .bigLargeIcon((Bitmap) null)) // Avoid ambiguity by explicitly passing null as a Bitmap
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Trigger the Notification
        notificationManager.notify(taskId, builder.build());
    }

}
