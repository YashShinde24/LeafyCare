package com.example.leafy;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class WateringUtil {
    
    private static final Map<String, Integer> WATERING_MAP = new HashMap<>();
    private static final Map<Pattern, Integer> PATTERN_MAP = new HashMap<>();
    
    static {
        // Exact matches
        WATERING_MAP.put("Water Weekly", 7);
        WATERING_MAP.put("Water weekly", 7);
        WATERING_MAP.put("Water When Soil is Dry", 12); // 10-14 days average
        WATERING_MAP.put("Water when soil is dry", 12);
        WATERING_MAP.put("Keep Soil Evenly Moist", 3); // 3-4 days average
        WATERING_MAP.put("Keep soil evenly moist", 3);
        WATERING_MAP.put("Keep Soil Moist", 2); // 2-3 days average
        WATERING_MAP.put("Keep soil moist", 2);
        WATERING_MAP.put("Keep Soil Slightly Moist", 4); // 4-5 days average
        WATERING_MAP.put("Keep soil slightly moist", 4);
        WATERING_MAP.put("Let Soil Dry Between Watering", 8); // 7-10 days average
        WATERING_MAP.put("Let soil dry between watering", 8);
        WATERING_MAP.put("Water When Topsoil is Dry", 6); // 5-7 days average
        WATERING_MAP.put("Water when topsoil is dry", 6);
        WATERING_MAP.put("Water When Soil Feels Dry", 5); // 5-6 days average
        WATERING_MAP.put("Water when soil feels dry", 5);
        WATERING_MAP.put("Regular Watering", 4); // 3-5 days average
        WATERING_MAP.put("Regular watering", 4);
        WATERING_MAP.put("Regular, Moist Soil", 3); // 3-4 days average
        WATERING_MAP.put("Regular, moist soil", 3);
        WATERING_MAP.put("Regular, Well-Drained Soil", 5); // 4-6 days average
        WATERING_MAP.put("Regular, well-drained soil", 5);
        
        // Pattern matches for more flexible matching
        PATTERN_MAP.put(Pattern.compile("(?i).*weekly.*"), 7);
        PATTERN_MAP.put(Pattern.compile("(?i).*soil.*dry.*"), 10);
        PATTERN_MAP.put(Pattern.compile("(?i).*evenly moist.*"), 3);
        PATTERN_MAP.put(Pattern.compile("(?i).*soil moist.*"), 3);
        PATTERN_MAP.put(Pattern.compile("(?i).*slightly moist.*"), 4);
        PATTERN_MAP.put(Pattern.compile("(?i).*dry between.*"), 8);
        PATTERN_MAP.put(Pattern.compile("(?i).*topsoil.*dry.*"), 6);
        PATTERN_MAP.put(Pattern.compile("(?i).*feels dry.*"), 5);
        PATTERN_MAP.put(Pattern.compile("(?i).*regular.*"), 4);
        PATTERN_MAP.put(Pattern.compile("(?i).*moderate.*"), 5);
        PATTERN_MAP.put(Pattern.compile("(?i).*sparingly.*"), 10);
        PATTERN_MAP.put(Pattern.compile("(?i).*infrequent.*"), 14);
    }
    
    public static int getWateringDays(String wateringDescription) {
        // Check for exact match first
        if (WATERING_MAP.containsKey(wateringDescription)) {
            return WATERING_MAP.get(wateringDescription);
        }
        
        // Try pattern matching
        for (Map.Entry<Pattern, Integer> entry : PATTERN_MAP.entrySet()) {
            if (entry.getKey().matcher(wateringDescription).matches()) {
                return entry.getValue();
            }
        }
        
        // Default value if no match found
        return 7;
    }
}
