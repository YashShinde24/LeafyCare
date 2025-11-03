package com.example.leafy;

import java.io.Serializable;

public class Task implements Serializable {
    private int taskId;             // Unique ID for the task
    private String plantName;       // Name of the plant associated with the task
    private String taskPurpose;     // Purpose of the task (e.g., watering, fertilizing)
    private long time;              // Time in milliseconds for the task
    private String frequency;       // Frequency of the task (e.g., Daily, Weekly, Monthly)

    // Default Constructor (for flexibility)
    public Task() {
        this.taskId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE); // Generate a unique ID
        this.plantName = "";
        this.taskPurpose = "";
        this.time = System.currentTimeMillis(); // Default to current time
        this.frequency = "Once"; // Default frequency
    }

    // Parameterized Constructor
    public Task(int taskId, String plantName, String taskPurpose, long time, String frequency) {
        this.taskId = taskId;
        this.plantName = plantName;
        this.taskPurpose = taskPurpose;
        this.time = time;
        this.frequency = frequency;
    }

    // Constructor without taskId (auto-generate it)
    public Task(String plantName, String taskPurpose, long time, String frequency) {
        this.taskId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE); // Auto-generate unique ID
        this.plantName = plantName;
        this.taskPurpose = taskPurpose;
        this.time = time;
        this.frequency = frequency;
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getTaskPurpose() {
        return taskPurpose;
    }

    public void setTaskPurpose(String taskPurpose) {
        this.taskPurpose = taskPurpose;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    // Utility Methods
    public String getFormattedTime() {
        // Converts the time in milliseconds to a readable time string
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(time));
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", plantName='" + plantName + '\'' +
                ", taskPurpose='" + taskPurpose + '\'' +
                ", time=" + getFormattedTime() +
                ", frequency='" + frequency + '\'' +
                '}';
    }
}
