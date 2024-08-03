package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.todolist.TaskItem;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<TaskItem> {
    public TaskAdapter(Context context, ArrayList<TaskItem> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reuse existing view if possible
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        // Get the data item for this position
        TaskItem taskItem = getItem(position);

        // Lookup view for data population
        TextView taskTitle = convertView.findViewById(R.id.task_title);
        TextView taskCompletedLabel = convertView.findViewById(R.id.task_completed_label);
        Button btnComplete = convertView.findViewById(R.id.btn_complete);
        Button btnDelete = convertView.findViewById(R.id.btn_delete);

        // Populate the data into the template view using the data object
        taskTitle.setText(taskItem.getTitle());

        // Show or hide buttons and labels based on completion status
        if (taskItem.isCompleted()) {
            taskCompletedLabel.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.GONE);
        } else {
            taskCompletedLabel.setVisibility(View.GONE);
            btnComplete.setVisibility(View.VISIBLE);
        }

        // Set up the button click listeners
        btnDelete.setOnClickListener(v -> {
            // Handle delete action
            ((MainActivity) getContext()).deleteTask(v);
        });

        btnComplete.setOnClickListener(v -> {
            // Handle complete action
            ((MainActivity) getContext()).markTaskCompleted(v);
        });

        // Return the completed view to render on screen
        return convertView;
    }
}