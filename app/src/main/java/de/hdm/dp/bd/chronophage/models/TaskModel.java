package de.hdm.dp.bd.chronophage.models;

import java.util.ArrayList;
import java.util.List;

public class TaskModel {
    private long id;
    private String name;
    private boolean active;
    private RecordModel activeRecord = null;
    private List<RecordModel> records = new ArrayList<>();

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
        active = true;
        activeRecord = new RecordModel();
    }

    public void stop() {
        active = false;
        records.add(activeRecord);
        activeRecord = null;
    }

    public long getOverallDuration() {
        long overallDuration = 0;
        for (RecordModel record : records) {
            overallDuration += record.getDuration();
        }
        return overallDuration;
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
