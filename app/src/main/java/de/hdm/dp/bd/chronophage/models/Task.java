package de.hdm.dp.bd.chronophage.models;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private long id;
    private String name;
    private boolean active;
    private Record activeRecord = null;
    private List<Record> records = new ArrayList<>();

    public Task(long id, String name) {
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
        if (active) {
            throw new TaskAlreadyActiveException();
        }
        active = true;
        activeRecord = new Record();
        activeRecord.start();
    }

    public void stop() {
        active = false;
        activeRecord.stop();
        records.add(activeRecord);
        activeRecord = null;
    }

    public long getOverallDuration() {
        long overallDuration = 0;
        for (Record record : records) {
            overallDuration += record.getDuration();
        }
        return overallDuration;
    }

    @Override
    public String toString() {
        return name;
    }

    private class TaskAlreadyActiveException extends RuntimeException {
    }
}
