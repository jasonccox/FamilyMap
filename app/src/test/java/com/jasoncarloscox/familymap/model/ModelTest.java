package com.jasoncarloscox.familymap.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ModelTest {

    Model model;

    @Before
    public void setup() {
        model = Model.newInstance();
    }

    @Test
    public void instanceAlwaysReturnsSameInstance() {
        Model model2 = Model.instance();

        assertTrue(model == model2);
    }

    @Test
    public void loginSetsUser() {
        User user = new User("uname", "pw");
        model.login(user);

        assertEquals(user.getUsername(), model.getUser().getUsername());
    }

    @Test
    public void loginSetsLoggedInToTrue() {
        User user = new User("uname", "pw");
        model.login(user);

        assertTrue(model.isLoggedIn());
    }

    @Test
    public void logoutClearsAllDataAndSettings() {
        User user = new User("uname", "pw");
        model.login(user);

        Event event = new Event("eid");
        Collection<Event> events = new ArrayList<>();
        events.add(event);

        Person person = new Person("pid");
        person.addEvent(event);
        Collection<Person> persons = new ArrayList<>();
        persons.add(person);

        model.load(persons, person.getId(), events);
        model.setShouldShowMale(false);
        model.setShouldShowLifeLines(false);

        model.logout();

        assertNull(model.getUser());
        assertNull(model.getPerson(person.getId()));
        assertNull(model.getEvent(event.getId()));
        assertTrue(model.shouldShowMale());
        assertTrue(model.shouldShowLifeLines());
    }

    @Test
    public void logoutSetsLoggedInToFalse() {
        User user = new User("uname", "pw");
        model.login(user);
        model.logout();

        assertFalse(model.isLoggedIn());
    }

    @Test
    public void clearClearsTreeData() {
        Event event = new Event("eid");
        Collection<Event> events = new ArrayList<>();
        events.add(event);

        Person person = new Person("pid");
        person.addEvent(event);
        Collection<Person> persons = new ArrayList<>();
        persons.add(person);

        model.load(persons, person.getId(), events);

        model.clear();

        assertNull(model.getPerson(person.getId()));
        assertNull(model.getEvent(event.getId()));
    }

    @Test
    public void clearPreservesFilters() {
        model.setShouldShowMale(false);

        model.clear();

        assertFalse(model.shouldShowMale());
    }

    @Test
    public void clearPreservesSettings() {
        model.setShouldShowLifeLines(false);

        model.clear();

        assertFalse(model.shouldShowLifeLines());
    }

    @Test
    public void clearClearsUser() {
        User user = new User("uname", "pw");
        model.login(user);

        model.clear();

        assertNull(model.getUser());
    }
}
