package com.jasoncarloscox.familymap.model;

public class Relative {

    public enum Relationship { FATHER, MOTHER, HUSBAND, WIFE, SON, DAUGHTER }

    private Person person;
    private Relationship relationship;

    public Relative(Person person, Relationship relationship) {
        this.person = person;
        this.relationship = relationship;
    }

    public String getPersonId() {
        return person.getId();
    }

    public String getFirstName() {
        return person.getFirstName();
    }

    public String getLastName() {
        return person.getLastName();
    }

    public String getGender() {
        return person.getGender();
    }

    public Relationship getRelationship() {
        return relationship;
    }


}
