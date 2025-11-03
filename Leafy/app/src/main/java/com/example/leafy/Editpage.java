package com.example.leafy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Editpage extends AppCompatActivity {

    private EditText inputField;
    private Button saveButton;
    private TextView titleText;

    private String fieldKey;
    private String fieldTitle;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpage);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        inputField = findViewById(R.id.display_name_input);
        saveButton = findViewById(R.id.save_button);
        titleText = findViewById(R.id.textTitle); // Add this TextView in XML to show the title

        // Get data from Intent
        Intent intent = getIntent();
        fieldKey = intent.getStringExtra("field"); // "display_name", "email", or "phone"
        fieldTitle = intent.getStringExtra("title"); // "Edit Display Name", etc.

        // Set title
        titleText.setText(fieldTitle);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Load existing value (if any)
        String currentValue = sharedPreferences.getString(fieldKey, "");
        inputField.setText(currentValue);

        // Save button logic
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedValue = inputField.getText().toString().trim();

                if (updatedValue.isEmpty()) {
                    Toast.makeText(Editpage.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save updated value to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(fieldKey, updatedValue);
                editor.apply();

                Toast.makeText(Editpage.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                // Close the activity
                finish();
            }
        });
    }
}
