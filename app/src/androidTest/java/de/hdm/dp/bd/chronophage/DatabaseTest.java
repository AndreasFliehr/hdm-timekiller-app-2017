package de.hdm.dp.bd.chronophage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.BeforeClass;
import org.junit.Test;

import de.hdm.dp.bd.chronophage.models.TaskList;
import de.hdm.dp.bd.chronophage.models.db.DbManager;
import de.hdm.dp.bd.chronophage.models.db.TaskListProviderDbImpl;

import static org.junit.Assert.assertFalse;

public class DatabaseTest {
    private DbManager dbManager;
    private final Context targetContext = InstrumentationRegistry.getTargetContext();

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
}
