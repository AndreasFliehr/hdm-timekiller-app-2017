package de.hdm.dp.bd.chronophage.models;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;


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

    @Test
    public void startsAfter_startedAfterGivenDate_returnsTrue() {
        Date now = Date.from(Instant.now());
        record.start();
        assertTrue(record.startsAfter(now));
    }

    @Test
    public void startsAfter_startedBeforeGivenDate_returnsFalse() {
        record.start();
        Date now = Date.from(Instant.now());
        assertFalse(record.startsAfter(now));
    }

    @Test
    public void endsBefore_endedBeforeGivenDate_returnsTrue() {
        record.start();
        Date now = Date.from(Instant.now());
        record.stop();
        assertTrue(record.endsBefore(now));
    }

    @Test
    public void endsBefore_endedAfterGivenDate_returnsFalse() {
        record.start();
        record.stop();
        Date now = Date.from(Instant.now());
        assertFalse(record.endsBefore(now));
    }
}