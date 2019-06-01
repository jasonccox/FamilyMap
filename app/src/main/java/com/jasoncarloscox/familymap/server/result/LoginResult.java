package com.jasoncarloscox.familymap.server.result;

/**
 * The result of a request to the <code>/user/login</code> or 
 * <code>/user/register</code> route. It describes the outcome of an attempt to 
 * log a user into or register a new user with the server.
 */
public class LoginResult extends ApiResult {

    /**
     * The error message used when a username has already been taken by another 
     * user.
     */
    public static final String USERNAME_TAKEN_ERROR = 
        "Username already taken by another user";

    /**
     * The error message used when a password is incorrect for the given user.
     */
    public static final String WRONG_PASSWORD_ERROR = 
        "Incorrect password for the given user";

    private String authToken;
    private String userName;
    private String personID;

    /**
     * Cretaes a new error LoginResult.
     * 
     * @param error the error message
     */
    public LoginResult(String error) {
        super(false, error);
    }

    /**
     * @return the authorization token for the logged in user
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @return the id of the person representing the logged in user in the family 
     *         map
     */
    public String getPersonID() {
        return personID;
    }

}