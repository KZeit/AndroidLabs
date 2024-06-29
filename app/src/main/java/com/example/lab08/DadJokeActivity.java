package com.example.lab08;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DadJokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dad_joke);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dad Joke");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set a joke to be displayed
        TextView textView = findViewById(R.id.text_dad_joke);
        textView.setText("Why don't skeletons fight each other? They don't have the guts.");
    }
}
