package com.example.leafy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class PlantImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> images;

    public PlantImageAdapter(Context context, ArrayList<Integer> images) {
        this.context = context;
        this.images = (images != null) ? images : new ArrayList<>();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_plant_image, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        try {
            imageView.setImageResource(images.get(position));
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.gulab); // Fallback image
        }

        return convertView;
    }

    public void updateImages(ArrayList<Integer> newImages) {
        images.clear();
        images.addAll(newImages);
        notifyDataSetChanged();
    }
}
