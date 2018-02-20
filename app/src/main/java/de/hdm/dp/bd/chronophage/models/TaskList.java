package de.hdm.dp.bd.chronophage.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdm.dp.bd.chronophage.models.db.TaskListProvider;

public class TaskList {

    private final TaskListProvider taskListProvider;
    private final Date after;
    private final Date before;

    public TaskList(TaskListProvider taskListProvider) {
        this.taskListProvider = taskListProvider;
        after = null;
        before = null;
    }

    public TaskList(TaskListProvider context, Date before, Date after) {
        this.taskListProvider = context;
        this.before = before != null ? new Date(before.getTime()) : null;
        this.after =  after != null ? new Date(after.getTime()) : null;
    }

    public void setTaskActive(Task task) {
        task.start();
    }

    public void setTaskInactive(Task task) {
        task.stop();
        taskListProvider.updateTasksRecords(task);
    }

    public boolean isTaskActive(Task task) {
        return task.isActive();
    }

    public List<Task> getAllTasks() {
        return taskListProvider.getAllRecordLessTasks();
    }

    public List<Task> getAllTasksWithRecords() {
        List<Task> tasks = taskListProvider.getAllTasks();
        ArrayList<Task> tasksWithRecords = new ArrayList<>();

        for (Task task : tasks) {
            Task filteredTask = filterIfApplicable(task);
            if (filteredTask.getOverallDuration() > 0) {
                tasksWithRecords.add(filteredTask);
            }
        }
        return tasksWithRecords;
    }

    private Task filterIfApplicable(Task task) {
        if (before == null && after == null) {
            return task;
        } else if (before != null && after != null) {
            return task.getTaskWithRecordsAfter(after).getTaskWithRecordsBefore(before);
        } else if (before != null) {
            return task.getTaskWithRecordsBefore(before);
        } else {
            return task.getTaskWithRecordsAfter(after);
        }
    }

    public TaskList getFilteredTasksWithRecordsAfter(Date after) {
        return new TaskList(taskListProvider, this.before, after);
    }

    public TaskList getFilteredTasksWithRecordsBefore(Date before) {
        return new TaskList(taskListProvider, before, this.after);
    }
}
