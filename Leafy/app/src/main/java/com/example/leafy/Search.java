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


public class Search extends Fragment {

    private PlantRepository plantRepository;
    private List<Search_Plant> allPlants;
    private List<Search_Plant> filteredPlants;
    private PlantAdapter_for_search plantAdapter;
    private EditText searchEditText;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_page, container, false);

        // Initialize views
        searchEditText = view.findViewById(R.id.search_edit_text);
        recyclerView = view.findViewById(R.id.plants_recycler_view);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load plant data
        plantRepository = new PlantRepository(getContext());
        List<Plant> rawPlants = plantRepository.loadPlants();
        allPlants = new ArrayList<>();

        for (Plant plant : rawPlants) {
            Search_Plant searchPlant = new Search_Plant(
                    plant.getName(),
                    plant.getGrowth(),
                    plant.getSoil(),
                    plant.getSunlight(),
                    plant.getWatering(),
                    plant.getFertilizingType(),
                    plant.getImageURL()
            );
            allPlants.add(searchPlant);
        }

        if (allPlants.isEmpty()) {
            Toast.makeText(getContext(), "No plants found in the CSV file", Toast.LENGTH_LONG).show();
        }

        filteredPlants = new ArrayList<>(allPlants);

        // Setup adapter
        plantAdapter = new PlantAdapter_for_search(filteredPlants, getContext());
        recyclerView.setAdapter(plantAdapter);

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPlants(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void filterPlants(String query) {
        filteredPlants.clear();

        if (query.isEmpty()) {
            filteredPlants.addAll(allPlants);
        } else {
            for (Search_Plant plant : allPlants) {
                if (plant.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredPlants.add(plant);
                }
            }
        }

        plantAdapter.notifyDataSetChanged();
    }
}
