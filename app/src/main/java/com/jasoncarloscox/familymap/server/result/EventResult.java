package com.jasoncarloscox.familymap.server.result;

import com.jasoncarloscox.familymap.model.Event;

/**
 * The result of a request to the <code>/event/[eventID]</code> API route. It
 * describes the outcome of an attempt to add one event to the database.
 */
public class EventResult extends ApiResult {

    /**
     * The error message used when the requested event doesn't belong to the 
     * user associated with the provided authorization token.
     */
    public static final String NOT_USERS_EVENT_ERROR = 
        "The requested event belongs to a different user";

    /**
     * The error message used when the requested event isn't found.
     */
    public static final String EVENT_NOT_FOUND_ERROR = 
        "The requested event could not be found";

    private String eventID;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Creates a new error EventResult.
     *
     * @param message a description of the error
     */
    public EventResult(String message) {
        super(false, message);
    }

    /**
     * @return the retrieved event
     */
    public Event getEvent() {
        Event e = new Event(eventID);
        e.setPersonId(personID);
        e.setLatitude(latitude);
        e.setLongitude(longitude);
        e.setCountry(country);
        e.setCity(city);
        e.setType(eventType);
        e.setYear(year);

        return e;
    }

}