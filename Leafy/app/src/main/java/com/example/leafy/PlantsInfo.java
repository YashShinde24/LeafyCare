package com.example.leafy;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class PlantsInfo extends AppCompatActivity {
    private Button btn, saveButton;
    private ImageButton ibtn;
    private TextView showtext, desc, hei, wa, sunn, humi;

    private static final HashMap<Integer, PlantDetails> PLANT_DATA = new HashMap<>();

    static {
        PLANT_DATA.put(R.drawable.jasmin, new PlantDetails("Jasmine", "A fragrant flowering plant used in perfumes and teas. Read more!", "2-3m", "Moderate", "Full/Partial Sun", "Moderate"));
        PLANT_DATA.put(R.drawable.money, new PlantDetails("Money Plant", "A popular indoor plant believed to bring good luck and prosperity. Read more!", "Up to 3m", "Low", "Indirect Sunlight", "Moderate"));
        PLANT_DATA.put(R.drawable.sunflower, new PlantDetails("Sunflower", "A tall plant with bright yellow flowers that follow the sun. Read more!", "1.5-3.5m", "High", "Full Sun", "Frequent"));
        PLANT_DATA.put(R.drawable.tamto, new PlantDetails("Tomato", "A commonly grown fruiting plant used in various dishes. Read more!", "1-2m", "High", "Full Sun", "Frequent"));
        PLANT_DATA.put(R.drawable.tulsi, new PlantDetails("Tulsi", "A sacred and medicinal plant used in Ayurveda. Read more!", "30-60cm", "Moderate", "Full Sun", "Regular"));
        PLANT_DATA.put(R.drawable.gulab, new PlantDetails("Rose", "A beautiful flowering plant with a pleasant fragrance. Read more!", "1-2m", "Moderate", "Full Sun", "Regular"));
        PLANT_DATA.put(R.drawable.jasmin1, new PlantDetails("Jasmine (Variety 1)", "A fragrant flowering plant used in aromatherapy and decoration. Read more!", "2-3m", "Moderate", "Full/Partial Sun", "Moderate"));
        PLANT_DATA.put(R.drawable.shev, new PlantDetails("Chrysanthemum", "A vibrant flower widely used in decorations and floral arrangements. Read more!", "30-90cm", "Moderate", "Full Sun", "Regular"));
        PLANT_DATA.put(R.drawable.shevnti, new PlantDetails("Chrysanthemum (Shevanti)", "A variety of Chrysanthemum known for its beautiful blooms. Read more!", "30-90cm", "Moderate", "Full Sun", "Regular"));
        PLANT_DATA.put(R.drawable.kadipatta, new PlantDetails("Curry Leaves", "A flavorful herb used in Indian cuisine. Read more!", "2-4m", "Low", "Full Sun", "Moderate"));
        PLANT_DATA.put(R.drawable.jasvand, new PlantDetails("Hibiscus", "A tropical plant with large, colorful flowers. Read more!", "1-3m", "High", "Full Sun", "Regular"));
        PLANT_DATA.put(R.drawable.kan, new PlantDetails("Kaner (Oleander)", "A flowering shrub known for its bright and toxic blooms. Read more!", "2-4m", "Low", "Full Sun", "Low"));
        PLANT_DATA.put(R.drawable.parijat, new PlantDetails("Parijat (Night-flowering Jasmine)", "A sacred plant known for its fragrant flowers. Read more!", "3-5m", "Moderate", "Partial Sun", "Moderate"));
        PLANT_DATA.put(R.drawable.sadaful, new PlantDetails("Periwinkle", "A hardy flowering plant with medicinal properties. Read more!", "30-60cm", "Low", "Full Sun", "Low"));
        PLANT_DATA.put(R.drawable.mali, new PlantDetails("Gardenia", "A fragrant white flowering plant used in gardens and decorations. Read more!", "1-2m", "Moderate", "Partial Sun", "Moderate"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextpageforplant);

        btn = findViewById(R.id.clock);
        saveButton = findViewById(R.id.save);
        ibtn = findViewById(R.id.ibtn);
        ImageView imageView = findViewById(R.id.imageView);
        showtext = findViewById(R.id.showtext);
        desc = findViewById(R.id.textarea);
        hei = findViewById(R.id.hei);
        wa = findViewById(R.id.wa);
        sunn = findViewById(R.id.sunn);
        humi = findViewById(R.id.humi);

        int imageRes = getIntent().getIntExtra("image_res", -1);
        PlantDetails plant = PLANT_DATA.get(imageRes);
        if (plant != null) {
            imageView.setImageResource(imageRes);
            showtext.setText(plant.name);
            desc.setText(plant.description);
            hei.setText(plant.height);
            wa.setText(plant.watering);
            sunn.setText(plant.sunlight);
            humi.setText(plant.humidity);
        } else {
            Toast.makeText(this, "Plant data not found!", Toast.LENGTH_SHORT).show();
        }

        btn.setOnClickListener(v -> showTimePicker(imageRes));
        saveButton.setOnClickListener(v -> saveImage(imageRes));
        ibtn.setOnClickListener(v -> startActivity(new Intent(PlantsInfo.this, Search.class)));

        desc.setOnClickListener(v -> {
            String searchUrl = "https://www.google.com/search?q=" + showtext.getText().toString().trim();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl)));
        });

        // Request notification permission (for Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission();
        }
    }

    private void saveImage(int imageResId) {
        SharedPreferences prefs = getSharedPreferences("SavedImages", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = prefs.getString("imageList", "[]");
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        ArrayList<Integer> savedImages = gson.fromJson(json, type);

        if (!savedImages.contains(imageResId)) {
            savedImages.add(imageResId);
            editor.putString("imageList", gson.toJson(savedImages));
            editor.apply();
            Toast.makeText(this, "Plant saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plant is already saved.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showTimePicker(int imageRes) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.timepicker);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button tbtn = dialog.findViewById(R.id.btnSetTime);
        TimePicker timePicker = dialog.findViewById(R.id.time);

        tbtn.setOnClickListener(v -> {
            int selectedHour = timePicker.getHour();
            int selectedMinute = timePicker.getMinute();

            scheduleNotification(selectedHour, selectedMinute, imageRes);
            dialog.dismiss();
        });
    }

    private void scheduleNotification(int hour, int minute, int imageRes) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Toast.makeText(this, "AlarmManager not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(this, "Please allow exact alarms in settings", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        int requestCode = hour * 100 + minute;
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("image_res", imageRes);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Notification set for " + formattedTime(hour, minute), Toast.LENGTH_SHORT).show();
    }

    private String formattedTime(int hour, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d %s",
                (hour % 12 == 0) ? 12 : hour % 12, minute, (hour >= 12) ? "PM" : "AM");
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
    }

    static class PlantDetails {
        String name, description, height, watering, sunlight, humidity;

        PlantDetails(String name, String description, String height, String watering, String sunlight, String humidity) {
            this.name = name;
            this.description = description;
            this.height = height;
            this.watering = watering;
            this.sunlight = sunlight;
            this.humidity = humidity;
        }
    }
}
