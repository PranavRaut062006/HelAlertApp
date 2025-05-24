package com.example.helalert;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("YES_SAFE_ACTION".equals(intent.getAction())) {
            Log.d("NotificationReceiver", "User confirmed safety. Resetting SMS flag.");

            SharedPreferences prefs = context.getSharedPreferences("HelAlertPrefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("UserResponded", true).putBoolean("SmsAlreadySent", false).apply();
        }
    }
}
