package de.hdm.dp.bd.chronophage.models.db;

import java.util.List;

import de.hdm.dp.bd.chronophage.models.Task;

public interface TaskListProvider {
    List<Task> getAllTasks();

    List<Task> getAllRecordLessTasks();

    void updateTasksRecords(Task task);
}
