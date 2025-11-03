package com.example.leafy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

// ReminderReceiver.java: Triggers notifications for reminders
public class ReminderReceiver extends BroadcastReceiver {
    public static final String EXTRA_TASK_TITLE = "taskTitle"; // Define key as constant

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve the task title from the intent
        String taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE);

        // Provide a fallback title if taskTitle is null or empty
        if (TextUtils.isEmpty(taskTitle)) {
            taskTitle = "Reminder";
        }

        // Use a unique ID for the notification (e.g., using timestamp)
        int notificationId = (int) System.currentTimeMillis();

        // Create a notification using NotificationHelper
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification(taskTitle, "Don't forget to complete your task!", notificationId);
    }
}
