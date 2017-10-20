package de.hdm.dp.bd.chronophage.database;

import de.hdm.dp.bd.chronophage.models.TaskListModel;

public class TaskDatabaseInMemoryMock {
    public static TaskListModel taskListModel = new TaskListModel();

    private TaskDatabaseInMemoryMock() {
        taskListModel.createTaskList();
    };
}
