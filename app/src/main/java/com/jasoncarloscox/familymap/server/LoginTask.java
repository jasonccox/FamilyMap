package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.server.request.LoginRequest;
import com.jasoncarloscox.familymap.server.result.LoginResult;

public class LoginTask extends AsyncTask<LoginRequest, Object, LoginResult> {

    public interface Context {
        void onLoginComplete(LoginResult result);
    }

    private Context context;

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
