package com.example.leafy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Context context;
    ArrayList<ImageModel> arrayimage;
    Fragment fragment; // Add a Fragment reference

    public ImageAdapter(Context context, ArrayList<ImageModel> arrayimage, Fragment fragment) {
        this.context = context;
        this.arrayimage = arrayimage;
        this.fragment = fragment; // Initialize the Fragment reference
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.imagemodel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageResource(arrayimage.get(position).img);

        holder.rli.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }

            Log.d("ImageAdapter", "Item clicked at position: " + adapterPosition);

            if (fragment.isAdded() && fragment.getActivity() != null) {
                Context activityContext = fragment.getActivity();
                Intent intent = new Intent(activityContext, PlantsInfo.class);
                intent.putExtra("image_res", arrayimage.get(adapterPosition).img);
                activityContext.startActivity(intent);
            } else {
                Log.e("ImageAdapter", "Fragment is not attached to an activity!");
            }
        });

    }
    @Override
    public int getItemCount() {
        return arrayimage.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        RelativeLayout rli;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            rli = itemView.findViewById(R.id.rli);
        }
    }
}