package com.example.leafy;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Homeie extends Fragment {
    private TextView locationTextView, temperatureTextView, greetingTextView;
    private RequestQueue requestQueue;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String API_KEY = "5b4cd0cd738a64d14257b98e5cf61732";
    private CardView c1,c2,c3,c4;
    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homeie, container, false);
        locationTextView = view.findViewById(R.id.location);
        temperatureTextView = view.findViewById(R.id.temperature);
        requestQueue = Volley.newRequestQueue(requireContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        greetingTextView = view.findViewById(R.id.good);
        c1 = view.findViewById(R.id.garden);
        c2 = view.findViewById(R.id.diagnose);
        c3 = view.findViewById(R.id.remainder);
        c4 = view.findViewById(R.id.lightmeter);
        btn = view.findViewById(R.id.addPlantButton);
        btn.setOnClickListener(v->{openFragment(new SearchFragment(),R.id.search);});


        setGreetingMessage();
        getLocation();
        c1.setOnClickListener(v -> openFragment(new GardenFragment(),R.id.my_plants));
        c2.setOnClickListener(v -> openFragment(new Diseases(),R.id.disease));
        c3.setOnClickListener(v -> openRemainderPage());
        c4.setOnClickListener(v -> openLightMeterPage());
        return view;
    }
    private void openFragment(Fragment fragment, int navItemId) {
        // Replacing the current fragment with the new fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null); // Optional
        transaction.commit();

        // Updating the bottom navigation bar selected item
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(navItemId);
    }

    private void openLightMeterPage() {
        Intent intent = new Intent(getActivity(), SunlightActivity.class);
        startActivity(intent);
    }

    private void openRemainderPage() {
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        startActivity(intent);
    }

    private void setGreetingMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        int hour = Integer.parseInt(sdf.format(new Date()));

        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning";
        } else if (hour >= 12 && hour < 18) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Night";
        }
        if (getActivity() != null && isAdded()) {
        if (greetingTextView != null) {
            greetingTextView.setText(greeting);
        } else {
            Log.e("Homeie", "greetingTextView is null");
        }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String cityName = getCityName(latitude, longitude);
                        if (getActivity() != null && isAdded()) {
                            // Safe to interact with the Activity and fragment
                            locationTextView.setText(cityName);
                        } else {
                            Log.e("Homeie", "Fragment is not attached to Activity. Cannot update location.");
                        }
                        getWeatherData(latitude, longitude);
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    private String getCityName(double latitude, double longitude) {
        String cityName = "Unknown City";
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                cityName = addresses.get(0).getLocality();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cityName;
    }
    private boolean showInCelsius = true; // flag to toggle format

    private Handler tempHandler = new Handler();
    private double tempFahrenheit;
    private double tempCelsius;

    private void getWeatherData(double lat, double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=imperial&appid=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            tempFahrenheit = main.getDouble("temp");
                            tempCelsius = (tempFahrenheit - 32) * 5 / 9;

                            startTemperatureLoop(); // Start animated loop
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
    private void startTemperatureLoop() {
        tempHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && isAdded()) {

                    requireActivity().runOnUiThread(() -> {
                    temperatureTextView.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .withEndAction(() -> {
                                String temperatureText;
                                if (showInCelsius) {
                                    temperatureText = String.format("%.1f°C", tempCelsius);
                                } else {
                                    temperatureText = String.format("%.1f°F", tempFahrenheit);
                                }

                                temperatureTextView.setText(temperatureText);
                                temperatureTextView.animate()
                                        .alpha(1f)
                                        .setDuration(500)
                                        .start();

                                showInCelsius = !showInCelsius;

                                // Loop again after delay
                                tempHandler.postDelayed(this, 2000); // Delay between flips
                            }).start();
                });
            }else {
                    Log.e("Homeie", "Fragment is not attached to Activity. Cannot update temperature.");
                }
            }
        }, 0); // Start immediately
    }

}
