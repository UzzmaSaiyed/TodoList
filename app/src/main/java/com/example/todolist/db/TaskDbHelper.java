package com.example.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist.TaskContract;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_TASK_DESCRIPTION + " TEXT, " + // New column for description
                TaskContract.TaskEntry.COL_TASK_COMPLETED + " INTEGER DEFAULT 0);"; // New column for completion status

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Upgrade logic from version 1 to 2
            db.execSQL("ALTER TABLE " + TaskContract.TaskEntry.TABLE + " ADD COLUMN " +
                    TaskContract.TaskEntry.COL_TASK_DESCRIPTION + " TEXT;");
            db.execSQL("ALTER TABLE " + TaskContract.TaskEntry.TABLE + " ADD COLUMN " +
                    TaskContract.TaskEntry.COL_TASK_COMPLETED + " INTEGER DEFAULT 0;");
        }
        // Handle further upgrades here
    }
}