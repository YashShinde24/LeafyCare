package com.example.leafy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Settings extends Fragment {

    private ImageView profileImage, settingsIcon;
    private TextView title;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize UI elements
        profileImage = view.findViewById(R.id.profile_icon);
        settingsIcon = view.findViewById(R.id.settings_icon);
        title = view.findViewById(R.id.title);

        // Set click listener for settings icon
        settingsIcon.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SettingActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load profile image from SharedPreferences
        Bitmap savedBitmap = getImageFromPrefs(requireContext());
        if (savedBitmap != null) {
            profileImage.setImageBitmap(savedBitmap);
        }

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String fullName = sharedPreferences.getString("name", "User");

        String firstName = fullName.split(" ")[0];

        title.setText("Hello, " + firstName);

    }

    // Utility method to get profile image from shared preferences
    private Bitmap getImageFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("LeafyPrefs", Context.MODE_PRIVATE);
        String imageBase64 = prefs.getString("profile_image", null);

        if (imageBase64 != null) {
            byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return null;
    }
}
