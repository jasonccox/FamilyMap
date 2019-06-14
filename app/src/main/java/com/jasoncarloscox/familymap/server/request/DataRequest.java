package com.jasoncarloscox.familymap.server.request;

public class DataRequest {

    private PersonsRequest personsRequest;
    private EventsRequest eventsRequest;

    public DataRequest(String authToken) {
        this.personsRequest = new PersonsRequest(authToken);
        this.eventsRequest = new EventsRequest(authToken);
    }

    public PersonsRequest getPersonsRequest() {
        return personsRequest;
    }

    public EventsRequest getEventsRequest() {
        return eventsRequest;
    }
}
