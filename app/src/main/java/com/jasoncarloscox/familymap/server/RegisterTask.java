package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.server.request.RegisterRequest;
import com.jasoncarloscox.familymap.server.result.LoginResult;

/**
 * AsyncTask to register a new user with the server
 */
public class RegisterTask extends AsyncTask<RegisterRequest, Object, LoginResult> {

    /**
     * Implement this interface to be alerted when the task completes.
     */
    public interface Context {

        /**
         * Called when the task completes.
         * @param result the result of the task
         */
        void onRegisterComplete(LoginResult result);
    }

    private Context context;

    /**
     * Creates a new RegisterTask
     *
     * @param context the context
     */
    public RegisterTask(Context context) {
        this.context = context;
    }

    @Override
    protected LoginResult doInBackground(RegisterRequest... requests) {
        ServerProxy server = new ServerProxy();

        return server.register(requests[0]);
    }

    @Override
    protected void onPostExecute(LoginResult result) {
        context.onRegisterComplete(result);
    }
}
