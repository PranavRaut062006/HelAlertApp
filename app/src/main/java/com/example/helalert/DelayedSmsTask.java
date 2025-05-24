package com.example.helalert;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class DelayedSmsTask {
    private final Context context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final long DELAY_MILLIS = 2 * 60 * 1000; // 2 minutes delay
    private final Runnable smsRunnable; // Runnable for delayed SMS

    public DelayedSmsTask(Context context) {
        this.context = context;
        this.smsRunnable = () -> {
            SharedPreferences prefs = context.getSharedPreferences("HelAlertPrefs", Context.MODE_PRIVATE);
            boolean userResponded = prefs.getBoolean("UserResponded", false);

            if (!userResponded) {
                Log.d("DelayedSmsTask", "No user response. Sending emergency SMS...");

                SmsSender smsSender = new SmsSender(context);
                smsSender.sendEmergencySMS();

                // Mark SMS as sent
                prefs.edit().putBoolean("SmsAlreadySent", true).apply();
            } else {
                Log.d("DelayedSmsTask", "User responded in time. No SMS sent.");

                // Reset SMS sent flag for future accidents
                prefs.edit().putBoolean("SmsAlreadySent", false).apply();
            }
        };
    }

    // Start the emergency SMS countdown
    public void startEmergencySmsCountdown() {
        SharedPreferences prefs = context.getSharedPreferences("HelAlertPrefs", Context.MODE_PRIVATE);
        boolean smsAlreadySent = prefs.getBoolean("SmsAlreadySent", false);

        if (smsAlreadySent) {
            Log.d("DelayedSmsTask", "SMS already sent. Skipping countdown...");
            return; // Do not send SMS again
        }

        Log.d("DelayedSmsTask", "Starting 2-minute SMS countdown...");
        handler.postDelayed(smsRunnable, DELAY_MILLIS);
    }

    // Cancel the SMS countdown (if the user confirms safety)
    public void cancelEmergencySmsCountdown() {
        Log.d("DelayedSmsTask", "SMS countdown canceled.");
        handler.removeCallbacks(smsRunnable);
    }
}
