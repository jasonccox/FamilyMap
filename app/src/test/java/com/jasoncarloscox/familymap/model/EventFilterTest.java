package com.jasoncarloscox.familymap.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class EventFilterTest {

    private EventFilter filter;
    private ArrayList<Event> events = new ArrayList<>();

    @Before
    public void setup() {
        Event event1 = generateEvent("birth", true, false, Gender.MALE);
        Event event2 = generateEvent("birth", true, false, Gender.FEMALE);
        Event event5 = generateEvent("birth", false, true, Gender.MALE);
        Event event6 = generateEvent("birth", false, true, Gender.FEMALE);
        Event event7 = generateEvent("death", true, false, Gender.MALE);
        Event event8 = generateEvent("death", true, false, Gender.FEMALE);
        Event event11 = generateEvent("death", false, true, Gender.MALE);
        Event event12 = generateEvent("death", false, true, Gender.FEMALE);

        events.add(event1);
        events.add(event2);
        events.add(event5);
        events.add(event6);
        events.add(event7);
        events.add(event8);
        events.add(event11);
        events.add(event12);

        Set<String> types = new HashSet<>();
        types.add("birth");
        types.add("death");

        filter = new EventFilter(types);
    }

    @After
    public void cleanup() {
        events.clear();
    }

    @Test
    public void showEventReturnsTrueForAllEventsByDefault() {
        for (Event event : events) {
            assertTrue(filter.showEvent(event));
        }
    }

    @Test
    public void showEventFiltersByPaternalSide() {
        filter.setShowPaternalSide(false);

        for (Event event : events) {
            if (event.getPerson().isPaternalSide()) {
                assertFalse(filter.showEvent(event));
            } else {
                assertTrue(filter.showEvent(event));
            }
        }
    }

    @Test
    public void showEventFiltersByMaternalSide() {
        filter.setShowMaternalSide(false);

        for (Event event : events) {
            if (event.getPerson().isMaternalSide()) {
                assertFalse(filter.showEvent(event));
            } else {
                assertTrue(filter.showEvent(event));
            }
        }
    }

    @Test
    public void showEventFiltersBySideCorrectlyIfOnBothSides() {
        // if an event belongs to the root person, who is on both the paternal
        // and maternal sides, it should be shown if either showMaternalSide or
        // showPaternalSide is true

        Event bothSides = generateEvent(events.get(0).getType(), true, true, Gender.MALE);

        filter.setShowPaternalSide(false);
        filter.setShowMaternalSide(true);

        assertTrue(filter.showEvent(bothSides));

        filter.setShowPaternalSide(true);
        filter.setShowMaternalSide(false);

        assertTrue(filter.showEvent(bothSides));

        filter.setShowPaternalSide(false);
        filter.setShowMaternalSide(false);

        assertFalse(filter.showEvent(bothSides));
    }

    @Test
    public void showEventFiltersByMale() {
        filter.setShowMale(false);

        for (Event event : events) {
            if (Gender.MALE.equals(event.getPerson().getGender())) {
                assertFalse(filter.showEvent(event));
            } else {
                assertTrue(filter.showEvent(event));
            }
        }
    }

    @Test
    public void showEventFiltersByFemale() {
        filter.setShowFemale(false);

        for (Event event : events) {
            if (Gender.FEMALE.equals(event.getPerson().getGender())) {
                assertFalse(filter.showEvent(event));
            } else {
                assertTrue(filter.showEvent(event));
            }
        }
    }

    @Test
    public void showEventFiltersByType() {
        filter.setShowType("birth", false);
        filter.setShowType("death", true);

        for (Event event : events) {
            if ("birth".equals(event.getType())) {
                assertFalse(filter.showEvent(event));
            } else {
                assertTrue(filter.showEvent(event));
            }
        }

        filter.setShowType("birth", true);
        filter.setShowType("death", false);

        for (Event event : events) {
            if ("death".equals(event.getType())) {
                assertFalse(filter.showEvent(event));
            } else {
                assertTrue(filter.showEvent(event));
            }
        }
    }

    private int eventCounter = 0;

    private Event generateEvent(String type, boolean paternal, boolean maternal,
                                String gender) {

        eventCounter++;

        Event event = new Event("eid" + eventCounter);
        event.setType(type);

        Person eventPerson = new Person("pid" + eventCounter);
        eventPerson.setGender(gender);
        eventPerson.setPaternalSide(paternal);
        eventPerson.setMaternalSide(maternal);

        event.setPerson(eventPerson);

        return event;
    }
}