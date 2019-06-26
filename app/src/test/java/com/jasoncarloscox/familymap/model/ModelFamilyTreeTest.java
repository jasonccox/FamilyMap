package com.jasoncarloscox.familymap.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    @Test
    public void searchMatchesPersonName() {
        Person person = new Person("pid");
        person.setFirstName("first");
        person.setLastName("last");

        Collection<Person> persons = new ArrayList<>();
        persons.add(person);

        model.load(persons, person.getId(), null);

        List<Object> results = model.searchPersonsAndEvents("first");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Person);
        assertEquals(person.getId(), ((Person) results.get(0)).getId());

        results = model.searchPersonsAndEvents("last");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Person);
        assertEquals(person.getId(), ((Person) results.get(0)).getId());

        results = model.searchPersonsAndEvents("first last");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Person);
        assertEquals(person.getId(), ((Person) results.get(0)).getId());

        results = model.searchPersonsAndEvents("fi");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Person);
        assertEquals(person.getId(), ((Person) results.get(0)).getId());

        results = model.searchPersonsAndEvents("FIRST");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Person);
        assertEquals(person.getId(), ((Person) results.get(0)).getId());
    }

    @Test
    public void searchMatchesEventType() {
        Event event = new Event("eid");
        event.setType("type");

        Collection<Event> events = new ArrayList<>();
        events.add(event);

        Person person = new Person("pid");
        person.addEvent(event);

        Collection<Person> persons = new ArrayList<>();
        persons.add(person);

        model.load(persons, person.getId(), events);

        List<Object> results = model.searchPersonsAndEvents("type");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());

        results = model.searchPersonsAndEvents("pe");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());

        results = model.searchPersonsAndEvents("TYPE");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());
    }

    @Test
    public void searchMatchesEventCity() {
        Event event = new Event("eid");
        event.setCity("city");

        Collection<Event> events = new ArrayList<>();
        events.add(event);

        Person person = new Person("pid");
        person.addEvent(event);

        Collection<Person> persons = new ArrayList<>();
        persons.add(person);

        model.load(persons, person.getId(), events);

        List<Object> results = model.searchPersonsAndEvents("city");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());

        results = model.searchPersonsAndEvents("it");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());

        results = model.searchPersonsAndEvents("CITY");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());
    }

    @Test
    public void searchMatchesEventCountry() {
        Event event = new Event("eid");
        event.setCountry("country");

        Collection<Event> events = new ArrayList<>();
        events.add(event);

        Person person = new Person("pid");
        person.addEvent(event);

        Collection<Person> persons = new ArrayList<>();
        persons.add(person);

        model.load(persons, person.getId(), events);

        List<Object> results = model.searchPersonsAndEvents("country");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());

        results = model.searchPersonsAndEvents("ntr");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());

        results = model.searchPersonsAndEvents("COUNTRY");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());
    }

    @Test
    public void searchMatchesEventYear() {
        Event event = new Event("eid");
        event.setYear(2000);

        Collection<Event> events = new ArrayList<>();
        events.add(event);

        Person person = new Person("pid");
        person.addEvent(event);

        Collection<Person> persons = new ArrayList<>();
        persons.add(person);

        model.load(persons, person.getId(), events);

        List<Object> results = model.searchPersonsAndEvents("2000");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());

        results = model.searchPersonsAndEvents("20");
        assertEquals(1, results.size());
        assertTrue(results.get(0) instanceof Event);
        assertEquals(event.getId(), ((Event) results.get(0)).getId());
    }

    @Test
    public void searchSearchesEntireTree() {
        Person person = new Person("pid1");
        person.setFirstName("John");
        person.setLastName("Smith");

        Person dad = new Person("pid2");
        dad.setFirstName("Bob");
        dad.setLastName("Smith");

        Person mom = new Person("pid3");
        mom.setFirstName("Sally");
        mom.setLastName("Smith");

        person.setFather(dad);
        person.setMother(mom);

        Collection<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(dad);
        persons.add(mom);

        Event personEvent = new Event("eid1");
        personEvent.setType("birth");

        Event dadEvent = new Event("eid2");
        dadEvent.setType("birth");

        Event momEvent = new Event("eid3");
        momEvent.setType("birth");

        Collection<Event> events = new ArrayList<>();
        events.add(personEvent);
        events.add(dadEvent);
        events.add(momEvent);

        person.addEvent(personEvent);
        dad.addEvent(dadEvent);
        mom.addEvent(momEvent);

        model.load(persons, person.getId(), events);

        List<Object> results = model.searchPersonsAndEvents("Smith");
        assertEquals(3, results.size());

        results = model.searchPersonsAndEvents("birth");
        assertEquals(3, results.size());
    }

    @Test
    public void searchReturnsEmptyListIfNoMatch() {
        Event event = new Event("eid");
        event.setType("type");
        event.setCity("city");
        event.setCountry("country");
        event.setYear(2000);

        Collection<Event> events = new ArrayList<>();
        events.add(event);

        Person person = new Person("pid");
        person.setFirstName("first");
        person.setLastName("last");
        person.addEvent(event);

        Collection<Person> persons = new ArrayList<>();
        persons.add(person);

        model.load(persons, person.getId(), events);

        List<Object> results = model.searchPersonsAndEvents("something");
        assertEquals(0, results.size());
    }
}