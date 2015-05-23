package com.android.lagger.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.requestObjects.EditMeetingRequest;
import com.android.lagger.responseObjects.EditMeetingResponse;
import com.android.lagger.services.HttpClient;

/**
 * Created by Ewelina Klisowska on 2015-05-23.
 */
public class EditMeetingTask extends AsyncTask<EditMeetingRequest, Void, EditMeetingResponse> {
    private Context context;
    private HttpClient client;

    public EditMeetingTask(Context context) {
        this.context = context;
        client = new HttpClient(context);
    }

    protected EditMeetingResponse doInBackground(EditMeetingRequest... editMeetingRequests) {
        return client.editMeeting(editMeetingRequests[0]);
    }

    protected void onPostExecute(EditMeetingResponse resp) {
        if (!resp.isError()) {
           showInfo(context.getResources().getString(R.string.saved_changes));
        } else {
           showInfo(resp.getResponse());
        }
    }

    private void showInfo(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
