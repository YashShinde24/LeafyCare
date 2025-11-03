package com.example.leafy;

import static java.security.AccessController.getContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class SettingActivity extends AppCompatActivity {
    Button editProfileButton;
    ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting); // Ensure this matches your XML file name

        LinearLayout contactSupport = findViewById(R.id.contactSupport);

        ImageButton backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        LinearLayout termsTextView = findViewById(R.id.termsConditions); // The clickable text
        termsTextView.setOnClickListener(v -> {
            showInfoDialog("Terms and Conditions",
                    "Welcome to LeafyCare!\n\n" +
                            "1. Acceptance of Terms:\n" +
                            "By using LeafyCare, you agree to these Terms. If you don’t agree, please do not use the app.\n\n" +
                            "2. Use of the App:\n" +
                            "- You must be at least 13 years old.\n" +
                            "- Don’t misuse the app (e.g., no spam, viruses, or illegal activity).\n" +
                            "- You are responsible for your account activity.\n\n" +
                            "3. User Data:\n" +
                            "You agree to provide accurate info and keep it updated. Your data helps personalize your experience.\n\n" +
                            "4. Intellectual Property:\n" +
                            "All content in LeafyCare is owned by us or licensed to us. Don’t copy or use it without permission.\n\n" +
                            "5. Limitation of Liability:\n" +
                            "We try our best, but we’re not liable for any damage or issues caused by using the app.\n\n" +
                            "6. Changes to Terms:\n" +
                            "We may update these Terms from time to time. You will be notified of important changes in the app.\n\n" +
                            "7. Termination:\n" +
                            "We can suspend or remove your account for violating our terms or misuse.\n\n" +
                            "Thanks for using LeafyCare! 🌿"
            );
        });
        LinearLayout l =findViewById(R.id.privacy);
        l.setOnClickListener(v-> {
            showInfoDialog("Privacy Policy",
                    "We value your privacy. This policy explains how we collect, use, and protect your data.\n\n" +
                            "1. Information We Collect:\n" +
                            "- Personal Info: Email, username, profile photo (if used).\n" +
                            "- Plant Data: Plants you add, reminders you set.\n" +
                            "- Device Info: Android version, app usage, crash logs.\n" +
                            "- Camera/Sensor: Only used when you allow features like plant detection or light sensing.\n\n" +
                            "2. How We Use Your Info:\n" +
                            "- To personalize your experience.\n" +
                            "- To send care reminders.\n" +
                            "- To fix bugs and improve features.\n" +
                            "- To send important updates.\n\n" +
                            "3. Data Security:\n" +
                            "- We store your data securely using Firebase.\n" +
                            "- Your account and data are protected by encryption and secure login.\n\n" +
                            "4. Data Sharing:\n" +
                            "- We don’t sell your data.\n" +
                            "- Limited info may be shared with Firebase or similar services to operate the app.\n" +
                            "- If required by law, data may be shared with authorities.\n\n" +
                            "5. Permissions:\n" +
                            "- Camera: For plant recognition and sunlight detection.\n" +
                            "- Storage: To save plant images and notes.\n" +
                            "- Notifications: To alert you about your plant care schedule.\n\n" +
                            "6. Children’s Privacy:\n" +
                            "This app is not meant for children under 13. We do not knowingly collect data from minors.\n\n" +
                            "7. Updates:\n" +
                            "If we make any big changes to this policy, we’ll inform you in the app.\n\n" +
                            "Thank you for trusting LeafyCare 🌿"
            );

        });
        LinearLayout shareLayout = findViewById(R.id.share);

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Check out the LeafyCare app for plant lovers 🌿: https://play.google.com/store/apps/details?id=your.package.name";
                intent.putExtra(Intent.EXTRA_SUBJECT, "LeafyCare App");
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });


        Button deleteAccountButton = findViewById(R.id.deleteAccountButton);

        backButton.setOnClickListener(v -> {
            onBackPressed(); // Navigates back to the previous activity
        });

        deleteAccountButton.setOnClickListener(v -> showDeleteConfirmationDialog());

        editProfileButton =findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(v -> editprofile());

        contactSupport.setOnClickListener(v -> {
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

    }

    private void showInfoDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.terms_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView termsTitle = dialogView.findViewById(R.id.termsTitle);
        TextView termsContent = dialogView.findViewById(R.id.termsContent);
        Button btnClose = dialogView.findViewById(R.id.btnClose);

        termsTitle.setText(title);          // Set dialog title
        termsContent.setText(content);      // Set dialog content

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    private void editprofile() {
        Intent intent = new Intent(SettingActivity.this,Profile.class);
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Data?")
                .setMessage("This will erase all app data and reset it. You will need to log in again.")
                .setPositiveButton("Delete", (dialog, which) -> clearAppStorage())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearAppStorage() {
        try {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activityManager.clearApplicationUserData();
            } else {
                Toast.makeText(this, "Manual data deletion required for this device", Toast.LENGTH_SHORT).show();
            }

            // Restart app after deletion
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error clearing app data", Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap getImageFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("LeafyPrefs", Context.MODE_PRIVATE);
        String imageBase64 = prefs.getString("profile_image", null);

        if (imageBase64 != null) {
            byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bitmap savedBitmap = getImageFromPrefs(getApplicationContext());
        if (savedBitmap != null) {
            profileImage.setPadding(0, 0, 0, 0);
            profileImage.setImageBitmap(savedBitmap);
        }
    }
}
