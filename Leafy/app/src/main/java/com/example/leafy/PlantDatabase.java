package com.example.leafy;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GardenPlant.class}, version = 1, exportSchema = false)
public abstract class PlantDatabase extends RoomDatabase {
    
    private static PlantDatabase instance;
    
    public abstract GardenPlantDao gardenPlantDao();
    
    public static synchronized PlantDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    PlantDatabase.class,
                    "plant_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
