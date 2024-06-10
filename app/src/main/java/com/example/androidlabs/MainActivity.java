package com.example.androidlabs;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TodoItem> todoList = new ArrayList<>();
    private TodoAdapter todoAdapter;
    private EditText editText;
    private Switch urgentSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.todo_list);
        editText = findViewById(R.id.edit_text);
        urgentSwitch = findViewById(R.id.urgent_switch);
        Button addButton = findViewById(R.id.add_button);

        todoAdapter = new TodoAdapter(this, todoList);
        listView.setAdapter(todoAdapter);

        addButton.setOnClickListener(view -> {
            String text = editText.getText().toString();
            boolean isUrgent = urgentSwitch.isChecked();
            todoList.add(new TodoItem(text, isUrgent));
            editText.setText("");
            todoAdapter.notifyDataSetChanged();
        });

        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + position)
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        todoList.remove(position);
                        todoAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }
}
