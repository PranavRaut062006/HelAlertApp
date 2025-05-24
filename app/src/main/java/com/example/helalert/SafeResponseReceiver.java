package com.example.helalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class SafeResponseReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SafeResponseReceiver", "User confirmed they are safe. Cancelling SMS.");

        // Mark user responded in SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("HelAlertPrefs", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("UserResponded", true).apply();
    }
}
