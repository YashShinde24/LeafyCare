package com.example.leafy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

public class PlantDetailActivity extends AppCompatActivity {
    
    public static final String EXTRA_PLANT_NAME = "extra_plant_name";
    
    private ViewFlipper viewFlipper;
    private TabLayout tabLayout;
    private TextView additionalInfoValue;
    private ImageView expandIcon;
    private LinearLayout infoHeader;
    private boolean isInfoExpanded = false;
    private GardenViewModel gardenViewModel;
    private Button addToGardenButton;
    private Plant currentPlant;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        
        // Initialize views
        viewFlipper = findViewById(R.id.view_flipper);
        tabLayout = findViewById(R.id.tab_layout);
        additionalInfoValue = findViewById(R.id.additional_info_value);
        expandIcon = findViewById(R.id.expand_icon);
        infoHeader = findViewById(R.id.info_header);
        addToGardenButton = findViewById(R.id.add_to_garden_button);
        
        // Initialize ViewModel
        gardenViewModel = new ViewModelProvider(this).get(GardenViewModel.class);
        
        // Set up back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());
        
        // Get plant name from intent
        String plantName = getIntent().getStringExtra(EXTRA_PLANT_NAME);
        if (plantName == null || plantName.isEmpty()) {
            finish();
            return;
        }
        
        // Get plant details
        PlantRepository plantRepository = new PlantRepository(this);
        currentPlant = plantRepository.getPlantByName(plantName);
        
        if (currentPlant != null) {
            displayPlantDetails(currentPlant);
            updateAddToGardenButton(currentPlant.getName());
        } else {
            Toast.makeText(this, "Plant not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        
        // Set up tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewFlipper.setDisplayedChild(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
        
        // Set up expandable information section
        infoHeader.setOnClickListener(v -> toggleInfoSection());
        
        // Set up add to garden button
        addToGardenButton.setOnClickListener(v -> handleAddToGarden());
    }
    
    private void updateAddToGardenButton(String plantName) {
        boolean isInGarden = gardenViewModel.isPlantInGarden(plantName);
        if (isInGarden) {
            addToGardenButton.setText(R.string.already_in_garden);
            addToGardenButton.setEnabled(false);
        } else {
            addToGardenButton.setText(R.string.add_to_garden);
            addToGardenButton.setEnabled(true);
        }
    }
    
    private void handleAddToGarden() {
        if (currentPlant != null) {
            int wateringDays = WateringUtil.getWateringDays(currentPlant.getWatering());
            
            GardenPlant gardenPlant = new GardenPlant(
                    currentPlant.getName(),
                    currentPlant.getImageURL(),
                    currentPlant.getSunlight(),
                    currentPlant.getWatering(),
                    wateringDays
            );
            
            gardenViewModel.addPlantToGarden(gardenPlant);
            
            Toast.makeText(this, 
                    getString(R.string.plant_added_to_garden, currentPlant.getName()), 
                    Toast.LENGTH_SHORT).show();
            
            updateAddToGardenButton(currentPlant.getName());
        }
    }
    
    private void toggleInfoSection() {
        isInfoExpanded = !isInfoExpanded;
        additionalInfoValue.setVisibility(isInfoExpanded ? View.VISIBLE : View.GONE);
        expandIcon.setImageResource(isInfoExpanded ? 
                R.drawable.ic_expand_less : R.drawable.ic_expand_more);
    }
    
    private void displayPlantDetails(Plant plant) {
        // Set plant name
        TextView plantNameTextView = findViewById(R.id.plant_name_text_view);
        plantNameTextView.setText(plant.getName());
        
        // Load plant image
        ImageView plantImageView = findViewById(R.id.plant_image);
        String imageUrl = plant.getImageURL();
        if (!TextUtils.isEmpty(imageUrl) && !imageUrl.equals("Error fetching")) {
            Glide.with(this)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(plantImageView);
        } else {
            // Set placeholder if no image URL or error
            plantImageView.setImageResource(R.drawable.placeholder_image);
        }
        
        // Set General Info values
        TextView sunlightValue = findViewById(R.id.sunlight_value);
        TextView wateringValue = findViewById(R.id.watering_value);
        TextView soilValue = findViewById(R.id.soil_value);
        TextView growthValue = findViewById(R.id.growth_value);
        TextView fertilizingValue = findViewById(R.id.fertilizing_value);
        
        sunlightValue.setText(plant.getSunlight());
        wateringValue.setText(plant.getWatering());
        soilValue.setText(plant.getSoil());
        growthValue.setText(plant.getGrowth());
        fertilizingValue.setText(plant.getFertilizingType());
        
        // Set Care values
        TextView wateringValueCare = findViewById(R.id.watering_value_care);
        TextView soilValueCare = findViewById(R.id.soil_value_care);
        TextView pruningValue = findViewById(R.id.pruning_value);
        
        // Use specific tips if available, otherwise use general info
        wateringValueCare.setText(TextUtils.isEmpty(plant.getWateringTips()) || 
                plant.getWateringTips().equals("Error") ? 
                plant.getWatering() : plant.getWateringTips());
                
        soilValueCare.setText(TextUtils.isEmpty(plant.getSoilTips()) || 
                plant.getSoilTips().equals("Error") ? 
                plant.getSoil() : plant.getSoilTips());
                
        pruningValue.setText(TextUtils.isEmpty(plant.getPruningTips()) || 
                plant.getPruningTips().equals("Error") ? 
                "No pruning information available" : plant.getPruningTips());
        
        // Set Scientific Info values
        TextView scientificNameValue = findViewById(R.id.scientific_name_value);
        TextView genusValue = findViewById(R.id.genus_value);
        TextView familyValue = findViewById(R.id.family_value);
        TextView additionalInfoValue = findViewById(R.id.additional_info_value);
        
        scientificNameValue.setText(TextUtils.isEmpty(plant.getScientificName()) || 
                plant.getScientificName().equals("Error") ? 
                "Unknown" : plant.getScientificName());
                
        genusValue.setText(TextUtils.isEmpty(plant.getGenus()) || 
                plant.getGenus().equals("Error") ? 
                "Unknown" : plant.getGenus());
                
        familyValue.setText(TextUtils.isEmpty(plant.getFamily()) || 
                plant.getFamily().equals("Error") ? 
                "Unknown" : plant.getFamily());
                
        additionalInfoValue.setText(TextUtils.isEmpty(plant.getAdditionalInfo()) || 
                plant.getAdditionalInfo().equals("Error") ? 
                "No additional information available" : plant.getAdditionalInfo());
    }
}
