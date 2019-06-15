package com.jasoncarloscox.familymap.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class ModelEventFilterTest {

    private Model model;
    private ArrayList<Event> events = new ArrayList<>();

    @Before
    public void setup() {
        model = Model.newInstance();

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

        model.load(null, null, events);
    }

    @After
    public void cleanup() {
        events.clear();
    }

    @Test
    public void shouldShowReturnsTrueForAllEventsByDefault() {
        for (Event event : events) {
            assertTrue(model.shouldShow(event));
        }
    }

    @Test
    public void shouldShowFiltersByPaternalSide() {
        model.setShouldShowPaternalSide(false);

        for (Event event : events) {
            if (event.getPerson().isPaternalSide()) {
                assertFalse(model.shouldShow(event));
            } else {
                assertTrue(model.shouldShow(event));
            }
        }
    }

    @Test
    public void shouldShowFiltersByMaternalSide() {
        model.setShouldShowMaternalSide(false);

        for (Event event : events) {
            if (event.getPerson().isMaternalSide()) {
                assertFalse(model.shouldShow(event));
            } else {
                assertTrue(model.shouldShow(event));
            }
        }
    }

    @Test
    public void shouldShowFiltersBySideCorrectlyIfOnNeitherSide() {
        // if an event belongs to the root person, who is on neither the paternal
        // nor maternal side, it should be shown no matter what

        Event neitherSide = generateEvent(events.get(0).getType(), true, true, Gender.MALE);

        model.setShouldShowPaternalSide(true);
        model.setShouldShowMaternalSide(true);

        assertTrue(model.shouldShow(neitherSide));

        model.setShouldShowPaternalSide(false);
        model.setShouldShowMaternalSide(true);

        assertTrue(model.shouldShow(neitherSide));

        model.setShouldShowPaternalSide(true);
        model.setShouldShowMaternalSide(false);

        assertTrue(model.shouldShow(neitherSide));

        model.setShouldShowPaternalSide(false);
        model.setShouldShowMaternalSide(false);

        assertTrue(model.shouldShow(neitherSide));
    }

    @Test
    public void shouldShowFiltersByMale() {
        model.setShouldShowMale(false);

        for (Event event : events) {
            if (Gender.MALE.equals(event.getPerson().getGender())) {
                assertFalse(model.shouldShow(event));
            } else {
                assertTrue(model.shouldShow(event));
            }
        }
    }

    @Test
    public void shouldShowFiltersByFemale() {
        model.setShouldShowFemale(false);

        for (Event event : events) {
            if (Gender.FEMALE.equals(event.getPerson().getGender())) {
                assertFalse(model.shouldShow(event));
            } else {
                assertTrue(model.shouldShow(event));
            }
        }
    }

    @Test
    public void shouldShowFiltersByType() {
        model.setShouldShow("birth", false);
        model.setShouldShow("death", true);

        for (Event event : events) {
            if ("birth".equals(event.getType())) {
                assertFalse(model.shouldShow(event));
            } else {
                assertTrue(model.shouldShow(event));
            }
        }

        model.setShouldShow("birth", true);
        model.setShouldShow("death", false);

        for (Event event : events) {
            if ("death".equals(event.getType())) {
                assertFalse(model.shouldShow(event));
            } else {
                assertTrue(model.shouldShow(event));
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

        model.setShouldShow("birth", false);

        Collection<Event> filteredEvents = model.filter(events);

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

        List<Event> filteredEvents = model.filter(events);

        assertEquals(event1, filteredEvents.get(0));
        assertEquals(event2, filteredEvents.get(1));
    }

    private Event generateEvent(String type, boolean paternal, boolean maternal,
                                String gender) {

        Event event = new Event(UUID.randomUUID().toString());
        event.setType(type);

        Person eventPerson = new Person(UUID.randomUUID().toString());
        eventPerson.setGender(gender);
        eventPerson.setPaternalSide(paternal);
        eventPerson.setMaternalSide(maternal);

        event.setPerson(eventPerson);

        return event;
    }
}