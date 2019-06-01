package com.jasoncarloscox.familymap.server;

import android.util.Log;

import com.jasoncarloscox.familymap.server.request.ApiRequest;
import com.jasoncarloscox.familymap.server.request.EventsRequest;
import com.jasoncarloscox.familymap.server.request.LoginRequest;
import com.jasoncarloscox.familymap.server.request.PersonsRequest;
import com.jasoncarloscox.familymap.server.request.RegisterRequest;
import com.jasoncarloscox.familymap.server.result.ApiResult;
import com.jasoncarloscox.familymap.server.result.EventsResult;
import com.jasoncarloscox.familymap.server.result.LoginResult;
import com.jasoncarloscox.familymap.server.result.PersonsResult;
import com.jasoncarloscox.familymap.util.ObjectEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerProxy {

    private static final String TAG = "ServerProxy";
    private static final String AUTH_HEADER = "Authorization";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String ACCEPT_VALUE = "application/json";

    private static String host = "localhost";
    private static int port = 8080;

    /**
     * @param host the hostname of the server to receive requests
     *             (e.g. www.example.com)
     */
    public static void setHost(String host) {
        ServerProxy.host = host;
    }

    /**
     * @param port the port number of the server to receive requests
     */
    public static void setPort(int port) {
        ServerProxy.port = port;
    }

    /**
     * Verifies a user's credentials and provides an auth token for future
     * requests as well as the id of the person representing the user in the
     * family tree.
     *
     * @param request the login request, which stores the username and password
     * @return a login result, which contains the auth token and person id
     */
    public LoginResult login(LoginRequest request) {
        return (LoginResult) makeRequest(request, LoginResult.class);
    }

    /**
     * Creates a new user, generates a family tree for the user and provides an
     * auth token for future requests as well as the id of the person
     * representing the user in the family tree.
     *
     * @param request the login request, which stores the username and password
     * @return a login result, which contains the auth token and person id
     */
    public LoginResult register(RegisterRequest request) {
        return (LoginResult) makeRequest(request, LoginResult.class);
    }

    /**
     * Retrieves all of the persons from a user's family tree.
     *
     * @param request the persons request, which contains an auth token
     * @return a persons result, which contains the retrieved persons
     */
    public PersonsResult getPersons(PersonsRequest request) {
        return (PersonsResult) makeRequest(request, PersonsResult.class);
    }

    /**
     * Retrieves all of the events from a user's family tree.
     *
     * @param request the events request, which contains an auth token
     * @return a events result, which contains the retrieved events
     */
    public EventsResult getEvents(EventsRequest request) {
        return (EventsResult) makeRequest(request, EventsResult.class);
    }

    /**
     * Makes an HTTP request.
     *
     * @param request an object containing data about the request
     * @param resultClass the type of result that should be returned - this must
     *                    be either ApiResult or a subclass of ApiResult
     * @return an object of type resultClass containing the result of the request
     */
    private ApiResult makeRequest(ApiRequest request, Class resultClass) {
        assert ApiResult.class.isAssignableFrom(resultClass);

        try {
            URL url = new URL("http://" + host + ":" + port + request.getPath());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod(request.getMethod());
            http.addRequestProperty(ACCEPT_HEADER, ACCEPT_VALUE);

            if (request.getAuthToken() != null) {
                http.addRequestProperty(AUTH_HEADER, request.getAuthToken());
            }

            if (request.sendBody()) {
                http.setDoOutput(true);
            } else {
                http.setDoOutput(false);
            }

            http.connect();

            if (request.sendBody()) {
                writeToStream(ObjectEncoder.serialize(request), http.getOutputStream());
                http.getOutputStream().close();
            }

            if (http.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                Log.i(TAG, "Request failed: " + http.getResponseMessage());

                ApiResult result = (ApiResult)
                        ObjectEncoder.deserialize(http.getErrorStream(), resultClass);
                result.setSuccess(false);
                return result;
            }

            ApiResult result = (ApiResult)
                    ObjectEncoder.deserialize(http.getInputStream(), resultClass);
            result.setSuccess(true);
            return result;

        } catch (IOException e) {
            Log.e(TAG, "Failed to make request: " + request.getClass().getSimpleName(), e);
            return new ApiResult(false, "Error connecting to server. " +
                                        "Please try again later.");
        }
    }

    /**
     * Writes a string to an OutputStream.
     *
     * @param string the String to be written
     * @param stream the OutputStream to which to write
     * @throws IOException if an I/O error occurs
     */
    protected void writeToStream(String string, OutputStream stream)
            throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(stream);
        writer.write(string);
        writer.flush();
    }
}
