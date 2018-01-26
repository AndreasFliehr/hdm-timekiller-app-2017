package de.hdm.dp.bd.chronophage.models.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

    public ArrayList<String> getTaskNames(Context context) {
        dbManager = createDbManager(context);
        ArrayList<String> result = new ArrayList();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor c = db.query(
                DbStatements.TABLE_NAME_TASK,  // The table to query
                getProjection(DbStatements.COLUMN_NAME_TITLE),       // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                getSortOrder(DbStatements.COLUMN_NAME_TITLE, DbStatements.ASC) // The sort order
        );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_TITLE));
            result.add(name);
            c.moveToNext();
        }
        c.close();
        db.close();
        return result;
    }
}
