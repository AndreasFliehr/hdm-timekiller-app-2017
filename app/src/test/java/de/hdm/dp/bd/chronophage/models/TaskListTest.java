package de.hdm.dp.bd.chronophage.models;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class TaskListTest {
    private Task taskMock;
    private TaskList taskList;

    @Before
    public void setUp() throws Exception {
        taskMock = Mockito.mock(Task.class);
        taskList = new TaskList();
        doReturn(true).when(taskMock).isActive();
    }

    @Test
    public void setTaskActive_newTaskList_startsTask() throws Exception {
        taskList.setTaskActive(taskMock);
        verify(taskMock).start();
    }

    @Test
    public void setTaskInactive_newTaskList_stopsTask() throws Exception {
        taskList.setTaskInactive(taskMock);
        verify(taskMock).stop();
    }

    @Test
    public void isTaskActive_newTaskList_isTrue() throws Exception {
        assertTrue(taskList.isTaskActive(taskMock));
    }

    @Test
    public void getAllTasksWithRecords_oneTaskWithRecordsOneWithout_returnsListWithTaskWithRecord() {
        //test setup
        TaskList test = new TaskList();
        Task withRecords = Mockito.mock(Task.class);
        doReturn(1L).when(withRecords).getOverallDuration();
        Task withoutRecords = Mockito.mock(Task.class);
        doReturn(0L).when(withoutRecords).getOverallDuration();
        test.getAllTasks().add(withoutRecords);
        test.getAllTasks().add(withRecords);
        //execute test
        final List<Task> tasksWithRecords = test.getAllTasksWithRecords();
        assertNotNull(tasksWithRecords);
        assertTrue(tasksWithRecords.contains(withRecords));
        assertFalse(tasksWithRecords.contains(withoutRecords));
    }

}