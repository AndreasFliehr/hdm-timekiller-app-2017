package de.hdm.dp.bd.chronophage.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdm.dp.bd.chronophage.models.db.DbCalls;

public class TaskList {

    private final DbCalls db;

    private List<Task> tasks;

    public TaskList(DbCalls db) {
        this.db = db;
        tasks = new ArrayList<>();
    }

    public TaskList(DbCalls db, List<Task> tasks) {
        this.db = db;
        this.tasks = tasks;
    }

    public void setTaskActive(Task task) {
        task.start();
    }

    public void setTaskInactive(Task task) {
        task.stop();
    }

    public boolean isTaskActive(Task task) {
        return task.isActive();
    }

    public List<Task> getAllTasks(Context context) {
        return db.getTaskObjects(context);
    }

    public List<Task> getAllTasksWithRecords(Context context) {
        this.tasks = db.getTasksWithRecords(context);
        ArrayList<Task> tasksWithRecords = new ArrayList<>();

        for (Task task: tasks) {
            if (task.getOverallDuration() > 0) {
                tasksWithRecords.add(task);
            }
        }
        return tasksWithRecords;
    }

    public TaskList getFilteredTasksWithRecordsAfter(Date after) {
        ArrayList<Task> tasks = new ArrayList<>();

        for (Task task: this.tasks) {
            Task filtered = task.getTaskWithRecordsAfter(after);
            if (filtered.getOverallDuration() > 0) {
                tasks.add(filtered);
            }
        }
        return new TaskList(db, tasks);
    }

    public TaskList getFilteredTasksWithRecordsBefore(Date before) {
        ArrayList<Task> tasks = new ArrayList<>();

        for (Task task: this.tasks) {
            Task filtered = task.getTaskWithRecordsBefore(before);
            if (filtered.getOverallDuration() > 0) {
                tasks.add(filtered);
            }
        }
        return new TaskList(db, tasks);
    }
}
