package com.example.helalert;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class ServicingActivity extends AppCompatActivity {
    private EditText editMonths;
    private Button btnSaveServicing;
    private TextView textViewInterval; // Added TextView to display the saved interval
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ServicingPrefs";
    private static final String KEY_MONTHS = "servicingMonths";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicing);

        editMonths = findViewById(R.id.edit_months);
        btnSaveServicing = findViewById(R.id.btn_save_servicing);
        textViewInterval = findViewById(R.id.textViewInterval); // Initialize TextView

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loadSavedInterval();

        btnSaveServicing.setOnClickListener(v -> saveServicingInterval());

        createNotificationChannel();
    }

    private void saveServicingInterval() {
        String monthsText = editMonths.getText().toString().trim();
        if (monthsText.isEmpty()) {
            Toast.makeText(this, "Please enter a valid number of months.", Toast.LENGTH_SHORT).show();
            return;
        }

        int months = Integer.parseInt(monthsText);
        if (months <= 0) {
            Toast.makeText(this, "Interval must be at least 1 month.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save new interval
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_MONTHS, months);
        editor.apply();

        // Schedule notification
        scheduleServicingNotification(months);

        // Update displayed interval
        textViewInterval.setText("Current Interval: " + months + " months");

        Toast.makeText(this, "Servicing interval saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedInterval() {
        int savedMonths = sharedPreferences.getInt(KEY_MONTHS, 0);
        if (savedMonths > 0) {
            editMonths.setText(String.valueOf(savedMonths));
            textViewInterval.setText("Current Interval: " + savedMonths + " months");
        } else {
            textViewInterval.setText("No servicing interval set.");
        }
    }

    private void scheduleServicingNotification(int months) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ServicingReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, months);
        long triggerTime = calendar.getTimeInMillis();

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "servicing_channel",
                    "Servicing Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}