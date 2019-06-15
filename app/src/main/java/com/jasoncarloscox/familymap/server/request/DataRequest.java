package com.jasoncarloscox.familymap.server.request;

/**
 * A request for Persons and Events.
 */
public class DataRequest {

    private PersonsRequest personsRequest;
    private EventsRequest eventsRequest;

    /**
     * Creates a new DataRequest.
     *
     * @param authToken the authorization token to use for the API calls
     */
    public DataRequest(String authToken) {
        this.personsRequest = new PersonsRequest(authToken);
        this.eventsRequest = new EventsRequest(authToken);
    }

    /**
     * @return the PersonsRequest associated with this DataRequest
     */
    public PersonsRequest getPersonsRequest() {
        return personsRequest;
    }

    /**
     * @return the EventsRequest associated with this DataRequest
     */
    public EventsRequest getEventsRequest() {
        return eventsRequest;
    }
}
