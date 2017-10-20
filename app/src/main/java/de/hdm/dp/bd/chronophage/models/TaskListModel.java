package de.hdm.dp.bd.chronophage.models;

import java.util.ArrayList;
import java.util.List;

public class TaskListModel {

    private List<TaskModel> tasks;

    public TaskListModel() {

    }

    public void setTaskActive(TaskModel taskModel) {
        taskModel.start();
    }

    public void setTaskInactive(TaskModel taskModel) {
        taskModel.stop();
    }

    public boolean isTaskActive(TaskModel taskModel) {
        return taskModel.isActive();
    }

    public List<TaskModel> getAllTasks() {
        return tasks;
    }

    void createTaskList(){
        tasks = new ArrayList<>();
        tasks.add(new TaskModel(1, "Internet"));
    }
}
