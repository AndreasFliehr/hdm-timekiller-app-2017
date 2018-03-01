package de.hdm.dp.bd.chronophage.models;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.time.Instant;
import java.util.Date;
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

    @Test
    public void startsAfter_startedAfterGivenDate_returnsTrue() throws Exception {
        Date now = Date.from(Instant.now());
        Thread.sleep(100);
        record.start();
        assertTrue(record.startsAfter(now));
    }

    @Test
    public void startsAfter_startedBeforeGivenDate_returnsFalse() throws Exception {
        record.start();
        Thread.sleep(100);
        Date now = Date.from(Instant.now());
        assertFalse(record.startsAfter(now));
    }

    @Test
    public void endsBefore_endedBeforeGivenDate_returnsTrue() throws Exception {
        record.start();
        record.stop();
        Thread.sleep(100);
        Date now = Date.from(Instant.now());
        assertTrue(record.endsBefore(now));
    }

    @Test
    public void endsBefore_endedAfterGivenDate_returnsFalse() throws Exception {
        record.start();
        Date now = Date.from(Instant.now());
        Thread.sleep(100);
        record.stop();
        assertFalse(record.endsBefore(now));
    }
}