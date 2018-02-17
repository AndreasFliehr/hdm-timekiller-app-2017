package de.hdm.dp.bd.chronophage.models.db;

import android.content.Context;

import de.hdm.dp.bd.chronophage.models.Task;

import java.util.List;

public class TaskListProviderDbImpl implements TaskListProvider {
    private final Context context;
    private final DbCalls dbCalls;

    public TaskListProviderDbImpl(Context context) {
        this.context = context;
        this.dbCalls = new DbCalls();
    }

    @Override
    public List<Task> getAllTasks() {
        return dbCalls.getTasksWithRecords(context);
    }

    @Override
    public List<Task> getAllRecordLessTasks() {
        return dbCalls.getTasksWithoutRecords(context);
    }

    @Override
    public void updateTasksRecords(Task task) {
        dbCalls.updateTasksRecords(task, context);
    }
}
