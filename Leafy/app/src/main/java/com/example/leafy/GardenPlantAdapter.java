package com.example.leafy;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GardenPlantAdapter extends RecyclerView.Adapter<GardenPlantAdapter.GardenPlantViewHolder> {

    private List<GardenPlant> plants = new ArrayList<>();
    private final Context context;
    private final OnPlantActionListener actionListener;
    private final ReminderManager reminderManager;
    private final SimpleDateFormat dateFormat;

    public interface OnPlantActionListener {
        void onPlantRemove(GardenPlant plant);
        void onReminderSet(GardenPlant plant, int days);
        void onReminderRemove(GardenPlant plant);
    }

    public GardenPlantAdapter(Context context, OnPlantActionListener actionListener) {
        this.context = context;
        this.actionListener = actionListener;
        this.reminderManager = new ReminderManager(context);
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public GardenPlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_garden_plant, parent, false);
        return new GardenPlantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GardenPlantViewHolder holder, int position) {
        GardenPlant currentPlant = plants.get(position);

        holder.plantNameTextView.setText(currentPlant.getName());
        holder.sunlightTextView.setText(currentPlant.getSunlight());
        holder.wateringTextView.setText(String.format("%d days", currentPlant.getWateringDays()));

        // Load image
        Glide.with(context)
                .load(currentPlant.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.plantImageView);

        // Set up reminder status
        if (currentPlant.hasReminder()) {
            holder.reminderStatusText.setVisibility(View.VISIBLE);
            String reminderDate = dateFormat.format(new Date(currentPlant.getReminderDate()));
            holder.reminderStatusText.setText(context.getString(R.string.reminder_set_for, reminderDate));
        } else {
            holder.reminderStatusText.setVisibility(View.GONE);
        }

        // Set up remove button
        holder.removeButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onPlantRemove(currentPlant);
            }
        });

        // Set up reminder button
        holder.setReminderButton.setOnClickListener(v -> showReminderDialog(currentPlant));
    }

    private void showReminderDialog(GardenPlant plant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_set_reminder, null);
        builder.setView(dialogView);

        // Initialize dialog views
        TextView recommendedDaysText = dialogView.findViewById(R.id.recommended_days_text);
        SeekBar daysSeekBar = dialogView.findViewById(R.id.days_seekbar);
        TextView selectedDaysText = dialogView.findViewById(R.id.selected_days_text);
        Button removeReminderButton = dialogView.findViewById(R.id.remove_reminder_button);
        Button setReminderButton = dialogView.findViewById(R.id.set_reminder_button);

        // Set recommended days text
        recommendedDaysText.setText(context.getString(R.string.recommended_days, plant.getWateringDays()));

        // Set initial seekbar value
        int initialDays = plant.hasReminder() ? plant.getReminderDays() : plant.getWateringDays();
        initialDays = Math.min(Math.max(initialDays, 1), 14); // Ensure between 1-14
        daysSeekBar.setProgress(initialDays - 1); // SeekBar is 0-based
        selectedDaysText.setText(context.getString(R.string.days_value, initialDays));

        // Set up seekbar listener
        daysSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int days = progress + 1; // Convert to 1-14 range
                selectedDaysText.setText(context.getString(R.string.days_value, days));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set up button listeners
        removeReminderButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onReminderRemove(plant);
            }
            dialog.dismiss();
        });

        setReminderButton.setOnClickListener(v -> {
            int days = daysSeekBar.getProgress() + 1; // Convert to 1-14 range
            if (actionListener != null) {
                actionListener.onReminderSet(plant, days);
            }
            dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void setPlants(List<GardenPlant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }

    static class GardenPlantViewHolder extends RecyclerView.ViewHolder {
        private final TextView plantNameTextView;
        private final TextView sunlightTextView;
        private final TextView wateringTextView;
        private final TextView reminderStatusText;
        private final ImageView plantImageView;
        private final Button removeButton;
        private final Button setReminderButton;

        public GardenPlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantNameTextView = itemView.findViewById(R.id.garden_plant_name);
            sunlightTextView = itemView.findViewById(R.id.garden_plant_sunlight);
            wateringTextView = itemView.findViewById(R.id.garden_plant_watering);
            reminderStatusText = itemView.findViewById(R.id.reminder_status_text);
            plantImageView = itemView.findViewById(R.id.garden_plant_image);
            removeButton = itemView.findViewById(R.id.remove_plant_button);
            setReminderButton = itemView.findViewById(R.id.set_reminder_button);
        }
    }
}
