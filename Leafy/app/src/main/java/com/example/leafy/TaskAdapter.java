package com.example.leafy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private final OnTaskClickListener listener;

    // Constructor with click listener
    public TaskAdapter(List<Task> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        // Set task-specific details
        holder.title.setText(task.getPlantName());
        holder.description.setText(task.getTaskPurpose());

        // Format and set date details
        long timeInMillis = task.getTime();
        String[] dateParts = getFormattedDateParts(timeInMillis);
        holder.day.setText(dateParts[0]);
        holder.date.setText(dateParts[1]);
        holder.month.setText(dateParts[2]);

        // Calculate remaining time
        long currentTime = System.currentTimeMillis();
        long timeDifference = timeInMillis - currentTime;

        if (timeDifference > 0) {
            // Task is in the future
            holder.remainingTime.setText(formatRemainingTime(timeDifference));
            holder.remainingTime.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.primary));
        } else {
            // Task time has passed
            holder.remainingTime.setText("Time passed");
            holder.remainingTime.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        // Handle card click event
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });

        // Handle edit icon click
        holder.editIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditTaskClick(task);
            }
        });

        // Handle delete icon click
        holder.deleteIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteTaskClick(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Helper method to format date and time into parts
    private String[] getFormattedDateParts(long timeInMillis) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());

        Date date = new Date(timeInMillis);
        return new String[]{dayFormat.format(date), dateFormat.format(date), monthFormat.format(date)};
    }

    // ViewHolder class
    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, day, date, month, remainingTime;
        ImageView editIcon, deleteIcon;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            description = itemView.findViewById(R.id.taskDescription);
            day = itemView.findViewById(R.id.d);
            date = itemView.findViewById(R.id.taskTime);
            month = itemView.findViewById(R.id.m);
            editIcon = itemView.findViewById(R.id.editIcon);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            remainingTime = itemView.findViewById(R.id.remainingTime); // Add this line
        }
    }


    // Interface for handling task clicks
    public interface OnTaskClickListener {
        void onTaskClick(Task task);          // General task click
        void onEditTaskClick(Task task);      // Edit icon click
        void onDeleteTaskClick(Task task);    // Delete icon click
    }

    private String formatRemainingTime(long timeDifference) {
        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        minutes = minutes % 60; // Get remaining minutes after calculating hours
        return hours + "h " + minutes + "m remaining";
    }

}
