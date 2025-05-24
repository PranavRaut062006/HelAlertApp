package com.example.helalert;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.helalert.BluetoothActivity;


public class AccidentResponseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_response);

        TextView messageTextView = findViewById(R.id.accident_message);
        Button yesButton = findViewById(R.id.btn_yes_safe);

        messageTextView.setText("Hey, are you safe?");

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mark user as responded
                BluetoothActivity.setUserResponded(true);
                finish(); // Now close the activity
            }
        });

    }
}