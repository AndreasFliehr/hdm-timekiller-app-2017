package de.hdm.dp.bd.chronophage.models;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdm.dp.bd.chronophage.models.db.TaskListProvider;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class TaskListTest {
    private Task taskMock;
    private TaskList taskList;
    private TaskListProviderMock taskListProvider;

    @Before
    public void setUp() throws Exception {
        taskMock = Mockito.mock(Task.class);
        taskListProvider = new TaskListProviderMock();
        taskList = new TaskList(taskListProvider);
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
    public void setTaskInactive_newTaskList_updateCalled() throws Exception {
        taskList.setTaskInactive(taskMock);
        assertTrue(taskListProvider.updateCalled);
    }

    @Test
    public void isTaskActive_newTaskList_isTrue() throws Exception {
        assertTrue(taskList.isTaskActive(taskMock));
    }

    @Test
    public void getAllTasksWithRecords_oneTaskWithRecordsOneWithout_returnsListWithTaskWithRecord() {
        //test setup
        Task withRecords = Mockito.mock(Task.class);
        doReturn(1L).when(withRecords).getOverallDuration();
        Task withoutRecords = Mockito.mock(Task.class);
        doReturn(0L).when(withoutRecords).getOverallDuration();
        taskListProvider.taskList.add(withoutRecords);
        taskListProvider.taskList.add(withRecords);
        //execute test
        final List<Task> tasksWithRecords = taskList.getAllTasksWithRecords();
        assertNotNull(tasksWithRecords);
        assertTrue(tasksWithRecords.contains(withRecords));
        assertFalse(tasksWithRecords.contains(withoutRecords));
    }

    @Test
    public void getFilteredTasksWithRecordsAfter_oneTasksWIthRecordsAfterDateOneWithOnesBefore_returnsListWithTaskWithRecord() {
        //testSetup 1: setup Tasks that will be returned after filtering
        Task withRecords = Mockito.mock(Task.class);
        doReturn(1L).when(withRecords).getOverallDuration();
        Task withoutRecords = Mockito.mock(Task.class);
        doReturn(0L).when(withoutRecords).getOverallDuration();
        //testSetup 2: setup Tasks that are called with the filtering method
        Task withRecordsAfter = Mockito.mock(Task.class);
        doReturn(withRecords).when(withRecordsAfter).getTaskWithRecordsAfter((Date) any());
        Task withoutRecordsAfter = Mockito.mock(Task.class);
        doReturn(withoutRecords).when(withoutRecordsAfter).getTaskWithRecordsAfter((Date) any());
        Date testDate = Date.from(Instant.now());
        taskListProvider.taskList.add(withRecordsAfter);
        taskListProvider.taskList.add(withoutRecordsAfter);
        //executeTest
        final List<Task> tasksWithRecords = taskList.getFilteredTasksWithRecordsAfter(testDate).getAllTasksWithRecords();
        assertNotNull(tasksWithRecords);
        assertTrue(tasksWithRecords.contains(withRecords));
        assertFalse(tasksWithRecords.contains(withoutRecords));
    }

    @Test
    public void getFilteredTasksWithRecordsAfter_noTasksWithRecordsAfter_returnsEmptyList() {
        //testSetup
        Task withoutRecords = Mockito.mock(Task.class);
        doReturn(0L).when(withoutRecords).getOverallDuration();
        Task withoutRecordsAfter = Mockito.mock(Task.class);
        doReturn(withoutRecords).when(withoutRecordsAfter).getTaskWithRecordsAfter((Date) any());
        Date testDate = Date.from(Instant.now());
        taskListProvider.taskList.add(withoutRecordsAfter);
        //executeTest
        final List<Task> tasksWithRecords = taskList.getFilteredTasksWithRecordsAfter(testDate).getAllTasksWithRecords();
        assertNotNull(tasksWithRecords);
        assertTrue(tasksWithRecords.isEmpty());
    }

    @Test
    public void getFilteredTasksWithRecordsBefore_oneTasksWIthRecordsAfterDateOneWithOnesBefore_returnsListWithTaskWithRecord() {
        //testSetup 1: setup Tasks that will be returned after filtering
        Task withRecords = Mockito.mock(Task.class);
        doReturn(1L).when(withRecords).getOverallDuration();
        Task withoutRecords = Mockito.mock(Task.class);
        doReturn(0L).when(withoutRecords).getOverallDuration();
        //testSetup 2: setup Tasks that are called with the filtering method
        Task withRecordsBefore = Mockito.mock(Task.class);
        doReturn(withRecords).when(withRecordsBefore).getTaskWithRecordsBefore((Date) any());
        Task withoutRecordsBefore = Mockito.mock(Task.class);
        doReturn(withoutRecords).when(withoutRecordsBefore).getTaskWithRecordsBefore((Date) any());
        Date testDate = Date.from(Instant.now());
        taskListProvider.taskList.add(withRecordsBefore);
        taskListProvider.taskList.add(withoutRecordsBefore);
        //executeTest
        final List<Task> tasksWithRecords = taskList.getFilteredTasksWithRecordsBefore(testDate).getAllTasksWithRecords();
        assertNotNull(tasksWithRecords);
        assertTrue(tasksWithRecords.contains(withRecords));
        assertFalse(tasksWithRecords.contains(withoutRecords));
    }

    @Test
    public void getFilteredTasksWithRecordsBefore_noTasksWithRecordsBefore_returnsEmptyList() {
        //testSetup
        Task withoutRecords = Mockito.mock(Task.class);
        doReturn(0L).when(withoutRecords).getOverallDuration();
        Task withoutRecordsBefore = Mockito.mock(Task.class);
        doReturn(withoutRecords).when(withoutRecordsBefore).getTaskWithRecordsBefore((Date) any());
        Date testDate = Date.from(Instant.now());
        taskListProvider.taskList.add(withoutRecordsBefore);
        //executeTest
        final List<Task> tasksWithRecords = taskList.getFilteredTasksWithRecordsBefore(testDate).getAllTasksWithRecords();
        assertNotNull(tasksWithRecords);
        assertTrue(tasksWithRecords.isEmpty());
    }

    private class TaskListProviderMock implements TaskListProvider {
        private final List<Task> taskList = new ArrayList<>();
        private boolean updateCalled = false;

        @Override
        public List<Task> getAllTasks() {
            return taskList;
        }

        @Override
        public List<Task> getAllRecordLessTasks() {
            return taskList;
        }

        @Override
        public void updateTasksRecords(Task task) {
            this.updateCalled = true;
        }
    }
}