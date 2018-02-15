package de.hdm.dp.bd.chronophage.models.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DbManager extends SQLiteOpenHelper {
    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "example.db";
    public static final ArrayList<String> ALL_TASK_NAMES = new ArrayList<String>() {{
        add("Internet");
        add("Lesen");
        add("Mails");
        add("Putzen");
        add("Spielen");
        add("Vorlesungen");
    }};

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
