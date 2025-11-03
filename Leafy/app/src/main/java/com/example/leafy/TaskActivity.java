package com.example.leafy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskActivity extends AppCompatActivity implements BottomSheetAddTask.TaskAddListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
        taskList = loadTasks(); // Load saved tasks

        updateEmptyState();

        adapter = new TaskAdapter(taskList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Toast.makeText(getApplicationContext(), "Task clicked: " + task.getPlantName(), Toast.LENGTH_SHORT).show();
            }

            public void onEditTaskClick(Task task) {
                BottomSheetAddTask bottomSheetAddTask = new BottomSheetAddTask();
                Bundle bundle = new Bundle();
                bundle.putString("TASK_TITLE", task.getPlantName());
                bundle.putString("TASK_DESCRIPTION", task.getTaskPurpose());
                bundle.putString("TASK_FREQUENCY", task.getFrequency());
                bottomSheetAddTask.setArguments(bundle);
                bottomSheetAddTask.show(getSupportFragmentManager(), "BottomSheetAddTask");
            }

            @Override
            public void onDeleteTaskClick(Task task) {
                new AlertDialog.Builder(TaskActivity.this)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            taskList.remove(task);
                            adapter.notifyDataSetChanged();
                            saveTasks(); // Save updated list
                            Toast.makeText(getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            BottomSheetAddTask bottomSheetAddTask = new BottomSheetAddTask();
            bottomSheetAddTask.show(getSupportFragmentManager(), bottomSheetAddTask.getTag());
        });

        createNotificationChannel();
        setupSwipeToMarkAsCompleted();
    }

    private void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("LeafyCareTasks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(taskList); // Convert taskList to JSON
        editor.putString("task_list", json);
        editor.apply();
    }


    private List<Task> loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("LeafyCareTasks", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task_list", null);

        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasks = gson.fromJson(json, type);

        return tasks != null ? tasks : new ArrayList<>();
    }



    @Override
    public void onTaskAdded(Task task) {
        taskList.add(task);
        adapter.notifyDataSetChanged();
        saveTasks(); // Save tasks persistently
        scheduleTaskReminder(task);
        updateEmptyState();

    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "plant_tasks_channel";
            CharSequence channelName = "Plant Tasks";
            String channelDescription = "Channel for plant task reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void scheduleTaskReminder(Task task) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Exact alarm permission not granted. Unable to set reminder.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ReminderReceiver.class);
            intent.putExtra("plantName", task.getPlantName());
            intent.putExtra("taskPurpose", task.getTaskPurpose());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    task.getPlantName().hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(task.getTime());

            try {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
                Toast.makeText(this, "Reminder set for " + task.getPlantName(), Toast.LENGTH_SHORT).show();
            } catch (SecurityException e) {
                Toast.makeText(this, "Unable to schedule alarm. Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupSwipeToMarkAsCompleted() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    Paint paint = new Paint();
                    paint.setColor(getResources().getColor(R.color.primary_variant_light)); // Change background color as needed
                    paint.setAntiAlias(true);

                    View itemView = viewHolder.itemView;
                    float cornerRadius = 16f;
                    float left = itemView.getRight() + dX;
                    float right = itemView.getRight();
                    float top = itemView.getTop();
                    float bottom = itemView.getBottom();

                    RectF background = new RectF(left, top, right, bottom);
                    canvas.drawRoundRect(background, cornerRadius, cornerRadius, paint);
                }

                Drawable completeIcon = getResources().getDrawable(R.drawable.ic_check, null);

                // Set fixed size for the icon
                int iconSize = 96; // Fixed size in pixels (modify as needed)
                int iconMargin = (viewHolder.itemView.getHeight() - iconSize) / 2;

                int iconTop = viewHolder.itemView.getTop() + iconMargin;
                int iconBottom = iconTop + iconSize;
                int iconRight = viewHolder.itemView.getRight() - iconMargin;
                int iconLeft = iconRight - iconSize;

                completeIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                completeIcon.draw(canvas);

                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                showMarkAsCompletedDialog(viewHolder.getAdapterPosition());
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showMarkAsCompletedDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Mark as Completed")
                .setMessage("Are you sure you want to mark this task as completed?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    markTaskAsCompleted(position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    adapter.notifyItemChanged(position);
                    updateEmptyState();

                })
                .setCancelable(false)
                .show();
    }

    private void markTaskAsCompleted(int position) {
        Task task = taskList.get(position);
        cancelTaskReminder(task);
        taskList.remove(position);
        adapter.notifyItemRemoved(position);
        updateEmptyState();
        Toast.makeText(this, "Task marked as completed", Toast.LENGTH_SHORT).show();
    }

    private void cancelTaskReminder(Task task) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            Intent intent = new Intent(this, ReminderReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    task.getPlantName().hashCode(),
                    intent,
                    PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
            );

            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }
    private void updateEmptyState() {
        ImageView emptyImage = findViewById(R.id.emptyImage);
        TextView noReminderText = findViewById(R.id.noReminderText);
        TextView infoText = findViewById(R.id.infoText);

        if (taskList.isEmpty()) {
            emptyImage.setVisibility(View.VISIBLE);
            noReminderText.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyImage.setVisibility(View.GONE);
            noReminderText.setVisibility(View.GONE);
            infoText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
