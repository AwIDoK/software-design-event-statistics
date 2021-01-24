package com.awidok.eventstatistics;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventsStatisticTest {
    @Test
    public void noEvents() {
        Instant time = Instant.parse("2021-01-20T10:00:00.000Z");
        TestClock clock = new TestClock(time);
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);
        assertEquals(eventsStatistic.getAllEventStatistic().size(), 0);
        assertEquals(eventsStatistic.getEventStatisticByName("name"), 0);
    }

    @Test
    public void singleEvent() {
        Instant time = Instant.parse("2021-01-20T10:00:00.000Z");
        TestClock clock = new TestClock(time);
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);
        eventsStatistic.incEvent("name");
        assertEquals(eventsStatistic.getAllEventStatistic().size(), 1);
        assertEquals(eventsStatistic.getAllEventStatistic().get("name"), 1 / 60.);
        assertEquals(eventsStatistic.getEventStatisticByName("name"), 1 / 60.);
        assertEquals(eventsStatistic.getEventStatisticByName("name2"), 0);
    }

    @Test
    public void eventDisappearsAfterHour() {
        Instant time = Instant.parse("2021-01-20T10:00:00.000Z");
        TestClock clock = new TestClock(time);
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);
        eventsStatistic.incEvent("name");
        time = time.plus(59, ChronoUnit.MINUTES);
        clock.setTime(time);
        assertEquals(eventsStatistic.getAllEventStatistic().get("name"), 1 / 60.);
        time = time.plus(1, ChronoUnit.HOURS);
        clock.setTime(time);
        assertEquals(eventsStatistic.getAllEventStatistic().get("name"), 0);
    }

    @Test
    public void multipleEvents() {
        Instant time = Instant.parse("2021-01-20T10:00:00.000Z");
        TestClock clock = new TestClock(time);
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);
        eventsStatistic.incEvent("name");
        eventsStatistic.incEvent("name");
        eventsStatistic.incEvent("name2");

        time = time.plus(10, ChronoUnit.MINUTES);
        clock.setTime(time);
        eventsStatistic.incEvent("name");
        eventsStatistic.incEvent("name");

        time = time.plus(30, ChronoUnit.MINUTES);
        clock.setTime(time);
        eventsStatistic.incEvent("name2");
        eventsStatistic.incEvent("name2");
        eventsStatistic.incEvent("name2");
        eventsStatistic.incEvent("name3");

        time = time.plus(20, ChronoUnit.MINUTES);
        clock.setTime(time);

        assertEquals(eventsStatistic.getAllEventStatistic().get("name"), 2 / 60.);
        assertEquals(eventsStatistic.getAllEventStatistic().get("name2"), 3 / 60.);
        assertEquals(eventsStatistic.getAllEventStatistic().get("name3"), 1 / 60.);
    }
}