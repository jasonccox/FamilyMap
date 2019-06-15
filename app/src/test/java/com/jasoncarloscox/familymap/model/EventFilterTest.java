package com.jasoncarloscox.familymap.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
            assertTrue(filter.shouldShow(event));
        }
    }

    @Test
    public void showEventFiltersByPaternalSide() {
        filter.setShouldShowPaternalSide(false);

        for (Event event : events) {
            if (event.getPerson().isPaternalSide()) {
                assertFalse(filter.shouldShow(event));
            } else {
                assertTrue(filter.shouldShow(event));
            }
        }
    }

    @Test
    public void showEventFiltersByMaternalSide() {
        filter.setShouldShowMaternalSide(false);

        for (Event event : events) {
            if (event.getPerson().isMaternalSide()) {
                assertFalse(filter.shouldShow(event));
            } else {
                assertTrue(filter.shouldShow(event));
            }
        }
    }

    @Test
    public void showEventFiltersBySideCorrectlyIfOnBothSides() {
        // if an event belongs to the root person, who is on both the paternal
        // and maternal sides, it should be shown if either shouldShowMaternalSide or
        // shouldShowPaternalSide is true

        Event bothSides = generateEvent(events.get(0).getType(), true, true, Gender.MALE);

        filter.setShouldShowPaternalSide(false);
        filter.setShouldShowMaternalSide(true);

        assertTrue(filter.shouldShow(bothSides));

        filter.setShouldShowPaternalSide(true);
        filter.setShouldShowMaternalSide(false);

        assertTrue(filter.shouldShow(bothSides));

        filter.setShouldShowPaternalSide(false);
        filter.setShouldShowMaternalSide(false);

        assertFalse(filter.shouldShow(bothSides));
    }

    @Test
    public void showEventFiltersByMale() {
        filter.setShouldShowMale(false);

        for (Event event : events) {
            if (Gender.MALE.equals(event.getPerson().getGender())) {
                assertFalse(filter.shouldShow(event));
            } else {
                assertTrue(filter.shouldShow(event));
            }
        }
    }

    @Test
    public void showEventFiltersByFemale() {
        filter.setShouldShowFemale(false);

        for (Event event : events) {
            if (Gender.FEMALE.equals(event.getPerson().getGender())) {
                assertFalse(filter.shouldShow(event));
            } else {
                assertTrue(filter.shouldShow(event));
            }
        }
    }

    @Test
    public void showEventFiltersByType() {
        filter.setShouldShow("birth", false);
        filter.setShouldShow("death", true);

        for (Event event : events) {
            if ("birth".equals(event.getType())) {
                assertFalse(filter.shouldShow(event));
            } else {
                assertTrue(filter.shouldShow(event));
            }
        }

        filter.setShouldShow("birth", true);
        filter.setShouldShow("death", false);

        for (Event event : events) {
            if ("death".equals(event.getType())) {
                assertFalse(filter.shouldShow(event));
            } else {
                assertTrue(filter.shouldShow(event));
            }
        }
    }

    @Test
    public void filterReturnsEventsMatchingFilter() {
        Event event1 = new Event("eid1");
        event1.setType("birth");

        Event event2 = new Event("eid2");
        event2.setType("death");

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);

        filter.setShouldShow("birth", false);

        Collection<Event> filteredEvents = filter.filter(events);

        assertEquals(1, filteredEvents.size());
        assertTrue(filteredEvents.contains(event2));
    }

    @Test
    public void filterPreservesEventOrder() {
        Event event1 = new Event("eid1");
        event1.setType("birth");

        Event event2 = new Event("eid2");
        event2.setType("death");

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);

        List<Event> filteredEvents = filter.filter(events);

        assertEquals(event1, filteredEvents.get(0));
        assertEquals(event2, filteredEvents.get(1));
    }
    
    @Test
    public void detectsAlteration() {
        filter.setShouldShowPaternalSide(!filter.shouldShowPaternalSide());
        assertTrue(filter.isAltered());
        filter.setShouldShowPaternalSide(!filter.shouldShowPaternalSide());
        assertFalse(filter.isAltered());

        filter.setShouldShowMaternalSide(!filter.shouldShowMaternalSide());
        assertTrue(filter.isAltered());
        filter.setShouldShowMaternalSide(!filter.shouldShowMaternalSide());
        assertFalse(filter.isAltered());

        filter.setShouldShowMale(!filter.shouldShowMale());
        assertTrue(filter.isAltered());
        filter.setShouldShowMale(!filter.shouldShowMale());
        assertFalse(filter.isAltered());

        filter.setShouldShowFemale(!filter.shouldShowFemale());
        assertTrue(filter.isAltered());
        filter.setShouldShowFemale(!filter.shouldShowFemale());
        assertFalse(filter.isAltered());
        
        filter.setShouldShow("birth", !filter.shouldShow("birth"));
        assertTrue(filter.isAltered());
        filter.setShouldShow("birth", !filter.shouldShow("birth"));
        assertFalse(filter.isAltered());
    }
    
    @Test
    public void resetAlteredResets() {
        filter.setShouldShowPaternalSide(!filter.shouldShowPaternalSide());
        filter.setShouldShowMaternalSide(!filter.shouldShowMaternalSide());
        filter.setShouldShowMale(!filter.shouldShowMale());
        filter.setShouldShowFemale(!filter.shouldShowFemale());
        filter.setShouldShow("birth", !filter.shouldShow("birth"));

        filter.resetAltered();
        
        assertFalse(filter.isAltered());
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