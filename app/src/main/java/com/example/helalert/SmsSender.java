package com.example.helalert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SmsSender {
    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    public SmsSender(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void sendEmergencySMS() {
        SharedPreferences prefs = context.getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE);

        // Fetch all 5 emergency contacts
        String[] contacts = new String[5];
        for (int i = 0; i < 5; i++) {
            contacts[i] = prefs.getString("contact_" + (i + 1), "");  // Fetch contacts dynamically
        }

        // Check if at least one contact is saved
        boolean hasContacts = false;
        for (String contact : contacts) {
            if (!contact.isEmpty()) {
                hasContacts = true;
                break;
            }
        }

        if (!hasContacts) {
            Log.e("SmsSender", "No emergency contacts set. SMS not sent.");
            return;
        }

        getLocationAndSendSMS(contacts);
    }

    @SuppressLint("MissingPermission")
    private void getLocationAndSendSMS(String[] contacts) {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("SmsSender", "Location permission not granted.");
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                String locationUrl;
                if (location != null) {
                    locationUrl = "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                } else {
                    locationUrl = "Location not available";
                }

                String message = "My accident has just occurred, and I need urgent medical assistance. My location: " + locationUrl;

                // Send SMS to all saved contacts
                for (String contact : contacts) {
                    sendSMS(contact, message);
                }
            }
        });
    }

    private void sendSMS(String contact, String message) {
        if (!contact.isEmpty()) {
            // Check SMS permission before sending
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("SmsSender", "SMS permission not granted.");
                return;
            }

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(contact, null, message, null, null);
                Log.d("SmsSender", "Emergency SMS sent to: " + contact);
            } catch (Exception e) {
                Log.e("SmsSender", "Failed to send SMS", e);
            }
        }
    }
}
