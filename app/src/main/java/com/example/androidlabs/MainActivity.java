package com.example.androidlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText;
    private static final int REQUEST_CODE = 1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.editTextName);
        Button nextButton = findViewById(R.id.buttonNext);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String savedName = sharedPreferences.getString("user_name", "");

        if (!savedName.isEmpty()) {
            nameEditText.setText(savedName);
        }

        nextButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            Intent intent = new Intent(MainActivity.this, NameActivity.class);
            intent.putExtra("user_name", name);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", nameEditText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == 0) {
                nameEditText.setText("");
            } else if (resultCode == 1) {
                finish();
            }
        }
    }
}
