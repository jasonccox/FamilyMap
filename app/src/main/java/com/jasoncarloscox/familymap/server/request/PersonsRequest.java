package com.jasoncarloscox.familymap.server.request;

/**
 * A request to the <code>/person/[personId]</code> route. It is a request to
 * retrieve one person from the database.
 */
public class PersonsRequest extends ApiRequest {

    private String personId;

    /**
     * Creates a new PersonsRequest.
     * 
     * @param authToken the authorization token sent with the request
     */
    public PersonsRequest(String authToken) {
        super(authToken);
    }

    /**
     * @return the path used to make this request
     */
    @Override
    public String getPath() {
        return "/person";
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