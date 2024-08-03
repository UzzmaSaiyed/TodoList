package com.example.todolist;

public class TaskItem {
    private String title;
    private boolean completed;

    public TaskItem(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}