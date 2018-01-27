package de.hdm.dp.bd.chronophage.models;

import android.content.ContentValues;

import java.util.Date;

import de.hdm.dp.bd.chronophage.models.db.DbStatements;

public class Record {
    private long id;
    private Date start;
    private Date end;

    public Record(long id, Date start, Date end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    Record() {

    }

    public void start() {
        if (start != null) {
            throw new RecordException("StartWasNotSet");
        } else if (end != null) {
            throw new RecordException("EndWasAlreadySet");
        }

        start = new Date(System.currentTimeMillis());
    }

    public void stop() {
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

    public long getDuration() {
        if (start == null) {
            throw new RecordException("StartWasNotSet");
        } else if (end == null) {
            throw new RecordException("EndWasNotSet");
        }

        return end.getTime() - start.getTime();
    }

    public boolean startsAfter(Date date) {
        return start.after(date);
    }

    public boolean endsBefore(Date date) {
        return end.before(date);
    }

    public ContentValues toContentValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DbStatements.COLUMN_NAME_START, start.getTime());
        contentValues.put(DbStatements.COLUMN_NAME_END, end.getTime());
        contentValues.put(DbStatements.COLUMN_NAME_DURATION, getDuration());
        return contentValues;
    }

    public class RecordException extends RuntimeException {
        private RecordException(String message) {
            super(message);
        }
    }
}