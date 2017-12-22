package de.hdm.dp.bd.chronophage.models;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private List<Task> tasks;

    public TaskList() {
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

    public void createTaskList() {
        if (tasks == null) {
            tasks = new ArrayList<>();
            tasks.add(new Task(1, "Internet"));
            tasks.add(new Task(2, "Vorlesungen"));
            tasks.add(new Task(3, "Mails"));
        }
    }
}
