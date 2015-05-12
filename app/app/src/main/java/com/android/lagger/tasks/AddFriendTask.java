package com.android.lagger.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.requestObjects.AddFriendRequest;
import com.android.lagger.requestObjects.AddMeetingRequest;
import com.android.lagger.responseObjects.AddFriendResponse;
import com.android.lagger.services.HttpClient;

/**
 * Created by Ewelina Klisowska on 2015-05-12.
 */
public class AddFriendTask extends AsyncTask<AddFriendRequest, Void, AddFriendResponse> {
    private Context context;
    private HttpClient client;

    public AddFriendTask(Context context) {
        this.context = context;
        client = new HttpClient();
    }

    @Override
    protected AddFriendResponse doInBackground(AddFriendRequest... params) {
        return client.addFriend(params[0]);
    }

    protected void onPostExecute(final AddFriendResponse resp) {
        String info = context.getString(R.string.invited_a_new_friend);
        showInfo(info);
    }

    private void showInfo(String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}