package com.example.leafy;
public class Search_Plant {
    private final String name;
    private final String growth;
    private final String soil;
    private final String sunlight;
    private final String watering;
    private final String fertilizationType;
    private final String imageURL;

    public Search_Plant(String name, String growth, String soil, String sunlight,
                        String watering, String fertilizationType, String imageURL) {
        this.name = name;
        this.growth = growth;
        this.soil = soil;
        this.sunlight = sunlight;
        this.watering = watering;
        this.fertilizationType = fertilizationType;
        this.imageURL = imageURL;
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

    public String getFertilizationType() {
        return fertilizationType;
    }

    public String getImageURL() {
        return imageURL;
    }
}
