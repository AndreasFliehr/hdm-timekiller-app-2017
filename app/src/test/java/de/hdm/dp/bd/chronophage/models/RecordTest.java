package de.hdm.dp.bd.chronophage.models;

import org.junit.Before;
import org.junit.Test;


public class RecordTest {
    private Record record;

    @Before
    public void setUp() {
        record = new Record();
    }

    @Test
    public void start_newRecord_executes() {
        record.start();
    }

    @Test(expected = Exception.class)
    public void start_recordAlreadyStarted_throwsException() {
        record.start();
        record.start();
    }

    @Test(expected = Exception.class)
    public void start_recordAlreadyStopped_throwsException() {
        record.start();
        record.stop();
        record.start();
    }

    @Test(expected = Exception.class)
    public void stop_newRecord_throwsException() {
        record.stop();
    }

    @Test
    public void stop_recordStarted_executes() {
        record.start();
        record.stop();
    }

    @Test(expected = Exception.class)
    public void stop_recordStopped_throwsException() {
        record.start();
        record.stop();
        record.stop();
    }

    @Test(expected = Exception.class)
    public void getDuration_newRecord_throwsException() {
        record.getDuration();
    }

    @Test(expected = Exception.class)
    public void getDuration_recordStartedButNotStopped_throwsException() {
        record.start();
        record.getDuration();
    }

    @Test
    public void getDuration_recordStartedThenStopped_returnsDuration() {
        record.start();
        record.stop();
        record.getDuration();
    }

}