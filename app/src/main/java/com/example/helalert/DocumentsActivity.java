package com.example.helalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DocumentsActivity extends AppCompatActivity {
    private EditText editDL, editRC, editInsurance, editPUC, editMedical;
    private Button btnSaveDocs, btnOpenDL, btnOpenRC, btnOpenInsurance, btnOpenPUC, btnOpenMedical;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        editDL = findViewById(R.id.edit_dl);
        editRC = findViewById(R.id.edit_rc);
        editInsurance = findViewById(R.id.edit_insurance);
        editPUC = findViewById(R.id.edit_puc);
        editMedical = findViewById(R.id.edit_medical);
        btnSaveDocs = findViewById(R.id.btn_save_docs);

        btnOpenDL = findViewById(R.id.btn_open_dl);
        btnOpenRC = findViewById(R.id.btn_open_rc);
        btnOpenInsurance = findViewById(R.id.btn_open_insurance);
        btnOpenPUC = findViewById(R.id.btn_open_puc);
        btnOpenMedical = findViewById(R.id.btn_open_medical);

        sharedPreferences = getSharedPreferences("ImportantDocuments", MODE_PRIVATE);
        loadSavedDocuments();

        btnSaveDocs.setOnClickListener(v -> saveDocuments());

        btnOpenDL.setOnClickListener(v -> openDocument(editDL.getText().toString()));
        btnOpenRC.setOnClickListener(v -> openDocument(editRC.getText().toString()));
        btnOpenInsurance.setOnClickListener(v -> openDocument(editInsurance.getText().toString()));
        btnOpenPUC.setOnClickListener(v -> openDocument(editPUC.getText().toString()));
        btnOpenMedical.setOnClickListener(v -> openDocument(editMedical.getText().toString()));
    }

    private void saveDocuments() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dl", editDL.getText().toString());
        editor.putString("rc", editRC.getText().toString());
        editor.putString("insurance", editInsurance.getText().toString());
        editor.putString("puc", editPUC.getText().toString());
        editor.putString("medical", editMedical.getText().toString());
        editor.apply();

        Toast.makeText(this, "Documents saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedDocuments() {
        editDL.setText(sharedPreferences.getString("dl", ""));
        editRC.setText(sharedPreferences.getString("rc", ""));
        editInsurance.setText(sharedPreferences.getString("insurance", ""));
        editPUC.setText(sharedPreferences.getString("puc", ""));
        editMedical.setText(sharedPreferences.getString("medical", ""));
    }

    private void openDocument(String url) {
        if (!url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else {
            Toast.makeText(this, "No URL saved!", Toast.LENGTH_SHORT).show();
        }
    }
}