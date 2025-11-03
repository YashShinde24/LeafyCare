package com.example.leafy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GardenFragment extends Fragment implements GardenPlantAdapter.OnPlantActionListener {

    private GardenViewModel gardenViewModel;
    private GardenPlantAdapter adapter;
    private TextView emptyGardenText;
    private ReminderManager reminderManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garden, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.garden_recycler_view);
        emptyGardenText = view.findViewById(R.id.empty_garden_text);

        reminderManager = new ReminderManager(requireContext());
        adapter = new GardenPlantAdapter(requireContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gardenViewModel = new ViewModelProvider(this).get(GardenViewModel.class);
        gardenViewModel.getAllGardenPlants().observe(getViewLifecycleOwner(), gardenPlants -> {
            adapter.setPlants(gardenPlants);

            // Show empty text if no plants
            if (gardenPlants.isEmpty()) {
                emptyGardenText.setVisibility(View.VISIBLE);
            } else {
                emptyGardenText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onPlantRemove(GardenPlant plant) {
        // If plant has a reminder, cancel it first
        if (plant.hasReminder()) {
            reminderManager.cancelReminder(plant);
        }

        gardenViewModel.removePlantFromGarden(plant);
        Toast.makeText(requireContext(),
                getString(R.string.plant_removed_from_garden, plant.getName()),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReminderSet(GardenPlant plant, int days) {
        // Cancel any existing reminder
        if (plant.hasReminder()) {
            reminderManager.cancelReminder(plant);
        }

        // Schedule the new reminder
        long reminderDate = reminderManager.scheduleReminder(plant, days);

        // Update the plant in the database
        plant.setHasReminder(true);
        plant.setReminderDate(reminderDate);
        plant.setReminderDays(days);
        gardenViewModel.addPlantToGarden(plant);

        Toast.makeText(requireContext(),
                getString(R.string.reminder_set_success, plant.getName(), days),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReminderRemove(GardenPlant plant) {
        if (plant.hasReminder()) {
            // Cancel the reminder
            reminderManager.cancelReminder(plant);

            // Update the plant in the database
            plant.setHasReminder(false);
            plant.setReminderDate(0);
            plant.setReminderDays(0);
            gardenViewModel.addPlantToGarden(plant);

            Toast.makeText(requireContext(),
                    getString(R.string.reminder_removed, plant.getName()),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
