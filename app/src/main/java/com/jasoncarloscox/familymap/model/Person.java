package com.jasoncarloscox.familymap.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.jasoncarloscox.familymap.model.Relative.Relationship;

/**
 * Represents a person and his/her relationships in the family map.
 */
public class Person implements Comparable<Person> {

    private final String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private LinkedList<Event> events = new LinkedList<>();
    private LinkedList<Person> children = new LinkedList<>();
    private boolean isPaternalSide;
    private boolean isMaternalSide;
    private Person father;
    private Person mother;
    private Person spouse;

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
        while (i < events.size() && event.compareTo(events.get(i)) > 0) {
            i++;
        }
        events.add(i, event);

        event.setPerson(this);
    }

    /**
     * @return this person's events in chronological order
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * @return this person's chronologically first event that matches the filter,
     *         or null if this person has no events matching the filter
     */
    public Event getFirstEvent() {
        for (int i = 0; i < events.size(); i++) {
            if (Model.instance().shouldShow(events.get(i))) {
                return events.get(i);
            }
        }

        return null;
    }

    /**
     * Adds a child to this person.
     *
     * @param child the child to be added
     */
    private void addChild(Person child) {
        int i = 0;
        while (i < children.size() && child.compareTo(children.get(i)) > 0) {
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
     * @return the person's full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
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
     * @return the Pid of the person representing this person's fatherID
     */
    public String getFatherID() {
        return fatherID;
    }

    /**
     * @param fatherID the id of the person representing this person's fatherID
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * @return the id of the person representing this person's motherID
     */
    public String getMotherID() {
        return motherID;
    }

    /**
     * @param motherID the id of the person representing this person's motherID
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * @return the id of the person representing this person's spouseID
     */
    public String getSpouseID() {
        return spouseID;
    }

    /**
     * @param spouseID the id of the person representing this person's spouseID
     */
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    /**
     * @return whether this person is the root person's father or one of his
     *         ancestors
     */
    public boolean isPaternalSide() {
        return isPaternalSide;
    }

    /**
     * @param paternalSide whether this person is the root person's father or
     *                     one of his ancestors
     */
    public void setPaternalSide(boolean paternalSide) {
        isPaternalSide = paternalSide;
    }

    /**
     * @return whether this person is the root person's mother or one of her
     *         ancestors
     */
    public boolean isMaternalSide() {
        return isMaternalSide;
    }

    /**
     * @param maternalSide whether this person is the root person's mother or
     *                     one of her ancestors
     */
    public void setMaternalSide(boolean maternalSide) {
        isMaternalSide = maternalSide;
    }

    /**
     * @return this person's father
     */
    public Person getFather() {
        return father;
    }

    /**
     * Sets this person's father and adds this person as the father's child
     * @param father this person's father
     */
    public void setFather(Person father) {
        this.father = father;
        if (father != null) {
            this.fatherID = father.getId();
            father.addChild(this);
        } else {
            this.fatherID = null;
        }
    }

    /**
     * @return this person's mother
     */
    public Person getMother() {
        return mother;
    }

    /**
     * Sets this person's mother and adds this person as the mother's child
     * @param mother this person's mother
     */
    public void setMother(Person mother) {
        this.mother = mother;
        if (mother != null) {
            this.motherID = mother.getId();
            mother.addChild(this);
        } else {
            this.motherID = null;
        }
    }

    /**
     * @return this person's spouse
     */
    public Person getSpouse() {
        return spouse;
    }

    /**
     * @param spouse this person's spouse
     */
    public void setSpouse(Person spouse) {
        this.spouse = spouse;
        if (spouse != null) {
            this.spouseID = spouse.getId();
        } else {
            this.spouseID = null;
        }
    }

    /**
     * @return the members of the person's immediate family
     */
    public List<Relative> getFamily() {
        List<Relative> relatives = new ArrayList<>();

        if (father != null) {
            relatives.add(new Relative(father, Relationship.FATHER));
        }

        if (mother != null) {
            relatives.add(new Relative(mother, Relationship.MOTHER));
        }

        if (spouse != null) {
            if (Gender.MALE.equals(spouse.getGender())) {
                relatives.add(new Relative(spouse, Relationship.HUSBAND));
            } else if (Gender.FEMALE.equals(spouse.getGender())) {
                relatives.add(new Relative(spouse, Relationship.WIFE));
            } else {
                relatives.add(new Relative(spouse, null));
            }
        }

        for (Person child : children) {
            if (Gender.MALE.equals(child.getGender())) {
                relatives.add(new Relative(child, Relationship.SON));
            } else if (Gender.FEMALE.equals(child.getGender())) {
                relatives.add(new Relative(child, Relationship.DAUGHTER));
            } else {
                relatives.add(new Relative(child, null));
            }
        }

        return relatives;
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

    @Override
    public int compareTo(Person o) {
        if (o == null) {
            return 1;
        }

        Event thisFirstEvent = this.getFirstEvent();
        Event oFirstEvent = o.getFirstEvent();

        if (thisFirstEvent == null && oFirstEvent == null) {
            return 0;
        }

        if (thisFirstEvent == null && oFirstEvent != null) {
            return -1;
        }

        if (thisFirstEvent != null && oFirstEvent == null) {
            return 1;
        }

        return thisFirstEvent.compareTo(oFirstEvent);
    }
}