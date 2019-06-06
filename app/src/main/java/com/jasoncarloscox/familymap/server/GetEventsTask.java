package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.server.request.EventsRequest;
import com.jasoncarloscox.familymap.server.result.EventsResult;

public class GetEventsTask extends AsyncTask<EventsRequest, Object, EventsResult> {

    public interface Context {
        void onGetEventsComplete(EventsResult result);
    }

    private Context context;

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
