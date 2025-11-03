package com.example.leafy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GardenRepository {
    
    private final GardenPlantDao gardenPlantDao;
    private final LiveData<List<GardenPlant>> allGardenPlants;
    private final ExecutorService executorService;
    
    public GardenRepository(Application application) {
        PlantDatabase database = PlantDatabase.getInstance(application);
        gardenPlantDao = database.gardenPlantDao();
        allGardenPlants = gardenPlantDao.getAllPlants();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public LiveData<List<GardenPlant>> getAllGardenPlants() {
        return allGardenPlants;
    }
    
    public void insert(GardenPlant plant) {
        executorService.execute(() -> gardenPlantDao.insert(plant));
    }
    
    public void delete(GardenPlant plant) {
        executorService.execute(() -> gardenPlantDao.delete(plant));
    }
    
    public boolean isPlantInGarden(String plantName) {
        try {
            return new IsPlantInGardenTask(gardenPlantDao).execute(plantName).get() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static class IsPlantInGardenTask extends AsyncTask<String, Void, Integer> {
        private final GardenPlantDao gardenPlantDao;
        
        IsPlantInGardenTask(GardenPlantDao gardenPlantDao) {
            this.gardenPlantDao = gardenPlantDao;
        }
        
        @Override
        protected Integer doInBackground(String... strings) {
            return gardenPlantDao.isPlantInGarden(strings[0]);
        }
    }
}
