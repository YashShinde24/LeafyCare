package com.example.leafy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class GardenViewModel extends AndroidViewModel {
    
    private final GardenRepository repository;
    private final LiveData<List<GardenPlant>> allGardenPlants;
    
    public GardenViewModel(@NonNull Application application) {
        super(application);
        repository = new GardenRepository(application);
        allGardenPlants = repository.getAllGardenPlants();
    }
    
    public LiveData<List<GardenPlant>> getAllGardenPlants() {
        return allGardenPlants;
    }
    
    public void addPlantToGarden(GardenPlant plant) {
        repository.insert(plant);
    }
    
    public void removePlantFromGarden(GardenPlant plant) {
        repository.delete(plant);
    }
    
    public boolean isPlantInGarden(String plantName) {
        return repository.isPlantInGarden(plantName);
    }
}
