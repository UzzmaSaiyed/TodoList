package com.example.todolist;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.todolist.db";
    public static final int DB_VERSION = 2; // Increment the DB version

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DESCRIPTION = "description"; // New column for description
        public static final String COL_TASK_COMPLETED = "completed"; // New column for completion status
    }
}