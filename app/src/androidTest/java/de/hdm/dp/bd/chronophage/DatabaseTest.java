package de.hdm.dp.bd.chronophage;

import android.content.Context;
import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import de.hdm.dp.bd.chronophage.models.Record;
import de.hdm.dp.bd.chronophage.models.Task;
import de.hdm.dp.bd.chronophage.models.db.DbCalls;
import de.hdm.dp.bd.chronophage.models.db.DbManager;
import de.hdm.dp.bd.chronophage.models.db.DbStatements;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseTest {
    private DbManager dbManager = new DbManager(TARGET_CONTEXT);
    private DbCalls dbCalls = new DbCalls();
    private static final Context TARGET_CONTEXT = getTargetContext();
    private final Date start = new Date(1000);

    @BeforeClass
    public static void initTestDb() {
        DbManager.DATABASE_NAME = "test.db";
        new DbManager(TARGET_CONTEXT);
    }

    @Before
    @After
    public void clearRecords() {
        dbManager.getWritableDatabase().delete(DbStatements.TABLE_NAME_DURATION, null, null);
    }

    /**
     * We test reading of all tasks based on the Stammdaten that are inserted by DbManager using
     * classes we can assume to work (SQLiteHelper). This should suffice, as the data insertion
     * is not done with custom methods and only once on first startup of the app
     */
    @Test
    public void getTasksWithoutRecords_dbFreshlyInitialized_readsAllTasksWithCorrectNames() {
        final ArrayList<Task> tasksWithoutRecords = dbCalls.getTasksWithoutRecords(TARGET_CONTEXT);
        for (Task task : tasksWithoutRecords) {
            assertTrue(DbManager.ALL_TASK_NAMES.contains(task.getName()));
        }
    }

    @Test
    public void updateTasksRecords_taskWithOneRecord_correctFieldsAreInserted() {
        //setup: create record with a given duration and insert it into any task
        final Date end = new Date(start.getTime() + (long) 15_000);
        final Record toInsert = new Record(0, start, end);
        final Task anyTask = dbCalls.getTasksWithoutRecords(TARGET_CONTEXT).get(0);
        final Task forRecord = new Task(anyTask.getId(), anyTask.getName(), new ArrayList<Record>() {{
            add(toInsert);
        }});
        //tested method: insert newest record (toInsert) into db
        dbCalls.updateTasksRecords(forRecord, TARGET_CONTEXT);
        //assert: via backdoor access, that the specific record above was inserted
        final Cursor c = getMostRecentRecordFor(anyTask);
        c.moveToFirst();
        final long duration = c.getLong(c.getColumnIndexOrThrow(DbStatements.COLUMN_NAME_DURATION));
        Date foundStart = new Date(c.getLong(c.getColumnIndexOrThrow((DbStatements.COLUMN_NAME_START))));
        Date foundEnd = new Date(c.getLong(c.getColumnIndexOrThrow((DbStatements.COLUMN_NAME_END))));
        assertEquals(toInsert.getDuration(), duration);
        assertEquals(start, foundStart);
        assertEquals(end, foundEnd);
    }

    @Test
    public void getTasksWithRecords_tasksDoNotHaveRecords_overallDurationForEachZero() {
        final ArrayList<Task> tasks = dbCalls.getTasksWithRecords(TARGET_CONTEXT);
        for (Task task : tasks) {
            assertEquals(0, task.getOverallDuration());
        }
    }

    @Test
    public void getTasksWithRecords_oneTasksHasRecords_itsDurationIsGreaterZero() {
        final Task anyTask = dbCalls.getTasksWithRecords(TARGET_CONTEXT).get(0);
        final long expected_duration = 15_000;
        addDurationToTask(anyTask, expected_duration);
        final Task reloaded = dbCalls.getTasksWithRecords(TARGET_CONTEXT).get(0);
        assertEquals(expected_duration, reloaded.getOverallDuration());
    }

    private void addDurationToTask(Task anyTask, long duration) {
        final Date end = new Date(start.getTime() + duration);
        final Record toInsert = new Record(0, start, end);
        final Task forRecord = new Task(anyTask.getId(), anyTask.getName(), new ArrayList<Record>() {{
            add(toInsert);
        }});
        dbCalls.updateTasksRecords(forRecord, TARGET_CONTEXT);
    }

    private Cursor getMostRecentRecordFor(Task task) {
        return dbManager.getReadableDatabase().rawQuery("SELECT * FROM " +
                        DbStatements.TABLE_NAME_DURATION + " WHERE " +
                        DbStatements.COLUMN_NAME_TASKID + " = ?" +
                        " ORDER BY " + DbStatements._ID + " DESC LIMIT 1",
                new String[]{"" + task.getId()});
    }

}
