package com.jasoncarloscox.familymap.server.result;

import com.jasoncarloscox.familymap.model.Person;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The result of a request to the <code>/person</code> API route. It describes 
 * the outcome of an attempt to retrieve multiple persons from the database.
 */
public class PersonsResult extends ApiResult {

    private Collection<PersonResult> data;

    /**
     * Creates a new error PersonsResult.
     * 
     * @param message a description of the error
     */
    public PersonsResult(String message) {
        super(false, message);
    }

    /**
     * @return the retrieved persons
     */
    public Collection<Person> getPersons() {
        if (data == null) {
            return null;
        }

        ArrayList<Person> persons = new ArrayList<>();

        for (PersonResult pr : data) {
            persons.add(pr.getPerson());
        }

        return persons;
    }

}