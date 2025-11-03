package com.example.leafy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

public class QuestionOne extends AppCompatActivity {

    private ImageButton backButton;
    private MaterialCheckBox indoorCheckbox, outdoorCheckbox, gardenCheckbox;
    private MaterialButton skipButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_question); // make sure the XML is named correctly

        // Bind views
        backButton = findViewById(R.id.backButton);
        indoorCheckbox = findViewById(R.id.indoorCheckbox);
        outdoorCheckbox = findViewById(R.id.outdoorCheckbox);
        gardenCheckbox = findViewById(R.id.gardenCheckbox);
        skipButton = findViewById(R.id.skipButton);
        nextButton = findViewById(R.id.nextButton);

        // Back button
        backButton.setOnClickListener(v -> onBackPressed());

        // Skip button
        skipButton.setOnClickListener(v -> {
            Toast.makeText(this, "Skipped", Toast.LENGTH_SHORT).show();
            goToNextActivity();
        });

        // Checkbox change listener to update button color
        CompoundButton.OnCheckedChangeListener checkboxListener = (buttonView, isChecked) -> updateNextButtonColor();

        indoorCheckbox.setOnCheckedChangeListener(checkboxListener);
        outdoorCheckbox.setOnCheckedChangeListener(checkboxListener);
        gardenCheckbox.setOnCheckedChangeListener(checkboxListener);

        // Next button
        nextButton.setOnClickListener(v -> {
            StringBuilder selected = new StringBuilder("Selected: ");
            if (indoorCheckbox.isChecked()) selected.append("Indoor ");
            if (outdoorCheckbox.isChecked()) selected.append("Outdoor ");
            if (gardenCheckbox.isChecked()) selected.append("Garden ");

            if (!indoorCheckbox.isChecked() && !outdoorCheckbox.isChecked() && !gardenCheckbox.isChecked()) {
                Toast.makeText(this, "Please select at least one option", Toast.LENGTH_SHORT).show();
            } else {
                goToNextActivity();
            }
        });

        // Set initial color
        updateNextButtonColor();
    }

    // Change button color based on selection
    private void updateNextButtonColor() {
        if (indoorCheckbox.isChecked() || outdoorCheckbox.isChecked() || gardenCheckbox.isChecked()) {
            nextButton.setBackgroundColor(getResources().getColor(R.color.primary));
        } else {
            nextButton.setBackgroundColor(getResources().getColor(R.color.btn_unselect));
        }
    }

    // Method to go to the next activity
    private void goToNextActivity() {
        Intent intent = new Intent(this, QuestionTwo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Change navigation bar color again when resuming the activity
        setNavigationBarColor("#D9E6D0");
    }

    private void setNavigationBarColor(String colorHex) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setNavigationBarColor(Color.parseColor(colorHex)); // Set color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }
    }
}
