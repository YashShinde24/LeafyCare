package com.example.leafy;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SunlightActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView lightLevel;
    private ProgressBar progressBar;
    private Vibrator vibrator;
    private boolean isMeasuring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunlight);

        lightLevel = findViewById(R.id.lightLevel);
        progressBar = findViewById(R.id.progressBar);
        Button startMeasurement = findViewById(R.id.startMeasurement);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        startMeasurement.setOnClickListener(v -> {
            isMeasuring = !isMeasuring;
            if (isMeasuring) {
                if (lightSensor != null) {
                    sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
                    Log.d("LightSensor", "Sensor registered successfully!");  // <-- Debugging
                    startMeasurement.setText("Stop Measurement");
                } else {
                    Log.e("LightSensor", "Light Sensor not available!");  // <-- Debugging
                    Toast.makeText(this, "Light Sensor not available on this device!", Toast.LENGTH_LONG).show();
                }
            } else {
                sensorManager.unregisterListener(this);
                Log.d("LightSensor", "Sensor unregistered.");
                startMeasurement.setText("Start Measurement");
            }
        });

    }

    @Override

    public void onSensorChanged(SensorEvent event) {
        if (isMeasuring) {
            float lux = event.values[0];
            Log.d("LightSensor", "Light Level: " + lux);  // <-- ADD THIS
            lightLevel.setText(lux + " LUX");
            progressBar.setProgress((int) Math.min(lux, 10000));

            // Vibrate if light is between 2000 - 5000 (ideal for plants)
            if (lux >= 2000 && lux <= 5000) {
                vibrator.vibrate(300);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
