package com.example.leafy;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "garden_plants")
public class GardenPlant {

    @PrimaryKey
    @NonNull
    private String name;
    private String imageUrl;
    private String sunlight;
    private String watering;
    private int wateringDays;
    private boolean hasReminder;
    private long reminderDate;
    private int reminderDays;

    public GardenPlant(@NonNull String name, String imageUrl, String sunlight, String watering, int wateringDays) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.sunlight = sunlight;
        this.watering = watering;
        this.wateringDays = wateringDays;
        this.hasReminder = false;
        this.reminderDate = 0;
        this.reminderDays = 0;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSunlight() {
        return sunlight;
    }

    public String getWatering() {
        return watering;
    }

    public int getWateringDays() {
        return wateringDays;
    }

    public boolean hasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public long getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(long reminderDate) {
        this.reminderDate = reminderDate;
    }

    public int getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(int reminderDays) {
        this.reminderDays = reminderDays;
    }
}
