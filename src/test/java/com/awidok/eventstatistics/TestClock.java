package com.awidok.eventstatistics;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class TestClock extends Clock {
    Instant time;

    public TestClock(Instant time) {
        this.time = time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    @Override
    public ZoneId getZone() {
        return null;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return null;
    }

    @Override
    public Instant instant() {
        return time;
    }
}
