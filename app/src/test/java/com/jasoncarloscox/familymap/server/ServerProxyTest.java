package com.jasoncarloscox.familymap.server;

import com.jasoncarloscox.familymap.model.Gender;
import com.jasoncarloscox.familymap.model.User;
import com.jasoncarloscox.familymap.server.request.EventsRequest;
import com.jasoncarloscox.familymap.server.request.LoginRequest;
import com.jasoncarloscox.familymap.server.request.PersonsRequest;
import com.jasoncarloscox.familymap.server.request.RegisterRequest;
import com.jasoncarloscox.familymap.server.result.EventsResult;
import com.jasoncarloscox.familymap.server.result.LoginResult;
import com.jasoncarloscox.familymap.server.result.PersonsResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerProxyTest {

    // TODO: change these tests to use a mock server, not the real thing

    ServerProxy server;

    @Before
    public void setup() throws Exception {
        ServerProxy.setHost("localhost");
        ServerProxy.setPort(8080);
        server = new ServerProxy();
    }

    @After
    public void cleanup() throws Exception {
    }

    @Test
    public void loginReturnsAuthTokenAndPersonId() {
        LoginRequest req = new LoginRequest("uname", "password");
        LoginResult res = server.login(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getAuthToken());
        assertNotNull(res.getPersonID());
    }

    @Test
    public void loginReturnsNotSuccessOnError() {
        LoginRequest req = new LoginRequest("uname", "invalid");
        LoginResult res = server.login(req);

        assertFalse(res.isSuccess());
    }

    @Test
    public void registerReturnsAuthTokenAndPersonId() {
        User newUser = new User("uname", "password");
        newUser.setFirstName("f");
        newUser.setLastName("l");
        newUser.setGender(Gender.MALE);
        newUser.setEmail("e");

        RegisterRequest req = new RegisterRequest(newUser);
        LoginResult res = server.register(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getAuthToken());
        assertNotNull(res.getPersonID());
    }

    @Test
    public void registerReturnsNotSuccessOnError() {
        User newUser = new User(null, null);

        RegisterRequest req = new RegisterRequest(newUser);
        LoginResult res = server.register(req);

        assertFalse(res.isSuccess());
    }

    @Test
    public void getPersonsReturnsPersons() {
        User newUser = new User("uname", "password");
        newUser.setFirstName("f");
        newUser.setLastName("l");
        newUser.setGender(Gender.MALE);
        newUser.setEmail("e");

        RegisterRequest req = new RegisterRequest(newUser);
        LoginResult res = server.register(req);

        assertTrue(res.isSuccess());

        PersonsRequest req2 = new PersonsRequest(res.getAuthToken());
        PersonsResult res2 = server.getPersons(req2);

        assertTrue(res2.isSuccess());
        assertNotNull(res2.getPersons());
    }

    @Test
    public void getPersonsReturnsNotSuccessOnError() {
        PersonsRequest req = new PersonsRequest("invalidtoken");
        PersonsResult res = server.getPersons(req);

        assertFalse(res.isSuccess());
    }

    @Test
    public void getEventsReturnsEvents() {
        User newUser = new User("uname", "password");
        newUser.setFirstName("f");
        newUser.setLastName("l");
        newUser.setGender(Gender.MALE);
        newUser.setEmail("e");

        RegisterRequest req = new RegisterRequest(newUser);
        LoginResult res = server.register(req);

        assertTrue(res.isSuccess());

        EventsRequest req2 = new EventsRequest(res.getAuthToken());
        EventsResult res2 = server.getEvents(req2);

        assertTrue(res2.isSuccess());
        assertNotNull(res2.getEvents());
    }

    @Test
    public void getEventsReturnsNotSuccessOnError() {
        EventsRequest req = new EventsRequest("invalidtoken");
        EventsResult res = server.getEvents(req);

        assertFalse(res.isSuccess());

    }
}