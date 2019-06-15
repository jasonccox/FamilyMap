package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.server.request.PersonsRequest;
import com.jasoncarloscox.familymap.server.result.PersonsResult;

/**
 * AsyncTask to fetch persons from the server.
 */
public class GetPersonsTask extends AsyncTask<PersonsRequest, Object, PersonsResult> {

    /**
     * Implement this interface to be alerted when the task completes.
     */
    public interface Context {

        /**
         * Called when the task completes.
         * @param result the result of the task
         */
        void onGetPersonsComplete(PersonsResult result);
    }

    private Context context;

    /**
     * Creates a new GetPersonsTask
     *
     * @param context the context
     */
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
