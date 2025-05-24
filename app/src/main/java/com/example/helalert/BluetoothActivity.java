package com.example.helalert;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothActivity";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private Handler handler = new Handler();

    private NotificationHandler notificationHandler;
    private SmsSender smsSender;

    private ListView deviceListView;
    private Button connectButton;
    private TextView statusText;
    private ArrayAdapter<String> deviceListAdapter;
    private ArrayList<BluetoothDevice> pairedDevicesList = new ArrayList<>();

    private boolean isSmsScheduled = false;
    private static boolean isUserResponded = false; // still static so other classes can modify it

    private TextView statusTextView;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        statusTextView = findViewById(R.id.statusTextView);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        notificationHandler = new NotificationHandler(this);
        smsSender = new SmsSender(this);

        deviceListView = findViewById(R.id.deviceListView);
        connectButton = findViewById(R.id.connectButton);
        statusText = findViewById(R.id.statusText);
        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(deviceListAdapter);

        prefs = getSharedPreferences("HelAlertPrefs", MODE_PRIVATE);

        loadPairedDevices();

        deviceListView.setOnItemClickListener((adapterView, view, position, id) -> {
            BluetoothDevice device = pairedDevicesList.get(position);
            connectToDevice(device);
        });

        connectButton.setOnClickListener(view -> loadPairedDevices());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

    }

    private void loadPairedDevices() {
        deviceListAdapter.clear();
        pairedDevicesList.clear();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Enable Bluetooth first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Grant Bluetooth permissions in settings", Toast.LENGTH_LONG).show();
            return;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.isEmpty()) {
            Toast.makeText(this, "No paired devices found. Pair HC-05 in Bluetooth settings.", Toast.LENGTH_LONG).show();
            return;
        }

        for (BluetoothDevice device : pairedDevices) {
            deviceListAdapter.add(device.getName() + " - " + device.getAddress());
            pairedDevicesList.add(device);
        }

        deviceListAdapter.notifyDataSetChanged();
    }

    private void connectToDevice(BluetoothDevice device) {
        new Thread(() -> {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                inputStream = bluetoothSocket.getInputStream();

                runOnUiThread(() -> {
                    statusText.setText("Connected to " + device.getName());
                    Toast.makeText(this, "Connected to " + device.getName(), Toast.LENGTH_SHORT).show();
                });

                listenForData();
            } catch (IOException e) {
                Log.e(TAG, "Connection failed", e);
                runOnUiThread(() -> {
                    statusText.setText("Connection failed");
                    Toast.makeText(this, "Bluetooth Connection Failed", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void listenForData() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            int bytes;
            try {
                while (bluetoothSocket != null && bluetoothSocket.isConnected()) {
                    bytes = inputStream.read(buffer);
                    String receivedMessage = new String(buffer, 0, bytes).trim();
                    Log.d(TAG, "Received: " + receivedMessage);
                    runOnUiThread(() -> statusText.setText("Received Data: " + receivedMessage));

                    if (receivedMessage.contains("!!! Accident Detected !!!")) {
                        if (!isUserResponded) {
                            isUserResponded = false;
                            isSmsScheduled = false;
                            notificationHandler.sendAccidentNotification();
                            scheduleEmergencySMS();
                        } else {
                            Log.d(TAG, "User already responded safe. Notification skipped.");
                        }
                    }

                }
            } catch (IOException e) {
                Log.e(TAG, "Bluetooth connection lost. Reconnecting...", e);
                reconnectToBluetooth();
            }
        }).start();
    }

    private void scheduleEmergencySMS() {
        if (!isSmsScheduled) {
            isSmsScheduled = true;
            handler.postDelayed(() -> {
                boolean userResponded = prefs.getBoolean("UserResponded", false);
                if (!userResponded) {
                    Log.d(TAG, "No user response. Sending emergency SMS...");
                    smsSender.sendEmergencySMS();
                } else {
                    Log.d(TAG, "User responded in time. No SMS sent.");
                }
                isSmsScheduled = false;
            }, 2 * 60 * 1000); // 2 minutes
        }
    }

    private void reconnectToBluetooth() {
        runOnUiThread(() -> statusText.setText("Reconnecting to Bluetooth..."));
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException ignored) {}
        }
        loadPairedDevices();
    }

    private void updateBluetoothUI(String message) {
        runOnUiThread(() -> {
            statusTextView.setText(message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    // ADD THIS STATIC METHOD FOR GLOBAL ACCESS
    public static void setUserResponded(boolean responded) {
        isUserResponded = responded;
    }
}
