package com.jasoncarloscox.familymap.server;

import android.os.AsyncTask;

import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.server.request.DataRequest;
import com.jasoncarloscox.familymap.server.result.ApiResult;
import com.jasoncarloscox.familymap.server.result.EventsResult;
import com.jasoncarloscox.familymap.server.result.PersonsResult;

public class GetDataTask extends AsyncTask<DataRequest, Object, Object>
        implements GetPersonsTask.Context, GetEventsTask.Context {

    public interface Context {
        void onGetDataComplete(ApiResult result);
    }

    private Context context;
    private String rootPersonId;
    private PersonsResult personsResult;
    private EventsResult eventsResult;

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

    @Override
    public void onGetEventsComplete(EventsResult result) {
        eventsResult = result;

        if (personsResult != null) {
            processResults();
        }
    }

    @Override
    public void onGetPersonsComplete(PersonsResult result) {
        personsResult = result;

        if (eventsResult != null) {
            processResults();
        }
    }

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

    private String generateResultMessage(ApiResult... results) {
        StringBuilder msg = new StringBuilder();

        for (ApiResult result : results) {
            msg.append(result.getMessage() + "\n");
        }

        return msg.toString();
    }
}
