package com.example.leafy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GardenPlantDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GardenPlant plant);
    
    @Delete
    void delete(GardenPlant plant);
    
    @Query("SELECT * FROM garden_plants ORDER BY name ASC")
    LiveData<List<GardenPlant>> getAllPlants();
    
    @Query("SELECT COUNT(*) FROM garden_plants WHERE name = :name")
    int isPlantInGarden(String name);
}
