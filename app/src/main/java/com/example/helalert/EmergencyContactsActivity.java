package com.example.helalert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmergencyContactsActivity extends AppCompatActivity {
    private EditText contact1, contact2, contact3, contact4, contact5;
    private Button saveButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        contact1 = findViewById(R.id.contact1);
        contact2 = findViewById(R.id.contact2);
        contact3 = findViewById(R.id.contact3);
        contact4 = findViewById(R.id.contact4);
        contact5 = findViewById(R.id.contact5);
        saveButton = findViewById(R.id.btn_save_contacts);

        sharedPreferences = getSharedPreferences("EmergencyContacts", MODE_PRIVATE);
        loadSavedContacts();

        saveButton.setOnClickListener(v -> saveContacts());
    }

    private void saveContacts() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("contact1", contact1.getText().toString());
        editor.putString("contact2", contact2.getText().toString());
        editor.putString("contact3", contact3.getText().toString());
        editor.putString("contact4", contact4.getText().toString());
        editor.putString("contact5", contact5.getText().toString());
        editor.apply();

        Toast.makeText(this, "Contacts saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedContacts() {
        contact1.setText(sharedPreferences.getString("contact1", ""));
        contact2.setText(sharedPreferences.getString("contact2", ""));
        contact3.setText(sharedPreferences.getString("contact3", ""));
        contact4.setText(sharedPreferences.getString("contact4", ""));
        contact5.setText(sharedPreferences.getString("contact5", ""));
    }
}