package com.jasoncarloscox.familymap.server.request;

import com.jasoncarloscox.familymap.model.User;

/**
 * A request to the <code>/user/register</code> route. It is a request to
 * register a new user with the server.
 */
public class RegisterRequest extends ApiRequest {

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     * Creates a new RegisterRequest.
     * 
     * @param user the user to be added - note that the personId field will be 
     *             blank
     */
    public RegisterRequest(User user) {
        super();
        setUser(user);
    }

    /**
     * @param user the new user - note that the personId field will be blank
     */
    public void setUser(User user) {
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
    }

    /**
     * @return the path used to make this request
     */
    @Override
    public String getPath() {
        return "/user/register";
    }

    /**
     * @return the HTTP method used to make this request
     */
    @Override
    public String getMethod() {
        return "POST";
    }

    /**
     * @return whether a body needs to be sent with this request
     */
    @Override
    public boolean sendBody() {
        return true;
    }

}