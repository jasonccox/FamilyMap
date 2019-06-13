package com.jasoncarloscox.familymap.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class PersonTest {

    private Person person;

    @Before
    public void setup() {
        person = new Person("pid");
    }

    @Test
    public void addEventAddsEventToPerson() {
        Event event = new Event("eid");
        person.addEvent(event);

        assertEquals(event, person.getEvents().get(0));
    }


    @Test
    public void addEventAddsPersonToEvent() {
        Event event = new Event("eid");
        person.addEvent(event);

        assertEquals(person, event.getPerson());
    }

    @Test
    public void getEventsAlwaysPutsBirthFirst() {
        Event first = new Event("first");
        first.setYear(1);

        Event second = new Event("second");
        second.setYear(2);

        Event third = new Event("third");
        third.setYear(3);
        third.setType("birth");

        person.addEvent(second);
        person.addEvent(third);
        person.addEvent(first);

        assertEquals(third, person.getEvents().get(0));
        assertEquals(first, person.getEvents().get(1));
        assertEquals(second, person.getEvents().get(2));
    }

    @Test
    public void getEventsAlwaysPutsDeathLast() {
        Event first = new Event("first");
        first.setYear(1);
        first.setType("death");

        Event second = new Event("second");
        second.setYear(2);

        Event third = new Event("third");
        third.setYear(3);

        person.addEvent(second);
        person.addEvent(third);
        person.addEvent(first);

        assertEquals(second, person.getEvents().get(0));
        assertEquals(third, person.getEvents().get(1));
        assertEquals(first, person.getEvents().get(2));
    }

    @Test
    public void getEventsReturnsEventsInChronologicalOrder() {
        Event first = new Event("first");
        first.setYear(1);

        Event second = new Event("second");
        second.setYear(2);

        Event third = new Event("third");
        third.setYear(3);

        person.addEvent(second);
        person.addEvent(third);
        person.addEvent(first);

        assertEquals(first, person.getEvents().get(0));
        assertEquals(second, person.getEvents().get(1));
        assertEquals(third, person.getEvents().get(2));
    }

    @Test
    public void getFirstEventReturnsFirstFilteredEvent() {
        Event first = new Event("first");
        first.setType("marriage");
        first.setYear(1);

        Event second = new Event("second");
        second.setType("baptism");
        second.setYear(2);

        Event third = new Event("third");
        third.setType("death");
        third.setYear(3);

        person.addEvent(second);
        person.addEvent(third);
        person.addEvent(first);

        Set<String> setBD = new HashSet<>();
        setBD.add("baptism");
        setBD.add("death");

        EventFilter filter = new EventFilter(setBD);

        assertEquals(second, person.getFirstEvent(filter));
    }

    @Test
    public void getFirstEventReturnsFirstEventIfNullFilter() {
        Event first = new Event("first");
        first.setType("marriage");
        first.setYear(1);

        Event second = new Event("second");
        second.setType("baptism");
        second.setYear(2);

        Event third = new Event("third");
        third.setType("death");
        third.setYear(3);

        person.addEvent(second);
        person.addEvent(third);
        person.addEvent(first);

        assertEquals(first, person.getFirstEvent(null));
    }

    @Test
    public void getChildrenReturnsChildrenInChronologicalOrderOfFirstEvent() {
        Person child1 = new Person("c1");
        Event event1 = new Event("e1");
        event1.setYear(1);
        child1.addEvent(event1);

        Person child2 = new Person("c2");
        Event event2 = new Event("e2");
        event2.setYear(2);
        child2.addEvent(event2);

        Person child3 = new Person("c3");
        Event event3 = new Event("e3");
        event3.setYear(3);
        child3.addEvent(event3);

        child3.setFather(person);
        child1.setFather(person);
        child2.setFather(person);

        List<Person> children = person.getChildren();

        assertEquals(3, children.size());
        assertEquals(child1, children.get(0));
        assertEquals(child2, children.get(1));
        assertEquals(child3, children.get(2));
    }

    @Test
    public void setFatherAddsFatherToChild() {
        Person father = new Person("f");
        person.setFather(father);

        assertEquals(father, person.getFather());
    }

    @Test
    public void setFatherAddsChildToFather() {
        Person father = new Person("f");
        person.setFather(father);

        assertEquals(person, father.getChildren().get(0));
    }

    @Test
    public void setFatherSetsFatherID() {
        Person father = new Person("f");
        person.setFather(father);

        assertEquals(father.getId(), person.getFatherID());
    }

    @Test
    public void setFatherHandlesNullInput() {
        person.setFather(null);

        assertEquals(null, person.getFather());
        assertEquals(null, person.getFatherID());
    }

    @Test
    public void setMotherAddsMotherToChild() {
        Person mother = new Person("m");
        person.setMother(mother);

        assertEquals(mother, person.getMother());
    }

    @Test
    public void setMotherAddsChildToMother() {
        Person mother = new Person("m");
        person.setMother(mother);

        assertEquals(person, mother.getChildren().get(0));
    }

    @Test
    public void setMotherSetsMotherID() {
        Person mother = new Person("m");
        person.setMother(mother);

        assertEquals(mother.getId(), person.getMotherID());
    }

    @Test
    public void setMotherHandlesNullInput() {
        person.setMother(null);

        assertEquals(null, person.getMother());
        assertEquals(null, person.getMotherID());
    }

    @Test
    public void setSpouseSetsSpouse() {
        Person spouse = new Person("s");
        person.setSpouse(spouse);

        assertEquals(spouse, person.getSpouse());
    }

    @Test
    public void setSpouseSetsSpouseID() {
        Person spouse = new Person("s");
        person.setSpouse(spouse);

        assertEquals(spouse.getId(), person.getSpouseID());
    }

    @Test
    public void setSpouseHandlesNullInput() {
        person.setSpouse(null);

        assertEquals(null, person.getSpouse());
        assertEquals(null, person.getSpouseID());
    }

    @Test
    public void getRelativesSetsCorrectRelationships() {
        Person dad = new Person("dad");
        person.setFather(dad);
        assertEquals(Relative.Relationship.FATHER,
                     person.getRelatives().get(0).getRelationship());
        person.setFather(null);

        Person mom = new Person("mom");
        person.setMother(mom);
        assertEquals(Relative.Relationship.MOTHER,
                person.getRelatives().get(0).getRelationship());
        person.setMother(null);

        Person wife = new Person("wife");
        wife.setGender(Gender.FEMALE);
        person.setSpouse(wife);
        assertEquals(Relative.Relationship.WIFE,
                person.getRelatives().get(0).getRelationship());

        Person husband = new Person("husband");
        husband.setGender(Gender.MALE);
        person.setSpouse(husband);
        assertEquals(Relative.Relationship.HUSBAND,
                person.getRelatives().get(0).getRelationship());
        person.setSpouse(null);

        Person son = new Person("son");
        son.setGender(Gender.MALE);
        son.setFather(person);
        assertEquals(Relative.Relationship.SON,
                     person.getRelatives().get(0).getRelationship());
        person = new Person("pid");

        Person daughter = new Person("daughter");
        daughter.setGender(Gender.FEMALE);
        daughter.setFather(person);
        assertEquals(Relative.Relationship.DAUGHTER,
                person.getRelatives().get(0).getRelationship());
    }

    @Test
    public void getRelativesReturnsInCorrectOrder() {
        Person dad = new Person("dad");
        person.setFather(dad);

        Person mom = new Person("mom");
        person.setMother(mom);

        Person wife = new Person("wife");
        wife.setGender(Gender.FEMALE);
        person.setSpouse(wife);

        Person son = new Person("son");
        son.setGender(Gender.MALE);
        son.setFather(person);

        List<Relative> relatives = person.getRelatives();

        assertEquals(Relative.Relationship.FATHER, relatives.get(0).getRelationship());
        assertEquals(Relative.Relationship.MOTHER, relatives.get(1).getRelationship());
        assertEquals(Relative.Relationship.WIFE, relatives.get(2).getRelationship());
        assertEquals(Relative.Relationship.SON, relatives.get(3).getRelationship());
    }

    @Test
    public void compareToSortsByFirstEvent() {
        Person person1 = new Person("p1");
        Event event1 = new Event("e1");
        event1.setYear(1);
        person1.addEvent(event1);

        Person person2 = new Person("p2");
        Event event2 = new Event("e2");
        event2.setYear(2);
        person2.addEvent(event2);

        assertEquals(event1.compareTo(event2), person1.compareTo(person2));
        assertEquals(event2.compareTo(event1), person2.compareTo(person1));
    }

    @Test
    public void compareToHandlesNoFirstEvent() {
        // neither has first event

        Person person1 = new Person("p1");
        Person person2 = new Person("p2");

        assertTrue(person1.compareTo(person2) == 0);
        assertTrue(person2.compareTo(person1) == 0);

        // only one has first event

        Event event1 = new Event("e1");
        event1.setYear(1);
        person1.addEvent(event1);

        assertTrue(person1.compareTo(person2) > 0);
        assertTrue(person2.compareTo(person1) < 0);
    }
}