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
    private Boolean isInvitation;

    public RemoveFriendTask(Context context, Boolean isInvitation) {
        this.context = context;
        this.isInvitation = isInvitation;
        client = new HttpClient(context);
    }

    @Override
    protected RemoveFriendResponse doInBackground(RemoveFriendRequest... params) {
        return client.removeFriend(params[0]);
    }

    protected void onPostExecute(final RemoveFriendResponse resp) {
        String info = null;
        if (!resp.isError()) {
            info = context.getString(R.string.removed_a_friend);
            if (isInvitation) {
                info = context.getString(R.string.refused_invitation_to_friends);
            }
        } else {
            info = resp.getResponse();
        }

        showInfo(info);
    }

    private void showInfo(String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}

