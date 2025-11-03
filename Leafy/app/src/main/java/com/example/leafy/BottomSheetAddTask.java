package com.example.leafy;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BottomSheetAddTask extends BottomSheetDialogFragment {

    private Spinner plantNameSpinner, frequencySpinner;
    private AutoCompleteTextView taskInput;
    private TextView time, date;
    private Button saveButton;

    private Calendar selectedTime;
    private Calendar selectedDate;
    private List<String> plantNames;
    private List<String> frequencies;
    private List<String> taskSuggestions;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_add_task, container, false);

        // Initialize views
        plantNameSpinner = view.findViewById(R.id.plantNameSpinner);
        frequencySpinner = view.findViewById(R.id.frequencySpinner);
        taskInput = view.findViewById(R.id.taskInput);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        saveButton = view.findViewById(R.id.saveButton);

        selectedTime = Calendar.getInstance();
        selectedDate = Calendar.getInstance();

        // Fetch data for dropdowns and suggestions
        plantNames = getPlantNames();
        frequencies = getFrequencyOptions();
        taskSuggestions = getTaskSuggestions();

        // Set up spinners and AutoCompleteTextView
        setupSpinnersAndSuggestions();

        // Handle date selection
        date.setOnClickListener(v -> showDatePicker());

        // Handle time selection
        time.setOnClickListener(v -> showTimePicker());

        // Handle save button click
        saveButton.setOnClickListener(v -> handleSaveTask());

        if (getArguments() != null) {
            String taskTitle = getArguments().getString("TASK_TITLE");
            String taskDescription = getArguments().getString("TASK_DESCRIPTION");
            String taskFrequency = getArguments().getString("TASK_FREQUENCY"); // Retrieve frequency

            // Populate the fields
            if (taskTitle != null && plantNames.contains(taskTitle)) {
                int position = plantNames.indexOf(taskTitle);
                plantNameSpinner.setSelection(position);
            }

            if (taskDescription != null) {
                taskInput.setText(taskDescription);
            }

            if (taskFrequency != null && frequencies.contains(taskFrequency)) {
                int position = frequencies.indexOf(taskFrequency);
                frequencySpinner.setSelection(position); // Set the selected frequency
            }
        }




        return view;
    }

    private void setupSpinnersAndSuggestions() {
        // Set up plant names spinner
        ArrayAdapter<String> plantAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, plantNames);
        plantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plantNameSpinner.setAdapter(plantAdapter);

        // Set up frequency spinner
        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, frequencies);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);

        // Set up task AutoCompleteTextView
        ArrayAdapter<String> taskAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, taskSuggestions);
        taskInput.setAdapter(taskAdapter);
        taskInput.setThreshold(1);
    }

    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            date.setText(dateFormat.format(selectedDate.getTime()));
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void showTimePicker() {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.setOnTimeSetListener((hourOfDay, minute) -> {
            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            selectedTime.set(Calendar.MINUTE, minute);
            selectedTime.set(Calendar.SECOND, 0);
            time.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
        });
        timePicker.show(getParentFragmentManager(), "timePicker");
    }

    private void handleSaveTask() {
        String selectedPlant = plantNameSpinner.getSelectedItem().toString();
        String frequency = frequencySpinner.getSelectedItem().toString();
        String taskPurpose = taskInput.getText().toString().trim();

        // Validate inputs
        if (selectedPlant.isEmpty()) {
            Toast.makeText(getContext(), "Please select a plant", Toast.LENGTH_SHORT).show();
            return;
        }
        if (taskPurpose.isEmpty()) {
            taskInput.setError("Task purpose cannot be empty");
            return;
        }
        if (date.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (time.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Combine date and time
        selectedDate.set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY));
        selectedDate.set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE));
        selectedDate.set(Calendar.SECOND, 0);

        long timeInMillis = selectedDate.getTimeInMillis();

        Task task = new Task(selectedPlant, taskPurpose, timeInMillis, frequency);

        // Pass the task to the parent activity or fragment
        if (getActivity() instanceof TaskAddListener) {
            ((TaskAddListener) getActivity()).onTaskAdded(task);
        }

        // Schedule the notification
        scheduleNotification(task);

        // Close the bottom sheet
        dismiss();
    }

    private void scheduleNotification(Task task) {
        Context context = getContext();
        if (context == null) return;

        // Map of plant names to image resource IDs
        Map<String, Integer> plantImageMap = new HashMap<>();
        plantImageMap.put("Aloe Vera", R.drawable.navbar_header_logo);
        plantImageMap.put("Money Plant", R.drawable.ic_baseline_delete);
        plantImageMap.put("Cactus", R.drawable.ic_baseline_edit);
        // Add more plant names and corresponding images as needed

        // Get the plant image resource ID based on the plant name
        int plantImageResId = R.drawable.navbar_header_logo; // Default image
        if (task.getPlantName() != null && plantImageMap.containsKey(task.getPlantName())) {
            plantImageResId = plantImageMap.get(task.getPlantName());
        }

        // Create an intent for the NotificationReceiver
        Intent notificationIntent = new Intent(context, NotificationReceiverforTask.class);
        notificationIntent.putExtra("title", task.getPlantName()); // Set plant name as the title
        notificationIntent.putExtra("description", "Reminder: " + task.getTaskPurpose()); // Task purpose as description
        notificationIntent.putExtra("taskId", task.getTaskId()); // Pass a unique ID for the task
        notificationIntent.putExtra("plantImageResId", plantImageResId); // Pass the plant image resource ID

        // Create a PendingIntent for the notification
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getTaskId(), // Unique request code for each task
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Access the AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Check if exact alarms are allowed for Android 12+ (API 31+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(context, "Exact alarm scheduling is not permitted. Notification may be delayed.", Toast.LENGTH_SHORT).show();
                // Fallback to inexact alarm
                alarmManager.set(AlarmManager.RTC_WAKEUP, task.getTime(), pendingIntent);
            } else {
                // Schedule the exact alarm
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getTime(), pendingIntent);
            }
        }
    }



    private List<String> getPlantNames() {
        return new ArrayList<String>() {{
            add("Select a plant"); // Placeholder option
            add("Rose");
            add("Tulip");
            add("Basil");
            add("Cactus");
            add("Money Plant");
        }};
    }


    private List<String> getFrequencyOptions() {
        return new ArrayList<String>() {{
            add("One-Time"); // For a single specific date
            add("Daily");
            add("Weekly");
            add("Monthly");
        }};
    }

    private List<String> getTaskSuggestions() {
        return new ArrayList<String>() {{
            add("Watering");
            add("Fertilizing");
            add("Pruning");
            add("Pest Control");
            add("Repotting");
        }};
    }

    public interface TaskAddListener {
        void onTaskAdded(Task task);
    }
}
