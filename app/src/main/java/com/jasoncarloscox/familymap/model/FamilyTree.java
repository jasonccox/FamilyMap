package com.jasoncarloscox.familymap.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a family tree with persons and events.
 */
public class FamilyTree {

    private Map<String, Person> personsById = new HashMap<>();
    private Map<String, Event> eventsById = new HashMap<>();

    private Person rootPerson;

    private Set<String> eventTypes = new HashSet<>();
    
    protected FamilyTree() {}

    /**
     * Loads persons and events into the family tree. This includes setting each
     * person's father, mother, and children and whether the person is on the
     * maternal or paternal side of the rootPerson's tree. It also includes
     * attaching each event to its person
     *
     * @param persons the persons to load
     * @param rootPersonId the id of the root person - this person must be
     *                     included in persons
     * @param events the events to load - each must belong to a person in persons
     */
    protected void load(Collection<Person> persons, String rootPersonId,
                     Collection<Event> events) {

        if (persons != null && !persons.isEmpty()) {
            loadPersons(persons, rootPersonId);
        }

        if (events != null && !events.isEmpty()) {
            loadEvents(events);
        }
    }

    /**
     * Gets a person with the designated id.
     *
     * @param id the id of the person to get
     * @return the person with the given id, or null if there is no such person
     */
    protected Person getPerson(String id) {
        return personsById.get(id);
    }

    /**
     * Gets a event with the designated id.
     *
     * @param id the id of the event to get
     * @return the event with the given id, or null if there is no such event
     */
    protected Event getEvent(String id) {
        return eventsById.get(id);
    }

    /**
     * @return all the events in this tree
     */
    protected Collection<Event> getEvents() {
        return eventsById.values();
    }

    /**
     * @return the types of events in the family tree
     */
    protected Set<String> getEventTypes() {
        return eventTypes;
    }

    /**
     * Loads persons into the family tree, including setting each person's
     * father, mother, and children and whether the person is on the maternal
     * or paternal side of the rootPerson's tree.
     *
     * @param persons the persons to be loaded
     * @param rootPersonId the id of the root person - this must be one of the
     *                     persons in persons
     */
    private void loadPersons(Collection<Person> persons, String rootPersonId) {
        for (Person p : persons) {
            personsById.put(p.getId(), p);
        }

        if (!personsById.containsKey(rootPersonId)) {
            throw new IllegalArgumentException("rootPersonId must be the id of " +
                    "one of the persons");
        }

        rootPerson = personsById.get(rootPersonId);

        setupTree();
    }

    /**
     * Sets each person's father, mother, and children and whether the person is
     * on the maternal or paternal side of the rootPerson's tree.
     */
    private void setupTree() {
        rootPerson.setPaternalSide(true);
        rootPerson.setMaternalSide(true);

        rootPerson.setFather(personsById.get(rootPerson.getFatherID()));
        rootPerson.setMother(personsById.get(rootPerson.getMotherID()));
        rootPerson.setSpouse(personsById.get(rootPerson.getSpouseID()));

        setupAncestors(rootPerson.getFather(), true, false);
        setupAncestors(rootPerson.getMother(), false, true);
    }

    /**
     * Recursive method that sets the father, mother, and children of the root
     * and each of its ancestors. This method also sets whether each of these
     * persons is on the paternal or maternal side of the rootPerson's (not the
     * root's) family.
     *
     * @param root the person whose ancestors should be setup
     * @param paternal whether root is on the paternal side of the rootPerson's
     *                 family
     * @param maternal whether root is on the maternal side of the rootPerson's
     *                 family
     */
    private void setupAncestors(Person root, boolean paternal, boolean maternal) {
        if (root == null) {
            return;
        }

        root.setPaternalSide(paternal);
        root.setMaternalSide(maternal);

        root.setFather(personsById.get(root.getFatherID()));
        root.setMother(personsById.get(root.getMotherID()));
        root.setSpouse(personsById.get(root.getSpouseID()));

        setupAncestors(root.getFather(), paternal, maternal);
        setupAncestors(root.getMother(), paternal, maternal);
    }

    /**
     * Attaches the events to the person's to whom they belong.
     *
     * @param events the events to be attached
     */
    private void loadEvents(Collection<Event> events) {
        for (Event e : events) {
            eventsById.put(e.getId(), e);
            eventTypes.add(e.getType());

            Person eventPerson = personsById.get(e.getPersonId());
            if (eventPerson != null) {
                eventPerson.addEvent(e);
            }
        }
    }

}