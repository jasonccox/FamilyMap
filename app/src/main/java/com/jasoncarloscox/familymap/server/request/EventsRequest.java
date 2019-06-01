package com.jasoncarloscox.familymap.server.request;

/**
 * A request to the <code>/event/[eventId]</code> route. It is a request to
 * retrieve one event from the database.
 */
public class EventsRequest extends ApiRequest {

    private String eventId;

    /**
     * Creates a new EventsRequest.
     * 
     * @param authToken the authorization token sent with the request
     */
    public EventsRequest(String authToken) {
        super(authToken);
    }

    /**
     * @return the path used to make this request
     */
    @Override
    public String getPath() {
        return "/event";
    }

    /**
     * @return the HTTP method used to make this request
     */
    @Override
    public String getMethod() {
        return "GET";
    }

    /**
     * @return whether a body needs to be sent with this request
     */
    @Override
    public boolean sendBody() {
        return false;
    }
    
}