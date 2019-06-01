package com.jasoncarloscox.familymap.server.request;

/**
 * A generic API request.
 */
public abstract class ApiRequest {

    private String authToken; 

    /**
     * Creates a new ApiRequest without an authorization token.
     */
    public ApiRequest() {}

    /**
     * Creates a new ApiRequest that includes an authorization token.
     * 
     * @param authToken the authorization token sent with the request
     */
    public ApiRequest(String authToken) {
        setAuthToken(authToken);
    }

    /**
     * @return the authorization token sent with the request
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken the authorization token sent with the request
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * @return the path used to make this request
     */
    public abstract String getPath();

    /**
     * @return the HTTP method used to make this request
     */
    public abstract String getMethod();

    /**
     * @return whether a body needs to be sent with this request
     */
    public abstract boolean sendBody();

}