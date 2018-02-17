package de.hdm.dp.bd.chronophage.models.db;

import de.hdm.dp.bd.chronophage.models.Task;

import java.util.List;

public interface TaskListProvider {
    List<Task> getAllTasks();

    List<Task> getAllRecordLessTasks();

    void updateTasksRecords(Task task);
}
