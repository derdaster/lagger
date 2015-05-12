package com.android.lagger.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.requestObjects.RemoveFriendRequest;
import com.android.lagger.responseObjects.RemoveFriendResponse;
import com.android.lagger.services.HttpClient;

/**
 * Created by Ewelina Klisowska on 2015-05-12.
 */
public class RemoveFriendTask extends AsyncTask<RemoveFriendRequest, Void, RemoveFriendResponse> {
    private Context context;
    private HttpClient client;

    public RemoveFriendTask(Context context) {
        this.context = context;
        client = new HttpClient();
    }

    @Override
    protected RemoveFriendResponse doInBackground(RemoveFriendRequest... params) {
        return client.removeFriend(params[0]);
    }

    protected void onPostExecute(final RemoveFriendResponse resp) {
        String info = context.getString(R.string.removed_a_friend);
        showInfo(info);
    }

    private void showInfo(String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}

