package com.android.lagger.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.requestObjects.RemoveFriendRequest;
import com.android.lagger.requestObjects.RemoveMeetingRequest;
import com.android.lagger.responseObjects.RemoveFriendResponse;
import com.android.lagger.responseObjects.RemoveMeetingResponse;
import com.android.lagger.services.HttpClient;

/**
 * Created by Ewelina Klisowska on 2015-05-12.
 */
public class RemoveMeetingTask extends AsyncTask<RemoveMeetingRequest, Void, RemoveMeetingResponse> {
    private Context context;
    private HttpClient client;

    public RemoveMeetingTask(Context context) {
        this.context = context;
        client = new HttpClient();
    }

    @Override
    protected RemoveMeetingResponse doInBackground(RemoveMeetingRequest... params) {
        return client.removeMeeting(params[0]);
    }

    protected void onPostExecute(final RemoveMeetingResponse resp) {
        String info = context.getString(R.string.removed_a_meeting);
        showInfo(info);
    }

    private void showInfo(String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}