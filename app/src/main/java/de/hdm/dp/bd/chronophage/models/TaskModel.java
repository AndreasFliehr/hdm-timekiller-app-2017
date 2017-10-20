package de.hdm.dp.bd.chronophage.models;

public class TaskModel {
    private long id;
    private String name;
    private boolean active;

    public TaskModel(long id, String name) {
        this.id = id;
        this.name = name;
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void start() {

    }

    public void stop() {

    }

    public long getOverallDuration() {

    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
