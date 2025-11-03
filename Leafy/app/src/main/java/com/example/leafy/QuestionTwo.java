package com.example.leafy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class QuestionTwo extends AppCompatActivity {

    ImageButton backButton;
    CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5;
    MaterialButton skipButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_question);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);
        checkbox3 = findViewById(R.id.checkbox3);
        checkbox4 = findViewById(R.id.checkbox4);
        checkbox5 = findViewById(R.id.checkbox5);
        skipButton = findViewById(R.id.skipButton);
        nextButton = findViewById(R.id.nextButton);

        // Back button logic
        backButton.setOnClickListener(v -> onBackPressed());

        // Skip button logic
        skipButton.setOnClickListener(v -> {
            Toast.makeText(this, "Skipped", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, QuestionThree.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Checkbox listener for dynamic color change
        CompoundButton.OnCheckedChangeListener checkboxListener = (buttonView, isChecked) -> updateNextButtonColor();

        checkbox1.setOnCheckedChangeListener(checkboxListener);
        checkbox2.setOnCheckedChangeListener(checkboxListener);
        checkbox3.setOnCheckedChangeListener(checkboxListener);
        checkbox4.setOnCheckedChangeListener(checkboxListener);
        checkbox5.setOnCheckedChangeListener(checkboxListener);

        // Next button logic
        nextButton.setOnClickListener(v -> {
            StringBuilder selectedOptions = new StringBuilder();

            if (checkbox1.isChecked()) selectedOptions.append("Sick Plant Help\n");
            if (checkbox2.isChecked()) selectedOptions.append("Water & Care Reminders\n");
            if (checkbox3.isChecked()) selectedOptions.append("Plant Identification\n");
            if (checkbox4.isChecked()) selectedOptions.append("Plant Journal\n");
            if (checkbox5.isChecked()) selectedOptions.append("Something Else\n");

            if (selectedOptions.length() == 0) {
                Toast.makeText(this, "Please select at least one option", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, QuestionThree.class);
                intent.putExtra("selectedOptions", selectedOptions.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // Initialize button color
        updateNextButtonColor();
    }

    // Method to update Next button color
    private void updateNextButtonColor() {
        if (checkbox1.isChecked() || checkbox2.isChecked() || checkbox3.isChecked()
                || checkbox4.isChecked() || checkbox5.isChecked()) {
            nextButton.setBackgroundColor(getResources().getColor(R.color.primary));
        } else {
            nextButton.setBackgroundColor(getResources().getColor(R.color.btn_unselect));
        }
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
