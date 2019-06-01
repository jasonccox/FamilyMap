package com.jasoncarloscox.familymap.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Represents a person and his/her relationships in the family map.
 */
public class Person {

    private final String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;
    private LinkedList<Event> events = new LinkedList<>();
    private LinkedList<Person> children = new LinkedList<>();

    /**
     * Creates a new Person.
     * 
     * @param id a unique identifier for this person
     */
    public Person(String id) {
        this.personID = id;
    }

    /**
     * Adds an event to this person.
     *
     * @param event the event to be added
     */
    public void addEvent(Event event) {
        int i = 0;
        while (i < events.size() && event.getYear() < events.get(i).getYear()) {
            i++;
        }

        events.add(i, event);
    }

    /**
     * @return this person's events in chronological order
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Gets all of this person's events of the specified types.
     *
     * @param types the types of events to get
     * @return the events of the specified types
     */
    public List<Event> getEvents(Set<String> types) {
        LinkedList<Event> matchingEvents = new LinkedList<>();

        for (Event e : events) {
            if (types.contains(e.getType())) {
                matchingEvents.add(e);
            }
        }

        return matchingEvents;
    }

    /**
     * @return this person's chronologically first event, or null if this person
     *         has no events
     */
    public Event getFirstEvent() {
        if (events.isEmpty()) {
            return null;
        }

        return events.getFirst();
    }

    /**
     * Adds a child to this person.
     *
     * @param child the child to be added
     */
    public void addChild(Person child) {
        int i = 0;
        while (i < children.size() && child.getFirstEvent().getYear() <
                                      children.get(i).getFirstEvent().getYear()) {
            i++;
        }

        children.add(i, child);
    }

    /**
     * @return this person's children, ordered chronologically based on each
     *         child's first event
     */
    public List<Person> getChildren() {
        return children;
    }

    /**
     * @return a unique identifier for this person
     */
    public String getId() {
        return personID;
    }

    /**
     * @return the person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the person's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the person's gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the Pid of the person representing this person's father
     */
    public String getFather() {
        return father;
    }

    /**
     * @param father the id of the person representing this person's father
     */
    public void setFather(String father) {
        this.father = father;
    }

    /**
     * @return the id of the person representing this person's mother
     */
    public String getMother() {
        return mother;
    }

    /**
     * @param mother the id of the person representing this person's mother
     */
    public void setMother(String mother) {
        this.mother = mother;
    }

    /**
     * @return the id of the person representing this person's spouse
     */
    public String getSpouse() {
        return spouse;
    }

    /**
     * @param spouse the id of the person representing this person's spouse
     */
    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    @Override
    public int hashCode() {
        return personID.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        return personID.equals(((Person) o).personID);
    }
    
}