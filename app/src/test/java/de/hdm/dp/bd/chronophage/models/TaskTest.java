package de.hdm.dp.bd.chronophage.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskTest {
    private Task task;

    @Before
    public void setUp() {
        task = new Task(0,"Test");
    }

    @Test
    public void start_newTask_executes() {
        task.start();
    }

    @Test (expected = Exception.class)
    public void start_taskAlreadyStarted_throwsException() {
        task.start();
        task.start();
    }

    @Test
    public void stop_taskAlreadyStarted_executes() {
        task.start();
        task.stop();
    }

    @Test (expected = Exception.class)
    public void stop_taskNotYetStarted_throwsException() {
        task.stop();
    }

    @Test
    public void getOverallDuration_newTask_returnsZero() {
        Assert.assertEquals(0,task.getOverallDuration());
    }

    @Test
    public void getOverallDuration_taskHasRecord_returnsMoreThanZero() throws Exception {
        task.start();
        wait(1000);
        task.stop();
        Assert.assertTrue(0 < task.getOverallDuration());
    }
}
