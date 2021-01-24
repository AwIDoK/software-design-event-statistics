package com.awidok.eventstatistics;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EventsStatisticImpl implements EventsStatistic {
    Clock clock;

    Map<String, LinkedList<Instant>> events = new HashMap<>();

    public EventsStatisticImpl(Clock clock) {
        this.clock = clock;
    }

    public void incEvent(String name) {
        Instant time = clock.instant();

        if (!events.containsKey(name)) {
            events.put(name, new LinkedList<>());
        }
        events.get(name).add(time);
        clean(name, time);
    }

    public double getEventStatisticByName(String name) {
        Instant time = clock.instant();
        return getEventStatisticByName(name, time);
    }

    public Map<String, Double> getAllEventStatistic() {
        Instant time = clock.instant();
        Map<String, Double> result = new HashMap<>();
        for (String name : events.keySet()) {
            result.put(name, getEventStatisticByName(name, time));
        }
        return result;
    }

    public void printStatistic() {
        System.out.println("Statistics:");
        for (Map.Entry<String, Double> entry : getAllEventStatistic().entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + String.format("%.2f", entry.getValue()) + " rpm");
        }
    }

    double getEventStatisticByName(String name, Instant time) {
        clean(name, time);
        if (!events.containsKey(name)) {
            return 0;
        }
        return events.get(name).size() / 60.;
    }

    private void clean(String name, Instant time) {
        if (events.containsKey(name)) {
            LinkedList<Instant> nameEvents = events.get(name);
            while (!nameEvents.isEmpty() && Duration.between(nameEvents.peekFirst(), time).compareTo(Duration.ofHours(1)) >= 0) {
                nameEvents.remove();
            }
        }
    }
}