package com.example.leafy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    
    private PlantRepository plantRepository;
    private List<Plant> allPlants;
    private List<Plant> filteredPlants;
    private PlantAdapter plantAdapter;
    private EditText searchEditText;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        
        // Initialize views
        searchEditText = view.findViewById(R.id.search_edit_text);
        RecyclerView recyclerView = view.findViewById(R.id.plants_recycler_view);
        
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        // Load plant data
        plantRepository = new PlantRepository(requireContext());
        allPlants = plantRepository.loadPlants();
        
        if (allPlants.isEmpty()) {
            Toast.makeText(requireContext(), "No plants found in the CSV file", Toast.LENGTH_LONG).show();
        }
        
        filteredPlants = new ArrayList<>(allPlants);
        
        // Set up adapter
        plantAdapter = new PlantAdapter(filteredPlants, requireContext());
        recyclerView.setAdapter(plantAdapter);
        
        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter plants based on search query
                filterPlants(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
        
        return view;
    }
    
    private void filterPlants(String query) {
        filteredPlants.clear();
        
        if (query.isEmpty()) {
            filteredPlants.addAll(allPlants);
        } else {
            for (Plant plant : allPlants) {
                if (plant.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredPlants.add(plant);
                }
            }
        }
        
        plantAdapter.notifyDataSetChanged();
    }
}
