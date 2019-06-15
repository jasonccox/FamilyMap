package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.server.request.DataRequest;
import com.jasoncarloscox.familymap.server.result.ApiResult;
import com.jasoncarloscox.familymap.server.result.EventsResult;
import com.jasoncarloscox.familymap.server.result.PersonsResult;

/**
 * AsyncTask to fetch persons and events from a server and load them into the
 * model.
 */
public class GetDataTask extends AsyncTask<DataRequest, Object, Object>
        implements GetPersonsTask.Context, GetEventsTask.Context {

    /**
     * Implement this interface to be alerted when the task completes.
     */
    public interface Context {

        /**
         * Called when the task completes.
         * @param result the result of the task
         */
        void onGetDataComplete(ApiResult result);
    }

    private Context context;
    private String rootPersonId;
    private PersonsResult personsResult;
    private EventsResult eventsResult;

    /**
     * Creates a new GetDataTask
     *
     * @param context the context
     * @param rootPersonId the id of the root person in the family tree
     */
    public GetDataTask(Context context, String rootPersonId) {
        this.context = context;
        this.rootPersonId = rootPersonId;
    }

    @Override
    protected Object doInBackground(DataRequest... dataRequests) {
        new GetPersonsTask(this).execute(dataRequests[0].getPersonsRequest());
        new GetEventsTask(this).execute(dataRequests[0].getEventsRequest());

        return null;
    }

    @Override // from GetEventsTask.Context
    public void onGetEventsComplete(EventsResult result) {
        eventsResult = result;

        if (personsResult != null) {
            processResults();
        }
    }

    @Override // from GetPersonsTask.Context
    public void onGetPersonsComplete(PersonsResult result) {
        personsResult = result;

        if (eventsResult != null) {
            processResults();
        }
    }

    /**
     * Processes the results of getting the data, either storing it in the model
     * or reporting an error.
     */
    private void processResults() {
        if (personsResult.isSuccess() && eventsResult.isSuccess()) {
            Model.instance().load(personsResult.getPersons(), rootPersonId,
                    eventsResult.getEvents());

            context.onGetDataComplete(new ApiResult(true, null));
        } else {
            String errorMsg = generateResultMessage(personsResult, eventsResult);
            context.onGetDataComplete(new ApiResult(false, errorMsg));
        }
    }

    /**
     * @param results various ApiResults
     * @return the combined error message from all the results
     */
    private String generateResultMessage(ApiResult... results) {
        StringBuilder msg = new StringBuilder();

        for (ApiResult result : results) {
            msg.append(result.getMessage() + "\n");
        }

        return msg.toString();
    }
}
