package com.example.leafy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Aboutus extends AppCompatActivity {
Button contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        contact = findViewById(R.id.contactUsButton);
        contact.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:leafycare.helpcenter@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "LeafyCare Support Request");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello LeafyCare Team,\n\nI need help with...");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email using..."));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "No email app found on this device.", Toast.LENGTH_SHORT).show();
            }
        });

        // Back Button functionality
        ImageView backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        // Facebook ImageView click listener
        ImageView facebookBtn = findViewById(R.id.facebookImageView);
        facebookBtn.setOnClickListener(v -> openSocialMedia("https://www.facebook.com/ZealInstitutes"));

        // Instagram ImageView click listener
        ImageView instagramBtn = findViewById(R.id.instagramImageView);
        instagramBtn.setOnClickListener(v -> openSocialMedia("https://www.instagram.com/mr.__shubham_06/"));
    }

    private void openSocialMedia(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
