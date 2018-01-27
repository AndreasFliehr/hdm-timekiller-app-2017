package de.hdm.dp.bd.chronophage.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public Task(long id, String name, ArrayList<Record> taskWithRecords) {
        this.id = id;
        this.name = name;
        this.active = false;
        records = taskWithRecords;
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
            throw new TaskException("TaskAlreadyStarted");
        }
        active = true;
        activeRecord = new Record();
        activeRecord.start();
    }

    public void stop() {
        if (!active) {
            throw new TaskException("TaskNotYetStarted");
        }
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

    public Task getTaskWithRecordsBefore(Date date) {
        ArrayList<Record> taskWithRecords = new ArrayList<>();

        for (Record record : records) {
            if (record.endsBefore(date)) {
                taskWithRecords.add(record);
            }
        }
        return new Task(this.getId(), this.getName(), taskWithRecords);
    }

    public Task getTaskWithRecordsAfter(Date date) {
        ArrayList<Record> taskWithRecords = new ArrayList<>();

        for (Record record : records) {
            if (record.startsAfter(date)) {
                taskWithRecords.add(record);
            }
        }
        return new Task(this.getId(), this.getName(), taskWithRecords);
    }

    public Record getMostRecentRecord() {
        if (records.isEmpty()) {
            throw new TaskException("No Record to retrieve is in this task");
        } else {
            return records.get(records.size() - 1);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public class TaskException extends RuntimeException {
        private TaskException(String message) {
            super(message);
        }
    }
}
