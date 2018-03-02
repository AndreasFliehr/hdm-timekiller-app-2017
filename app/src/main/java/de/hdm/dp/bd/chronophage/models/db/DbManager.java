package de.hdm.dp.bd.chronophage.models.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DbManager extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;

    /**
     * Suppress FingBug-Warning that can't be fixed with Android-Api Version 23.
     * In Android-Versions higher than 23 use Roboelectric-framework to change database-name.
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(
        value = "MS_SHOULD_BE_FINAL",
        justification = "Can't be fixed in Android-API 23"
    )
    public static String DATABASE_NAME = "example.db";
    public static final List<String> ALL_TASK_NAMES = Collections.unmodifiableList(
        new ArrayList<String>() {
            {
                add("Internet");
                add("Lesen");
                add("Mails");
                add("Putzen");
                add("Spielen");
                add("Vorlesungen");
            }
        });

    public DbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbStatements.SQL_CREATE_TASK_TABLE);
        sqLiteDatabase.execSQL(DbStatements.SQL_CREATE_TASK_DURATION);
        insertInitialValues(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void insertInitialValues(SQLiteDatabase db) {
        for (String name : ALL_TASK_NAMES) {
            ContentValues values = new ContentValues();
            values.put(DbStatements.COLUMN_NAME_TITLE, name);
            db.insert(
                DbStatements.TABLE_NAME_TASK,
                DbStatements.COLUMN_NAME_TITLE,
                values);
        }
    }
}
