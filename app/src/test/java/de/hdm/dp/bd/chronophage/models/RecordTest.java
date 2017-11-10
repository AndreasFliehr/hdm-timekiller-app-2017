package de.hdm.dp.bd.chronophage.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTest {
    Record record;

    @Before
    public void setUp() {
        record = new Record();
    }

    @Test
    public void start_newRecord_throwsNoException() throws Exception {
        record.start();
    }

    @Test(expected = Exception.class)
    public void start_recordAlreadyStarted_throwsException() throws Exception {
        record.start();
        record.start();
    }

    @Test(expected = Exception.class)
    public void start_recordAlreadyStopped_throwsNoException() throws Exception {
        record.start();
        record.stop();
        record.start();
    }

    @Test(expected = Exception.class)
    public void stop_newRecord_throwsException() throws Exception {
        record.stop();
    }

    @Test
    public void stop_recordStarted_throwsNoException() throws Exception {
        record.start();
        record.stop();
    }

    @Test(expected = Exception.class)
    public void stop_recordStopped_throwsException() throws Exception {
        record.start();
        record.stop();
        record.stop();
    }

    @Test(expected = Exception.class)
    public void getDuration_newRecord_throwsException() throws Exception {
        record.getDuration();
    }

    @Test(expected = Exception.class)
    public void getDuration_recordStartedButNotStopped_throwsException() throws Exception {
        record.start();
        record.getDuration();
    }

    @Test
    public void getDuration_recordStartedThenStopped_returnsDuration() throws Exception {
        record.start();
        record.stop();
        record.getDuration();
    }

}