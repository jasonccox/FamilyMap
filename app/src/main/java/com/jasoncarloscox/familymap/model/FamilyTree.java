package com.jasoncarloscox.familymap.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a family tree with persons and events.
 */
public class FamilyTree {

    private Map<String, Person> personsById = new HashMap<>();
    private Map<String, Event> eventsById = new HashMap<>();

    private Set<Person> paternalSidePersons = new HashSet<>();
    private Set<Person> maternalSidePersons= new HashSet<>();

    private Person rootPerson;

    private Set<String> eventTypes = new HashSet<>();
    
    private FamilyTree() {}

    /**
     * Loads persons into the family tree and sets the root person.
     *
     * @param persons the persons to load
     * @param rootPerson the root person - this person must also be included in
     *                   persons
     * @param events the events to load - each must belong to a person in persons
     */
    public void load(Collection<Person> persons, Person rootPerson,
                     Collection<Event> events) {

        if (!persons.contains(rootPerson)) {
            throw new IllegalArgumentException("rootPerson must be in persons");
        }

        this.rootPerson = rootPerson;

        loadPersons(persons);

        loadEvents(events);

        setupChildren();
    }

    /**
     * Gets a person with the designated id.
     *
     * @param id the id of the person to get
     * @return the person with the given id, or null if there is no such person
     */
    public Person getPerson(String id) {
        return personsById.get(id);
    }

    /**
     * Gets a event with the designated id.
     *
     * @param id the id of the event to get
     * @return the event with the given id, or null if there is no such event
     */
    public Event getEvent(String id) {
        return eventsById.get(id);
    }

    /**
     * @return the ancestors on the root person's paternal side, including the
     *         root person
     */
    public Set<Person> getPaternalSidePersons() {
        return paternalSidePersons;
    }

    /**
     * @return the ancestors on the root person's maternal side, including the
     *         root person
     */
    public Set<Person> getMaternalSidePersons() {
        return maternalSidePersons;
    }

    /**
     * @return the types of events in the family tree
     */
    public Set<String> getEventTypes() {
        return eventTypes;
    }

    private Set<Person> paternalSidePersons() {
        Set<Person> persons = new HashSet<>();

        persons.add(rootPerson);

        if (rootPerson.getFather() != null) {
            persons.add(personsById.get(rootPerson.getFather()));
            persons.addAll(ancestors(personsById.get(rootPerson.getFather())));
        }

        return persons;
    }

    private Set<Person> maternalSidePersons() {
        Set<Person> persons = new HashSet<>();

        persons.add(rootPerson);

        if (rootPerson.getMother() != null) {
            persons.add(personsById.get(rootPerson.getMother()));
            persons.addAll(ancestors(personsById.get(rootPerson.getMother())));
        }

        return persons;
    }

    private Set<Person> ancestors(Person root) {
        Set<Person> ancestors = new HashSet<>();

        if (root == null) {
            return ancestors;
        }

        if (root.getFather() != null) {
            ancestors.add(personsById.get(root.getFather()));
            ancestors.addAll(ancestors(personsById.get(root.getFather())));
        }

        if (root.getMother() != null) {
            ancestors.add(personsById.get(root.getMother()));
            ancestors.addAll(ancestors(personsById.get(root.getMother())));
        }

        return ancestors;
    }

    private void setupChildren() {
        for (Person p : personsById.values()) {
            if (p.getFather() != null) {
                personsById.get(p.getFather()).addChild(p);
            }

            if (p.getMother() != null) {
                personsById.get(p.getMother()).addChild(p);
            }
        }
    }

    private void loadPersons(Collection<Person> persons) {
        for (Person p : persons) {
            personsById.put(p.getId(), p);
        }

        paternalSidePersons = paternalSidePersons();
        maternalSidePersons = maternalSidePersons();
    }

    private void loadEvents(Collection<Event> events) {
        for (Event e : events) {
            eventsById.put(e.getId(), e);
            eventTypes.add(e.getType());

            personsById.get(e.getPersonId()).addEvent(e);
        }
    }

}