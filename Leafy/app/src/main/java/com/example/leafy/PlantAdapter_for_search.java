package com.example.leafy;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlantAdapter_for_search extends RecyclerView.Adapter<PlantAdapter_for_search.PlantViewHolder> {

    private final List<Search_Plant> plants;
    private final Context context;

    public PlantAdapter_for_search(List<Search_Plant> plants, Context context) {
        this.plants = plants;
        this.context = context;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Search_Plant plant = plants.get(position);
        holder.plantNameTextView.setText(plant.getName());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlantDetailActivity.class);
            intent.putExtra(PlantDetailActivity.EXTRA_PLANT_NAME, plant.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView plantNameTextView;

        PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            plantNameTextView = itemView.findViewById(R.id.plant_name_text_view);
        }
    }
}
