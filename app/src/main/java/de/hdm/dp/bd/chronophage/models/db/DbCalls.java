package de.hdm.dp.bd.chronophage.models.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import de.hdm.dp.bd.chronophage.models.Record;
import de.hdm.dp.bd.chronophage.models.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DbCalls {

    private DbManager dbManager;

    private DbManager createDbManager(Context context) {
        return new DbManager(context);
    }

    private String[] getProjection(String... columns) {
        return columns;
    }

    private String getSortOrder(String column, String sort) {
        return column + " " + sort;
    }

    public ArrayList<Task> getTasksWithoutRecords(Context context) {
        dbManager = createDbManager(context);
        ArrayList<Task> result = new ArrayList<>();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor c = db.query(
            DbStatements.TABLE_NAME_TASK, // The table to query
            // The columns to return
            getProjection(DbStatements._ID, DbStatements.COLUMN_NAME_TITLE),
            null,          // The columns for the WHERE clause
            null,      // The values for the WHERE clause
            null,          // don't group the rows
            null,           // don't filter by row groups
            getSortOrder(DbStatements.COLUMN_NAME_TITLE, DbStatements.ASC)
        );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_TITLE));
            long id = c.getLong(c.getColumnIndexOrThrow(DbStatements._ID));
            result.add(new Task(id, name));
            c.moveToNext();
        }
        c.close();
        db.close();
        return result;
    }

    public ArrayList<Task> getTasksWithRecords(Context context) {

        ArrayList<Task> listOfAllTasks = getTasksWithoutRecords(context);
        dbManager = createDbManager(context);
        ArrayList<Task> result = new ArrayList<>();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor c = db.query(
            DbStatements.TABLE_NAME_DURATION, // The table to query
            getProjection(
                DbStatements._ID,
                DbStatements.COLUMN_NAME_TASKID,
                DbStatements.COLUMN_NAME_START,
                DbStatements.COLUMN_NAME_END), // The columns to return
            null,                                      // The columns for the WHERE clause
            null,                                  // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            null
        );
        Map<Long, ArrayList<Record>> records = new HashMap<>();
        for (Task task : listOfAllTasks) {
            records.put(task.getId(), new ArrayList<Record>());
        }

        c.moveToFirst();
        while (!c.isAfterLast()) {
            long taskId = c.getLong(c.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_TASKID));
            long id = c.getLong(c.getColumnIndexOrThrow(DbStatements._ID));
            Date start = new Date(c.getLong(c.getColumnIndexOrThrow((
                DbStatements.COLUMN_NAME_START))));
            Date end = new Date(c.getLong(c.getColumnIndexOrThrow((DbStatements.COLUMN_NAME_END))));
            Record record = new Record(id, start, end);
            records.get(taskId).add(record);
            c.moveToNext();
        }
        c.close();
        db.close();

        for (Task task : listOfAllTasks) {
            result.add(new Task(task.getId(), task.getName(), records.get(task.getId())));
        }

        return result;
    }

    public void updateTasksRecords(Task task, Context context) {
        dbManager = createDbManager(context);
        SQLiteDatabase db = dbManager.getWritableDatabase();

        Record toInsert = task.getMostRecentRecord();
        ContentValues contentValues = toInsert.toContentValues();
        contentValues.put(DbStatements.COLUMN_NAME_TASKID, task.getId());

        final long id = db.insert(
            DbStatements.TABLE_NAME_DURATION, null, contentValues
        );
        if (id < 0) {
            throw new IllegalStateException(
                "Insert of record " + toInsert + " for task " + task + " failed!"
            );
        }
    }
}
