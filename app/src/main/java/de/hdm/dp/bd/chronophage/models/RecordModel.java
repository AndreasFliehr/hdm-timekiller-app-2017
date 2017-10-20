package de.hdm.dp.bd.chronophage.models;

import java.util.Date;

public class RecordModel {
    private long id;
    private Date start;
    private Date end;

    public void start() {
        start = new Date(System.currentTimeMillis());
    }

    public void stop() {
        end = new Date(System.currentTimeMillis());
    }

    public long getId() {
        return id;
    }

    public long getDuration() {
        return end.getTime() - start.getTime();
    }
}
