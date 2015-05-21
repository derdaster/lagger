package com.android.lagger.tasks;

import android.content.Context;
import android.os.AsyncTask;

import android.widget.ListView;
import android.widget.Toast;

import com.android.lagger.logic.adapters.FriendsListAdapter;
import com.android.lagger.model.entities.User;
import com.android.lagger.requestObjects.GetFriendsRequest;
import com.android.lagger.responseObjects.GetFriendsResponse;
import com.android.lagger.services.HttpClient;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-21.
 */
public class GetFriendsTask extends AsyncTask<GetFriendsRequest, Void, GetFriendsResponse> {
    private Context context;
    private HttpClient client;
    private ListView mList;
    private FriendsListAdapter adapter;
    private List<User> friendsList;

    public GetFriendsTask(Context context, ListView mList) {
        this.context = context;
        this.mList = mList;
        client = new HttpClient(context);
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    @Override
    protected GetFriendsResponse doInBackground(GetFriendsRequest... params) {
        return client.getFriends(params[0]);
    }

    protected void onPostExecute(final GetFriendsResponse resp) {
        showResult(resp);
    }

    private void showResult(GetFriendsResponse resp) {
        String info = "";
        if (!resp.isError()) {
            friendsList = resp.getFriends();
            adapter = new FriendsListAdapter(context, friendsList, true);
            mList.setAdapter(adapter);
        } else {
            info = resp.getResponse();
            showInfo(info);
        }
    }

    private void showInfo(String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
