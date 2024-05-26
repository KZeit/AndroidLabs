package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView welcomeTextView = findViewById(R.id.textViewWelcome);
        Button thankYouButton = findViewById(R.id.buttonThankYou);
        Button dontCallMeButton = findViewById(R.id.buttonDontCallMe);

        String userName = getIntent().getStringExtra("user_name");
        welcomeTextView.setText("Welcome " + userName + "!");

        thankYouButton.setOnClickListener(v -> {
            setResult(1);
            finish();
        });

        dontCallMeButton.setOnClickListener(v -> {
            setResult(0);
            finish();
        });
    }
}
