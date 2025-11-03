package com.example.leafy;

public class Plant {
    private final String name;
    private final String growth;
    private final String soil;
    private final String sunlight;
    private final String watering;
    private final String fertilizingType;
    private final String imageURL;
    
    // Fields for additional information
    private final String scientificName;
    private final String genus;
    private final String family;
    private final String additionalInfo;
    private final String wateringTips;
    private final String soilTips;
    private final String pruningTips;
    
    public Plant(String name, String growth, String soil, String sunlight, 
                String watering, String fertilizingType, String imageURL,
                String scientificName, String genus, String family, String additionalInfo,
                String wateringTips, String soilTips, String pruningTips) {
        this.name = name;
        this.growth = growth;
        this.soil = soil;
        this.sunlight = sunlight;
        this.watering = watering;
        this.fertilizingType = fertilizingType;
        this.imageURL = imageURL;
        this.scientificName = scientificName;
        this.genus = genus;
        this.family = family;
        this.additionalInfo = additionalInfo;
        this.wateringTips = wateringTips;
        this.soilTips = soilTips;
        this.pruningTips = pruningTips;
    }
    
    public String getName() {
        return name;
    }
    
    public String getGrowth() {
        return growth;
    }
    
    public String getSoil() {
        return soil;
    }
    
    public String getSunlight() {
        return sunlight;
    }
    
    public String getWatering() {
        return watering;
    }
    
    public String getFertilizingType() {
        return fertilizingType;
    }
    
    public String getImageURL() {
        return imageURL;
    }
    
    public String getScientificName() {
        return scientificName;
    }
    
    public String getGenus() {
        return genus;
    }
    
    public String getFamily() {
        return family;
    }
    
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    
    public String getWateringTips() {
        return wateringTips;
    }
    
    public String getSoilTips() {
        return soilTips;
    }
    
    public String getPruningTips() {
        return pruningTips;
    }
}
