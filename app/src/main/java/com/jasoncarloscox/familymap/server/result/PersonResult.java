package com.jasoncarloscox.familymap.server.result;

import com.jasoncarloscox.familymap.model.Person;

/**
 * The result of a request to the <code>/person/[personID]</code> API route. It 
 * describes the outcome of an attempt to retrieve one person from the database.
 */
public class PersonResult extends ApiResult {

    /**
     * The error message used when the requested person doesn't belong to the 
     * user associated with the provided authorization token.
     */
    public static final String NOT_USERS_PERSON_ERROR = 
        "The requested person belongs to a different user";

    /**
     * The error message used when the requested person isn't found.
     */
    public static final String PERSON_NOT_FOUND_ERROR = 
        "The requested person could not be found";

    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    /**
     * Creates a new error PersonResult.
     * 
     * @param message a description of the error
     */
    public PersonResult(String message) {
        super(false, message);
    }

    /**
     * @return the retrieved person
     */
    public Person getPerson() {
        Person p = new Person(personID);
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setGender(gender);
        p.setFatherID(fatherID);
        p.setMotherID(motherID);
        p.setSpouseID(spouseID);

        return p;
    }
    
}