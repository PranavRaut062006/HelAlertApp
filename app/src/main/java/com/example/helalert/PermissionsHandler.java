package com.example.helalert;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsHandler {
    private static final int REQUEST_CODE = 100;
    private final Activity activity;

    public PermissionsHandler(Activity activity) {
        this.activity = activity;
    }

    public void requestPermissions() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS
        };

        if (!hasAllPermissions(permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
        }
    }

    private boolean hasAllPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}