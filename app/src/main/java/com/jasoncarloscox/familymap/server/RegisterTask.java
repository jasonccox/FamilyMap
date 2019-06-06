package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.server.request.RegisterRequest;
import com.jasoncarloscox.familymap.server.result.LoginResult;

public class RegisterTask extends AsyncTask<RegisterRequest, Object, LoginResult> {

    public interface Context {
        void onRegisterComplete(LoginResult result);
    }

    private Context context;

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
