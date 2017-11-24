package de.hdm.dp.bd.chronophage.models;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

}