package com.example.androidlabs;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView result;
    Button buttonSwitch;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contraint);

        editText = (EditText) findViewById(R.id.edit_text);
        result = (TextView) findViewById(R.id.text_view);
        buttonSwitch = (Button) findViewById(R.id.press_btn);
        checkBox = (CheckBox) findViewById(R.id.check_box);

        final AppCompatActivity context = this;
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                result.setText(text);
                Toast.makeText(context, getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });

        checkBox.setOnCheckedChangeListener(new
                                                    CompoundButton.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                                                            if (isChecked) {
                                                                Snackbar.make(cb, getString(R.string.snack_on), Snackbar.LENGTH_LONG)
                                                                        .setAction("undo", click -> cb.setChecked(!isChecked))
                                                                        .show();
                                                            } else {
                                                                Snackbar.make(cb, getString(R.string.snack_off), Snackbar.LENGTH_LONG)
                                                                        .setAction("undo", click -> cb.setChecked(!isChecked))
                                                                        .show();
                                                            }
                                                        }
                                                    });
    }
}


