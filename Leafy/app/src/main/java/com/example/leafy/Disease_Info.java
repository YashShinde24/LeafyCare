package com.example.leafy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Disease_Info extends AppCompatActivity {

    String diseaseName;
    String detailedSignal = "";
    String detailedCause = "";
    String detailedCure = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_info);

        // Get the disease name from the intent
        diseaseName = getIntent().getStringExtra("disease_name");
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());


        // Initialize UI elements
        ImageView image1 = findViewById(R.id.first);
        ImageView image2 = findViewById(R.id.second);
        ImageView image3 = findViewById(R.id.third);
        TextView diseaseNameText = findViewById(R.id.diagnosisName);
        TextView commonName = findViewById(R.id.commonName);
        TextView scientificName = findViewById(R.id.scientificName);
        TextView signalDescription = findViewById(R.id.signalDesc);
        TextView causeDescription = findViewById(R.id.causeDesc);
        TextView cureDescription = findViewById(R.id.cureDesc);

        CardView cardSignal = findViewById(R.id.card_signal);
        CardView cardCause = findViewById(R.id.card_cause);
        CardView cardCure = findViewById(R.id.card_cure);

        // Manually set content based on the detected disease
        if (diseaseName != null) {
            switch (diseaseName) {
                case "Rust":
                    image1.setImageResource(R.drawable.rust_1);
                    image2.setImageResource(R.drawable.rust_2);
                    image3.setImageResource(R.drawable.rust_3);
                    diseaseNameText.setText("Diagnosis Name: Rust");
                    commonName.setText("Common Name: Rust");
                    scientificName.setText("Scientific Name: Puccinia spp.");
                    signalDescription.setText("Yellow or orange pustules on the leaves.");
                    causeDescription.setText("Caused by fungal spores that thrive in wet conditions.");
                    cureDescription.setText("Remove affected leaves and apply fungicide.");
                    detailedSignal = "Rust is identified by yellow-orange pustules that appear on the undersides of leaves. These pustules contain spores that can spread to other plants.";
                    detailedCause = "Rust is caused by fungal spores, typically Puccinia species, which thrive in humid environments and spread via wind and water.";
                    detailedCure = "To treat rust, remove infected leaves and dispose of them. Apply a sulfur-based fungicide and ensure good air circulation.";

                    break;

                case "Powdery":
                    image1.setImageResource(R.drawable.powdery_1);
                    image2.setImageResource(R.drawable.powdery_2);
                    image3.setImageResource(R.drawable.powdery_3);
                    diseaseNameText.setText("Diagnosis Name: Powdery Mildew");
                    commonName.setText("Common Name: White Mold");
                    scientificName.setText("Scientific Name: Erysiphales");
                    signalDescription.setText("White, powdery spots on leaves and stems.");
                    causeDescription.setText("Fungal spores that spread in dry and humid weather.");
                    cureDescription.setText("Increase air circulation, apply neem oil.");
                    detailedSignal = "Powdery mildew appears as white, powdery spots on leaves, stems, and flowers. It may cause distortion and yellowing of leaves.";
                    detailedCause = "This fungal disease thrives in dry days and humid nights. Poor air circulation and crowded plantings increase susceptibility.";
                    detailedCure = "Apply neem oil or potassium bicarbonate. Remove affected parts and avoid overhead watering.";

                    break;

                case "Mealybugs":
                    image1.setImageResource(R.drawable.mealy_1);
                    image2.setImageResource(R.drawable.mealy_2);
                    image3.setImageResource(R.drawable.mealy_3);
                    diseaseNameText.setText("Diagnosis Name: Mealybugs Infestation");
                    commonName.setText("Common Name: Mealybugs");
                    scientificName.setText("Scientific Name: Pseudococcidae");
                    signalDescription.setText("White cotton-like clumps on plant parts.");
                    causeDescription.setText("Sap-sucking insects found in moist, warm areas.");
                    cureDescription.setText("Use rubbing alcohol or insecticidal soap.");
                    detailedSignal = "Mealybugs show as white, cotton-like clusters mainly on leaves and nodes. They secrete sticky honeydew that attracts ants.";
                    detailedCause = "They infest when humidity is high and plants are stressed. These pests suck sap, weakening the plant.";
                    detailedCure = "Use rubbing alcohol on a cotton swab or insecticidal soap. Regular checks and isolation help prevent spread.";

                    break;

                case "Black Spot":
                    image1.setImageResource(R.drawable.blackspot);
                    image2.setImageResource(R.drawable.black_2);
                    image3.setImageResource(R.drawable.black_3);
                    diseaseNameText.setText("Diagnosis Name: Black Spot Disease");
                    commonName.setText("Common Name: Leaf Spot");
                    scientificName.setText("Scientific Name: Diplocarpon rosae");
                    signalDescription.setText("Black round spots on leaves.");
                    causeDescription.setText("Fungal spores spread by water splash.");
                    cureDescription.setText("Prune affected leaves, apply fungicide.");
                    detailedSignal = "Black spots with fringed edges form on leaf surfaces. Leaves may yellow and fall prematurely.";
                    detailedCause = "This is caused by the fungus Diplocarpon rosae and spreads through water splashing during rains or watering.";
                    detailedCure = "Remove affected leaves, use a fungicide containing chlorothalonil or neem oil, and water from the base.";

                    break;

                case "Healthy":
                    image1.setImageResource(R.drawable.healthy_1);
                    image2.setImageResource(R.drawable.healthy_2);
                    image3.setImageResource(R.drawable.healthy_3);
                    diseaseNameText.setText("Healthy Plant");
                    commonName.setText("-");
                    scientificName.setText("-");
                    signalDescription.setText("No signs of disease.");
                    causeDescription.setText("Good care and environmental conditions.");
                    cureDescription.setText("Keep maintaining proper care.");
                    detailedSignal = "Leaves are green, glossy, and free of any abnormalities. Growth is consistent and strong.";
                    detailedCause = "Proper care, balanced nutrients, and good environmental conditions maintain plant health.";
                    detailedCure = "No cure needed. Continue regular monitoring and care routines.";

                    break;

                default:
                    Toast.makeText(this, "Unknown disease detected", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        cardSignal.setOnClickListener(v -> {
            showBottomSheet("Signal", detailedSignal);
        });

        cardCause.setOnClickListener(v -> {
            showBottomSheet("Cause", detailedCause);
        });

        cardCure.setOnClickListener(v -> {
            showBottomSheet("Cure", detailedCure);
        });

    }
    private void showBottomSheet(String title, String detail) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        TextView titleView = bottomSheetView.findViewById(R.id.bottomSheetTitle);
        TextView descriptionView = bottomSheetView.findViewById(R.id.bottomSheetDesc);

        titleView.setText(title);
        descriptionView.setText(detail);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }



}
