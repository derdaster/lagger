package com.android.lagger.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.model.AdapterUser;
import com.android.lagger.model.entities.User;
import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.requestObjects.FindFriendRequest;
import com.android.lagger.responseObjects.FindFriendResponse;
import com.android.lagger.responseObjects.ResponseObject;
import com.android.lagger.services.HttpClient;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-11.
 */
public class FindFriendsTask extends AsyncTask<FindFriendRequest, Void, FindFriendResponse> {
    private Context context;
    private HttpClient client;
    private ArrayAdapter<AdapterUser> arrayAdapter;

    public FindFriendsTask(Context context, ArrayAdapter<AdapterUser> arrayAdapter) {
        this.context = context;
        this.arrayAdapter = arrayAdapter;
        client = new HttpClient();
    }

    protected FindFriendResponse doInBackground(FindFriendRequest... findFriendRequests) {
        return client.findFriends(findFriendRequests[0]);
    }

    protected void onPostExecute(final FindFriendResponse resp) {
        //TODO TESTING
        final AdapterUser[] emailsList = getEmailList(resp);
        setAutoAdapter(emailsList);
    }

    private AdapterUser[] getEmailList(final FindFriendResponse resp){
        AdapterUser[] emailList = null;
        if(resp != null) {
            List<User> users = resp.getUsers();
            emailList = new AdapterUser[users.size()];
            if (users != null) {
                int i = 0;
                for (User user : users) {
                    emailList[i] = user.convertToAdapterUser();
                    i++;
                }
            }
        }
        return emailList;
    }
   private void setAutoAdapter(final AdapterUser[] EMAIL_LIST){
       arrayAdapter.clear();
       if (EMAIL_LIST != null)
           for (int i = 0; i < EMAIL_LIST.length; i++) {
               arrayAdapter.add(EMAIL_LIST[i]);
       }
       arrayAdapter.notifyDataSetChanged();

   }
    private void showInfo(String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }

}

