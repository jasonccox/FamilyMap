package com.jasoncarloscox.familymap.server;

import android.util.Log;

import com.jasoncarloscox.familymap.model.Gender;
import com.jasoncarloscox.familymap.model.User;
import com.jasoncarloscox.familymap.server.request.EventsRequest;
import com.jasoncarloscox.familymap.server.request.LoginRequest;
import com.jasoncarloscox.familymap.server.request.PersonsRequest;
import com.jasoncarloscox.familymap.server.request.RegisterRequest;
import com.jasoncarloscox.familymap.server.result.EventsResult;
import com.jasoncarloscox.familymap.server.result.LoginResult;
import com.jasoncarloscox.familymap.server.result.PersonsResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServerProxy.class, Log.class})
public class ServerProxyTest {

    ServerProxy server;

    HttpURLConnection mockConn;

    @Before
    public void setup() throws Exception {
        ServerProxy.setHost("localhost");
        ServerProxy.setPort(8080);
        server = new ServerProxy();

        // mock the HttpURLConnection in order to test without the server
        URL url = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
        mockConn = PowerMockito.mock(HttpURLConnection.class);
        PowerMockito.when(url.openConnection()).thenReturn(mockConn);
        PowerMockito.doNothing().when(mockConn).connect();
        PowerMockito.when(mockConn.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        // mock the Log class
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void loginReturnsAuthTokenAndPersonId() throws IOException {
        String mockResBody = "{\"authToken\":\"abc\", \"personID\":\"pid\"}";
        setMockResponse(HttpURLConnection.HTTP_OK, mockResBody);

        LoginRequest req = new LoginRequest("uname", "password");
        LoginResult res = server.login(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getAuthToken());
        assertNotNull(res.getPersonID());
    }

    @Test
    public void loginReturnsNotSuccessAndMessageOnError() throws IOException {
        String mockResBody = "{\"message\":\"abc\"}";
        setMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, mockResBody);

        LoginRequest req = new LoginRequest("uname", "invalid");
        LoginResult res = server.login(req);

        assertFalse(res.isSuccess());
        assertNotNull(res.getMessage());
    }

    @Test
    public void loginReturnsLoginResultWhenConnectFails() throws IOException {
        PowerMockito.doThrow(new IOException()).when(mockConn).connect();

        LoginRequest req = new LoginRequest("uname", "password");
        assertTrue(server.login(req) instanceof LoginResult);
    }

    @Test
    public void registerReturnsAuthTokenAndPersonId() throws IOException {
        String mockResBody = "{\"authToken\":\"abc\", \"personID\":\"pid\"}";
        setMockResponse(HttpURLConnection.HTTP_OK, mockResBody);

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
    public void registerReturnsNotSuccessAndMessageOnError() throws IOException {
        String mockResBody = "{\"message\":\"abc\"}";
        setMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, mockResBody);

        RegisterRequest req = new RegisterRequest(new User(null, null));
        LoginResult res = server.register(req);

        assertFalse(res.isSuccess());
        assertNotNull(res.getMessage());
    }

    @Test
    public void registerReturnsLoginResultWhenConnectFails() throws IOException {
        PowerMockito.doThrow(new IOException()).when(mockConn).connect();

        User newUser = new User("uname", "password");
        newUser.setFirstName("f");
        newUser.setLastName("l");
        newUser.setGender(Gender.MALE);
        newUser.setEmail("e");

        RegisterRequest req = new RegisterRequest(newUser);;
        assertTrue(server.register(req) instanceof LoginResult);
    }

    @Test
    public void getPersonsReturnsPersons() throws IOException {
        String mockResBody = "{\"data\":[{\"personID\":\"p\"}]}";
        setMockResponse(HttpURLConnection.HTTP_OK, mockResBody);

        PersonsRequest req = new PersonsRequest("token");
        PersonsResult res = server.getPersons(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getPersons());
    }

    @Test
    public void getPersonsReturnsNotSuccessAndMessageOnError() throws IOException {
        String mockResBody = "{\"message\":\"abc\"}";
        setMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, mockResBody);

        PersonsRequest req = new PersonsRequest("invalidtoken");
        PersonsResult res = server.getPersons(req);

        assertFalse(res.isSuccess());
        assertNotNull(res.getMessage());
    }

    @Test
    public void getPersonsReturnsPersonsResultWhenConnectFails() throws IOException {
        PowerMockito.doThrow(new IOException()).when(mockConn).connect();

        PersonsRequest req = new PersonsRequest("token");
        assertTrue(server.getPersons(req) instanceof PersonsResult);
    }

    @Test
    public void getEventsReturnsEvents() throws IOException {
        String mockResBody = "{\"data\":[{\"eventID\":\"eid\"}]}";
        setMockResponse(HttpURLConnection.HTTP_OK, mockResBody);

        EventsRequest req = new EventsRequest("token");
        EventsResult res = server.getEvents(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getEvents());
    }

    @Test
    public void getEventsReturnsNotSuccessAndMessageOnError() throws IOException {
        String mockResBody = "{\"message\":\"abc\"}";
        setMockResponse(HttpURLConnection.HTTP_BAD_REQUEST, mockResBody);

        EventsRequest req = new EventsRequest("invalidtoken");
        EventsResult res = server.getEvents(req);

        assertFalse(res.isSuccess());
        assertNotNull(res.getMessage());
    }

    @Test
    public void getEventsReturnsEventsResultWhenConnectFails() throws IOException {
        PowerMockito.doThrow(new IOException()).when(mockConn).connect();

        EventsRequest req = new EventsRequest("token");
        assertTrue(server.getEvents(req) instanceof EventsResult);
    }

    private void setMockResponse(int code, String body) throws IOException {
        PowerMockito.when(mockConn.getResponseCode()).thenReturn(code);

        InputStream mockResponseStream = new ByteArrayInputStream(body.getBytes());

        if (code >= HttpURLConnection.HTTP_BAD_REQUEST) {
            PowerMockito.when(mockConn.getErrorStream())
                    .thenReturn(mockResponseStream);
        } else {
            PowerMockito.when(mockConn.getInputStream())
                    .thenReturn(mockResponseStream);
        }
    }
}