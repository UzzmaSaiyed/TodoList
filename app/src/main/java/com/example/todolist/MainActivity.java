package com.example.todolist;
import android.view.View;
import android.widget.ListView;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskListView = findViewById(R.id.list_todo);
        updateUI();
    }

    private void updateUI() {
        ArrayList<TaskItem> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_COMPLETED},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int completedIdx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_COMPLETED);
            String task = cursor.getString(idx);
            boolean isCompleted = cursor.getInt(completedIdx) == 1;
            taskList.add(new TaskItem(task, isCompleted));
        }

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(this, taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                showAddTaskDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddTaskDialog() {
        final EditText taskEditText = new EditText(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = taskEditText.getText().toString();
                        if (task.trim().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            addTaskToDatabase(task, "", false);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }

    private void addTaskToDatabase(String title, String description, boolean completed) {
        SQLiteDatabase db = null;
        try {
            db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, title);
            values.put(TaskContract.TaskEntry.COL_TASK_DESCRIPTION, description);
            values.put(TaskContract.TaskEntry.COL_TASK_COMPLETED, completed ? 1 : 0);
            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add task to database", e);
            Toast.makeText(MainActivity.this, "Error adding task", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
            updateUI();
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        String task = taskTextView.getText().toString().replace("[Completed] ", "");
        SQLiteDatabase db = null;
        try {
            db = mHelper.getWritableDatabase();
            db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", new String[]{task});
            Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete task from database", e);
            Toast.makeText(MainActivity.this, "Error deleting task", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
            updateUI();
        }
    }

    public void markTaskCompleted(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_title);
        String task = taskTextView.getText().toString().replace("[Completed] ", "");
        SQLiteDatabase db = null;
        try {
            db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COL_TASK_COMPLETED, 1);
            db.update(TaskContract.TaskEntry.TABLE, values, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", new String[]{task});
            Toast.makeText(MainActivity.this, "Task marked as completed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to mark task as completed in database", e);
            Toast.makeText(MainActivity.this, "Error marking task as completed", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
            updateUI();
        }
    }
}
