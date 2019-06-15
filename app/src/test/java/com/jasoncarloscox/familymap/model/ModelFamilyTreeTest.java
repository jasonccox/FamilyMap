package com.jasoncarloscox.familymap.model;

import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.maps.model.Marker;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;

public class ModelFamilyTreeTest {

    private Model model;

    @Before
    public void setup() {
        model = Model.newInstance();
    }

    @After
    public void cleanup() {
        model.clear();
    }

    // TODO: test search

    @Test
    public void loadSetsUpRelationships() {
        Person child = new Person("child");
        Person dad = new Person("dad");
        Person mom = new Person("mom");

        child.setFatherID(dad.getId());
        child.setMotherID(mom.getId());
        dad.setSpouseID(mom.getId());
        mom.setSpouseID(dad.getId());

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(child);
        persons.add(dad);
        persons.add(mom);

        model.load(persons, child.getId(), new ArrayList<Event>());

        assertEquals(dad, child.getFather());
        assertEquals(mom, child.getMother());
        assertEquals(child, dad.getChildren().get(0));
        assertEquals(child, mom.getChildren().get(0));
        assertEquals(mom, dad.getSpouse());
        assertEquals(dad, mom.getSpouse());
        assertNull(child.getSpouse());
        assertNull(dad.getFather());
        assertNull(dad.getMother());
        assertNull(mom.getFather());
        assertNull(mom.getMother());
    }

    @Test
    public void loadConnectsEventsToPeople() {
        Person person = new Person("pid");
        Event birth = new Event("eid");
        birth.setPersonId(person.getId());

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(person);

        ArrayList<Event> events = new ArrayList<>();
        events.add(birth);

        model.load(persons, person.getId(), events);

        assertEquals(birth, person.getEvents().get(0));
        assertEquals(person, birth.getPerson());
    }

    @Test
    public void loadSetsEventTypes() {
        Event event1 = new Event("eid1");
        event1.setType("type1");

        Event event2 = new Event("eid2");
        event2.setType("type2");

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);

        model.load(new ArrayList<Person>(), null, events);

        Set<String> eventTypes = model.getEventTypes();

        assertEquals(2, eventTypes.size());
        assertTrue(eventTypes.contains(event1.getType()));
        assertTrue(eventTypes.contains(event2.getType()));
    }

    @Test
    public void loadClearsPriorData() {
        Person person = new Person("pid");
        Event event = new Event("eid");
        event.setType("birth");
        event.setPersonId(person.getId());

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(person);

        ArrayList<Event> events = new ArrayList<>();
        events.add(event);

        model.load(persons, person.getId(), events);

        Person person2 = new Person("pid2");
        Event event2 = new Event("eid2");
        event2.setType("death");
        event2.setPersonId(person.getId());

        ArrayList<Person> persons2 = new ArrayList<>();
        persons2.add(person2);

        ArrayList<Event> events2 = new ArrayList<>();
        events2.add(event2);

        model.load(persons2, person2.getId(), events2);

        assertNull(model.getPerson(person.getId()));
        assertNull(model.getEvent(person.getId()));
        assertFalse(model.getEventTypes().contains(event.getType()));
    }

    @Test
    public void getPersonReturnsCorrectPerson() {
        Person person1 = new Person("pid1");
        Person person2 = new Person("pid2");

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        model.load(persons, person1.getId(), new ArrayList<Event>());

        assertEquals(person1, model.getPerson(person1.getId()));
        assertEquals(person2, model.getPerson(person2.getId()));
    }

    @Test
    public void getPersonReturnsNullIfNoSuchPerson() {
        Person person1 = new Person("pid1");
        Person person2 = new Person("pid2");

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        model.load(persons, person1.getId(), new ArrayList<Event>());

        assertNull(model.getPerson("cantfindme"));
    }

    @Test
    public void getEventReturnsCorrectEvent() {
        Event event1 = new Event("eid1");
        Event event2 = new Event("eid2");

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);

        model.load(new ArrayList<Person>(), null, events);

        assertEquals(event1, model.getEvent(event1.getId()));
        assertEquals(event2, model.getEvent(event2.getId()));
    }

    @Test
    public void getEventReturnsNullIfNoSuchPerson() {
        Event event1 = new Event("eid1");
        Event event2 = new Event("eid2");

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);

        model.load(new ArrayList<Person>(), null, events);

        assertNull(model.getEvent("cantfindme"));
    }

}