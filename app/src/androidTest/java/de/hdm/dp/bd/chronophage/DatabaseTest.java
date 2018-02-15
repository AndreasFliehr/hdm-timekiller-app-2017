package de.hdm.dp.bd.chronophage;

import android.content.Context;
import android.database.Cursor;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hdm.dp.bd.chronophage.models.Task;
import de.hdm.dp.bd.chronophage.models.TaskList;
import de.hdm.dp.bd.chronophage.models.db.DbManager;
import de.hdm.dp.bd.chronophage.models.db.TaskListProviderDbImpl;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DatabaseTest {
    private DbManager dbManager;
    private final Context targetContext = getTargetContext();

    @BeforeClass
    public void setUp() {
        DbManager.DATABASE_NAME = "test.db";
        dbManager = new DbManager(targetContext);
    }

    @Test
    public void dummyTest_taskListInitialized() {
        TaskList taskList = new TaskList(new TaskListProviderDbImpl(targetContext));
        assertFalse(taskList.getAllTasks().isEmpty());
    }

    @Test
    public void testGetTask_BackDoor() {
        Task task = dbCalls.getTask(getTargetContext(), "Lesen");
        String[] taskValues = getTaskValues("Lesen");
        assertEquals(taskValues[0], String.valueOf(task.getId()));
        assertEquals(taskValues[1], taskask.getName());
    }

    private String[] getTaskValues(String taskName) {
        String[] result = new String[2];
        Cursor c = db.rawQuery("SELECT * FROM tasks WHERE " +
            "name = '" + taskName + "'", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            result[0] = String.valueOf(c.getLong(c.getColumnIndexOrThrow("_id")));
            result[1] = c.getString(c.getColumnIndexOrThrow("name"));
            c.moveToNext();
        }
        c.close();
        return result;
    }
}
