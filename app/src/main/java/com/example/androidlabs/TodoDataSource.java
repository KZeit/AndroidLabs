package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TodoDataSource {

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    private static final String[] allColumns = {
            TodoDatabaseHelper.COLUMN_ID,
            TodoDatabaseHelper.COLUMN_TODO,
            TodoDatabaseHelper.COLUMN_URGENCY
    };

    public TodoDataSource(Context context) {
        dbHelper = new TodoDatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addTodoItem(TodoItem item) {
        ContentValues values = new ContentValues();
        values.put(TodoDatabaseHelper.COLUMN_TODO, item.getTodo());
        values.put(TodoDatabaseHelper.COLUMN_URGENCY, item.getUrgency());
        database.insert(TodoDatabaseHelper.TABLE_TODO, null, values);
    }

    public void deleteTodoItem(int id) {
        database.delete(TodoDatabaseHelper.TABLE_TODO, TodoDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<>();

        Cursor cursor = database.query(TodoDatabaseHelper.TABLE_TODO, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TodoItem item = cursorToTodoItem(cursor);
            todoItems.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return todoItems;
    }

    public Cursor getAllTodoItemsCursor() {
        return database.query(TodoDatabaseHelper.TABLE_TODO, allColumns, null, null, null, null, null);
    }

    private TodoItem cursorToTodoItem(Cursor cursor) {
        return new TodoItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
    }

    public void printCursor(Cursor c) {
        Log.d("Cursor Info", "Database Version: " + database.getVersion());
        Log.d("Cursor Info", "Number of Columns: " + c.getColumnCount());
        Log.d("Cursor Info", "Column Names: " + Arrays.toString(c.getColumnNames()));
        Log.d("Cursor Info", "Number of Rows: " + c.getCount());
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String row = "";
            for (int i = 0; i < c.getColumnCount(); i++) {
                row += c.getString(i) + " ";
            }
            Log.d("Cursor Info", row);
            c.moveToNext();
        }
    }
}