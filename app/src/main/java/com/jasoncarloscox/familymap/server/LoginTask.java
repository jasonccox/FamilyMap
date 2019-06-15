package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.server.request.LoginRequest;
import com.jasoncarloscox.familymap.server.result.LoginResult;

/**
 * AsyncTask to log a user into the server
 */
public class LoginTask extends AsyncTask<LoginRequest, Object, LoginResult> {

    /**
     * Implement this interface to be alerted when the task completes.
     */
    public interface Context {

        /**
         * Called when the task completes.
         * @param result the result of the task
         */
        void onLoginComplete(LoginResult result);
    }

    private Context context;

    /**
     * Creates a new LoginTask
     *
     * @param context the context
     */
    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected LoginResult doInBackground(LoginRequest... requests) {
        ServerProxy server = new ServerProxy();

        return server.login(requests[0]);
    }

    @Override
    protected void onPostExecute(LoginResult result) {
        context.onLoginComplete(result);
    }
}
