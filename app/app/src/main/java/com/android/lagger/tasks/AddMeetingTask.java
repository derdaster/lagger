package com.android.lagger.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.requestObjects.AddMeetingRequest;
import com.android.lagger.responseObjects.AddMeetingResponse;
import com.android.lagger.services.HttpClient;

/**
 * Created by Ewelina Klisowska on 2015-05-22.
 */
public class AddMeetingTask extends AsyncTask<AddMeetingRequest, Void, AddMeetingResponse> {
    private Context context;
    private HttpClient client;

    public AddMeetingTask(Context context) {
        this.context = context;
        client = new HttpClient(context);
    }

    @Override
    protected AddMeetingResponse doInBackground(AddMeetingRequest... params) {
        return client.createNewMeeting(params[0]);
    }

    protected void onPostExecute(final AddMeetingResponse resp) {
        String info = null;
        if (!resp.isError()) {
            info = context.getString(R.string.create_meeting);
        } else {
            info = resp.getResponse();
        }
        showInfo(info);
    }

    private void showInfo(String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}