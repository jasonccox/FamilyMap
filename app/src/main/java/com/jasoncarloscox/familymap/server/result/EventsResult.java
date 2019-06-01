package com.jasoncarloscox.familymap.server.result;

import com.jasoncarloscox.familymap.model.Event;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The result of a request to the <code>/event</code> route. It describes the 
 * outcome of an attempt to retrieve multiple events from the database.
 */
public class EventsResult extends ApiResult {

    private Collection<EventResult> data;

    /**
     * Creates a new error EventsResult.
     * 
     * @param message a description of the error
     */
    public EventsResult(String message) {
        super(false, message);
    }

    /**
     * @return the retrieved events
     */
    public Collection<Event> getEvents() {
        if (data == null) {
            return null;
        }

        ArrayList<Event> events = new ArrayList<>();

        for (EventResult er : data) {
            events.add(er.getEvent());
        }

        return events;
    }

}