package com.jasoncarloscox.familymap.model;

/**
 * Container object for a person and his/her relationship to another person.
 */
public class Relative {

    /**
     * Possible relationship values
     */
    public enum Relationship { FATHER, MOTHER, HUSBAND, WIFE, SON, DAUGHTER }

    private Person person;
    private Relationship relationship;

    /**
     * Creates a new Relative.
     *
     * @param person the person
     * @param relationship the person's relationship to another person
     */
    public Relative(Person person, Relationship relationship) {
        this.person = person;
        this.relationship = relationship;
    }

    /**
     * @return the id of the person
     */
    public String getPersonId() {
        return person.getId();
    }

    /**
     * @return the first name of the person
     */
    public String getFirstName() {
        return person.getFirstName();
    }

    /**
     * @return the last name of the person
     */
    public String getLastName() {
        return person.getLastName();
    }

    /**
     * @return the person's full name
     */
    public String getFullName() {
        return person.getFullName();
    }

    /**
     * @return the gender of the person
     */
    public String getGender() {
        return person.getGender();
    }

    /**
     * @return the person's relationship to another person
     */
    public Relationship getRelationship() {
        return relationship;
    }


}
