package de.hdm.dp.bd.chronophage.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskList {

    private List<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
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

    public List<Task> getAllTasks() {
        return tasks;
    }

    public List<Task> getAllTasksWithRecords() {
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
        return new TaskList(tasks);
    }

    public TaskList getFilteredTasksWithRecordsBefore(Date before) {
        ArrayList<Task> tasks = new ArrayList<>();

        for (Task task: this.tasks) {
            Task filtered = task.getTaskWithRecordsBefore(before);
            if (filtered.getOverallDuration() > 0) {
                tasks.add(filtered);
            }
        }
        return new TaskList(tasks);
    }

    public void createTaskList() {
        if (tasks.isEmpty()) {
            tasks.add(new Task(1, "Internet"));
            tasks.add(new Task(2, "Vorlesungen"));
            tasks.add(new Task(3, "Mails"));
        }
    }
}
