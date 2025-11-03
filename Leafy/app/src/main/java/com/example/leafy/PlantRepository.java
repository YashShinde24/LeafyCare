package com.example.leafy;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PlantRepository {
    
    private static final String TAG = "PlantRepository";
    private final Context context;
    
    public PlantRepository(Context context) {
        this.context = context;
    }
    
    public List<Plant> loadPlants() {
        List<Plant> plants = new ArrayList<>();
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("plants.csv")));
            
            // Skip header line
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = parseCsvLine(line);
                if (tokens.length >= 7) {
                    try {
                        // Map CSV columns to Plant object fields
                        String name = tokens[0];
                        String growth = tokens[1];
                        String soil = tokens[2];
                        String sunlight = tokens[3];
                        String watering = tokens[4];
                        String fertilizingType = tokens[5];
                        String imageURL = tokens[6];
                        
                        // Additional fields with default values if not present
                        String scientificName = tokens.length > 7 ? tokens[7] : "Unknown";
                        String genus = tokens.length > 8 ? tokens[8] : "Unknown";
                        String family = tokens.length > 9 ? tokens[9] : "Unknown";
                        String additionalInfo = tokens.length > 10 ? tokens[10] : "No additional information available";
                        String wateringTips = tokens.length > 11 ? tokens[11] : "No watering tips available";
                        String soilTips = tokens.length > 12 ? tokens[12] : "No soil tips available";
                        String pruningTips = tokens.length > 13 ? tokens[13] : "No pruning tips available";
                        
                        com.example.leafy.Plant plant= new com.example.leafy.Plant(
                                name,
                                growth,
                                soil,
                                sunlight,
                                watering,
                                fertilizingType,
                                imageURL,
                                scientificName,
                                genus,
                                family,
                                additionalInfo,
                                wateringTips,
                                soilTips,
                                pruningTips
                        );
                        plants.add(plant);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing line: " + line, e);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading CSV file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing reader", e);
                }
            }
        }
        
        return plants;
    }
    
    public Plant getPlantByName(String name) {
        List<Plant> plants = loadPlants();
        for (Plant plant : plants) {
            if (plant.getName().equals(name)) {
                return plant;
            }
        }
        return null;
    }
    
    // Improved CSV parser that handles quoted fields and commas within fields
    private String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(currentToken.toString().trim());
                currentToken = new StringBuilder();
            } else {
                currentToken.append(c);
            }
        }
        
        // Add the last token
        tokens.add(currentToken.toString().trim());
        
        return tokens.toArray(new String[0]);
    }
}
