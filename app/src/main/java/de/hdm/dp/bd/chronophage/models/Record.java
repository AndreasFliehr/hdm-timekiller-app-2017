package de.hdm.dp.bd.chronophage.models;

import java.util.Date;

public class Record {
    private long id;
    private Date start;
    private Date end;

    public void start() throws RecordException {
        if (start != null) {
            throw new RecordException("StartWasNotSet");
        } else if (end != null) {
            throw new RecordException("EndWasAlreadySet");
        }

        start = new Date(System.currentTimeMillis());
    }

    public void stop() throws RecordException {
        if (start == null) {
            throw new RecordException("StartWasNotSet");
        } else if (end != null) {
            throw new RecordException("StartWasAlreadySet");
        }

        end = new Date(System.currentTimeMillis());
    }

    public long getId() {
        return id;
    }

    public long getDuration() throws RecordException {
        if (start == null) {
            throw new RecordException("StartWasNotSet");
        } else if (end == null) {
            throw new RecordException("EndWasNotSet");
        }

        return end.getTime() - start.getTime();
    }
}

class RecordException extends Exception {
    public RecordException(String message) {
        super(message);
    }
}