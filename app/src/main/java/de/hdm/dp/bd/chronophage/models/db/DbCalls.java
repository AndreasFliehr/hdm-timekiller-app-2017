package de.hdm.dp.bd.chronophage.models.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdm.dp.bd.chronophage.models.Record;
import de.hdm.dp.bd.chronophage.models.Task;

/**
 * Created by Barbara on 16.01.2017.
 */
public class DbCalls {

    private DbManager dbManager;

    DbManager createDbManager(Context context) {
        return new DbManager(context);
    }

    private String[] getProjection(String... columns) {
        return columns;
    }

    private String getSortOrder(String column, String sort) {
        return column + " " + sort;
    }

    public ArrayList<Task> getTaskObjects(Context context) {
        dbManager = createDbManager(context);
        ArrayList<Task> result = new ArrayList<>();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor c = db.query(
            DbStatements.TABLE_NAME_TASK, // The table to query
            getProjection(DbStatements._ID, DbStatements.COLUMN_NAME_TITLE), // The columns to return
            null,                                      // The columns for the WHERE clause
            null,                                  // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            getSortOrder(DbStatements.COLUMN_NAME_TITLE, DbStatements.ASC) // The sort order
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

        ArrayList<Task> listOfAllTasks = getTaskObjects(context);
        dbManager = createDbManager(context);
        ArrayList<Task> result = new ArrayList();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor c = db.query(
            DbStatements.TABLE_NAME_DURATION, // The table to query
            getProjection(DbStatements.COLUMN_NAME_TASKID,
                DbStatements.COLUMN_NAME_START,
                DbStatements.COLUMN_NAME_END,
                DbStatements.COLUMN_NAME_DURATION), // The columns to return
            null,                                      // The columns for the WHERE clause
            null,                                  // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            null
        );
        Map<Long, ArrayList<Record>> records = new HashMap<>();
        for(Task task: listOfAllTasks) {
            records.put(task.getId(), new ArrayList<Record>());
        }

        c.moveToFirst();
        while (!c.isAfterLast()) {
            long taskId = c.getLong(c.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_TASKID));
            Date start = new Date(c.getLong(c.getColumnIndexOrThrow((DbStatements.COLUMN_NAME_START))));
            Date end = new Date(c.getLong(c.getColumnIndexOrThrow((DbStatements.COLUMN_NAME_END))));
            Record record = new Record(start,end);
            records.get(taskId).add(record);
            c.moveToNext();
        }
        c.close();
        db.close();

        for(Task task: listOfAllTasks) {
            result.add(new Task(task.getId(), task.getName(), records.get(task.getId())));
        }

        return result;
    }
}
