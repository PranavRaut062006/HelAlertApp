package com.example.helalert;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.SmsManager;
import androidx.core.app.ActivityCompat;
import java.util.List;

public class LiveLocationSender {
    private final Context context;
    private final LocationManager locationManager;
    private final List<String> emergencyContacts;

    public LiveLocationSender(Context context, List<String> emergencyContacts) {
        this.context = context;
        this.emergencyContacts = emergencyContacts;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void sendAccidentAlert() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; // Permission not granted
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String locationMessage = "My accident has just occurred, and I need urgent medical assistance. This is my address: "
                        + "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                sendSmsToEmergencyContacts(locationMessage);
            }

            @Override public void onStatusChanged(String provider, int status, android.os.Bundle extras) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onProviderDisabled(String provider) {}
        }, null);
    }

    private void sendSmsToEmergencyContacts(String message) {
        SmsManager smsManager = SmsManager.getDefault();
        for (String contact : emergencyContacts) {
            smsManager.sendTextMessage(contact, null, message, null, null);
        }
    }
}