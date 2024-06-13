package com.example.androidlabs;

public class TodoItem {
    private int id;
    private String todo;
    private int urgency;

    public TodoItem(int id, String todo, int urgency) {
        this.id = id;
        this.todo = todo;
        this.urgency = urgency;
    }

    public int getId() {
        return id;
    }

    public String getTodo() {
        return todo;
    }

    public int getUrgency() {
        return urgency;
    }

    @Override
    public String toString() {
        return todo + " (Urgency: " + urgency + ")";
    }
}
