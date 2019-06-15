package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.server.request.EventsRequest;
import com.jasoncarloscox.familymap.server.result.EventsResult;

/**
 * AsyncTask to fetch events from the server.
 */
public class GetEventsTask extends AsyncTask<EventsRequest, Object, EventsResult> {

    /**
     * Implement this interface to be alerted when the task completes.
     */
    public interface Context {

        /**
         * Called when the task completes.
         * @param result the result of the task
         */
        void onGetEventsComplete(EventsResult result);
    }

    private Context context;

    /**
     * Creates a new GetEventsTask
     *
     * @param context the context
     */
    public GetEventsTask(Context context) {
        this.context = context;
    }

    @Override
    protected EventsResult doInBackground(EventsRequest... requests) {
        ServerProxy server = new ServerProxy();

        return server.getEvents(requests[0]);
    }

    @Override
    protected void onPostExecute(EventsResult result) {
        context.onGetEventsComplete(result);
    }
}
