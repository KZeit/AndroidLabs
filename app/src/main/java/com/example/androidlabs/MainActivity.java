package com.example.androidlabs;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TodoDataSource dataSource;
    private ArrayAdapter<TodoItem> adapter;
    private List<TodoItem> todoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TodoDataSource(this);
        dataSource.open();

        todoItems = dataSource.getAllTodoItems();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);

        ListView listView = findViewById(R.id.todo_list);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            TodoItem item = adapter.getItem(position);
            if (item != null) {
                dataSource.deleteTodoItem(item.getId());
                adapter.remove(item);
                adapter.notifyDataSetChanged();
            }
            return true;
        });

        Button addButton = findViewById(R.id.add_button);
        EditText todoEditText = findViewById(R.id.todo_edit_text);

        addButton.setOnClickListener(v -> {
            String todoText = todoEditText.getText().toString();
            int urgency = 1; // You might want to get this value from a user input
            TodoItem newItem = new TodoItem(0, todoText, urgency);
            dataSource.addTodoItem(newItem);
            todoItems.add(newItem);
            adapter.notifyDataSetChanged();
            todoEditText.setText("");
        });

        // Get cursor and print cursor info
        Cursor cursor = dataSource.getAllTodoItemsCursor();
        dataSource.printCursor(cursor);
    }

    @Override
    protected void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }
}