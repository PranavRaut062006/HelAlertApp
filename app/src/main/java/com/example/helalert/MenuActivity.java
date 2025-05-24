package com.example.helalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.helalert.ServicingActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnEmergencyContacts = findViewById(R.id.btn_emergency_contacts);
        Button btnDocuments = findViewById(R.id.btn_documents);
        Button btnServiceInterval = findViewById(R.id.btn_service_interval);
        Button btnBluetooth = findViewById(R.id.btn_bluetooth);

        btnEmergencyContacts.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, EmergencyContactsActivity.class)));
        btnDocuments.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, DocumentsActivity.class)));
        btnServiceInterval.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, ServicingActivity.class)));
        btnBluetooth.setOnClickListener(v -> startActivity(new Intent(MenuActivity.this, BluetoothActivity.class)));
    }
}