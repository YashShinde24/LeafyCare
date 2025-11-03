package com.example.leafy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;



import java.util.Calendar;

public class ReminderManager {
    
    private final Context context;
    private final AlarmManager alarmManager;
    
    public ReminderManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public long scheduleReminder(GardenPlant plant, int days) {
        // Calculate the reminder time
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);

        // Create a unique ID for this plant's reminder
        int requestCode = plant.getName().hashCode();

        // Create the intent for the broadcast receiver
        Intent intent = new Intent(context, Reminderforplant.class);
        intent.putExtra("PLANT_NAME", plant.getName());
        intent.putExtra("REMINDER_MESSAGE", "Time to water your " + plant.getName() + "!");

        // Create the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        long triggerTime = calendar.getTimeInMillis();

        // 🔐 Android 12+ exact alarm permission check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent permissionIntent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                permissionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(permissionIntent);

                // Return early to prevent crash
                return -1;
            }
        }

        // Schedule the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }

        return triggerTime;
    }


    public void cancelReminder(GardenPlant plant) {
        int requestCode = plant.getName().hashCode();
        
        Intent intent = new Intent(context, Reminderforplant.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );
        
        // Cancel the alarm
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
