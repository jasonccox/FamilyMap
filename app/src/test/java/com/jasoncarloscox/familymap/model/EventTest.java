package com.jasoncarloscox.familymap.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void compareToBirthIsLess() {
        Event birth = new Event("b");
        birth.setType("birth");

        Event notBirth = new Event("nb");
        notBirth.setType("notbirth");

        assertTrue(birth.compareTo(notBirth) < 0);
        assertTrue(notBirth.compareTo(birth) > 0);
    }

    @Test
    public void compareToSortsMultipleBirthsByYear() {
        Event birth1 = new Event("b1");
        birth1.setType("birth");
        birth1.setYear(0);

        Event birth2 = new Event("b2");
        birth2.setType("birth");
        birth2.setYear(1);

        assertTrue(birth1.compareTo(birth2) < 0);
        assertTrue(birth2.compareTo(birth1) > 0);
    }

    @Test
    public void compareToDeathIsGreater() {
        Event death = new Event("d");
        death.setType("death");

        Event notDeath = new Event("nd");
        notDeath.setType("notDeath");

        assertTrue(death.compareTo(notDeath) > 0);
        assertTrue(notDeath.compareTo(death) < 0);
    }

    @Test
    public void compareToSortsMultipleDeathsByYear() {
        Event death1 = new Event("d1");
        death1.setType("death");
        death1.setYear(0);

        Event death2 = new Event("d2");
        death2.setType("death");
        death2.setYear(1);

        assertTrue(death1.compareTo(death2) < 0);
        assertTrue(death2.compareTo(death1) > 0);
    }

    @Test
    public void compareToSortsByYear() {
        Event event1 = new Event("e1");
        event1.setYear(0);

        Event event2 = new Event("e2");
        event2.setYear(1);

        Event event3 = new Event("e3");
        event3.setYear(0);

        assertTrue(event1.compareTo(event2) < 0);
        assertTrue(event2.compareTo(event1) > 0);
        assertTrue(event1.compareTo(event3) == 0);
        assertTrue(event3.compareTo(event1) == 0);
    }

    @Test
    public void compareToSortsByLowercaseTypeIfYearEqual() {
        Event event1 = new Event("e1");
        event1.setYear(0);
        event1.setType("A");

        Event event2 = new Event("e2");
        event2.setYear(0);
        event2.setType("b");

        assertTrue(event1.compareTo(event2) < 0);
        assertTrue(event2.compareTo(event1) > 0);
    }

    @Test
    public void compareToSortsCorrectlyIfYearEqualAndTypeNull() {
        Event event1 = new Event("e1");
        event1.setYear(0);
        event1.setType("a");

        Event event2 = new Event("e2");
        event2.setYear(0);

        Event event3 = new Event("e3");
        event3.setYear(0);

        assertTrue(event1.compareTo(event2) > 0);
        assertTrue(event2.compareTo(event1) < 0);
        assertTrue(event2.compareTo(event3) == 0);
    }

}