package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.server.request.PersonsRequest;
import com.jasoncarloscox.familymap.server.result.PersonsResult;

public class GetPersonsTask extends AsyncTask<PersonsRequest, Object, PersonsResult> {

    public interface Context {
        void onGetPersonsComplete(PersonsResult result);
    }

    private Context context;

    public GetPersonsTask(Context context) {
        this.context = context;
    }

    @Override
    protected PersonsResult doInBackground(PersonsRequest... requests) {
        ServerProxy server = new ServerProxy();

        return server.getPersons(requests[0]);
    }

    @Override
    protected void onPostExecute(PersonsResult result) {
        context.onGetPersonsComplete(result);
    }
}
