package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.lagger.R;
import com.android.lagger.logic.adapters.FriendsListAdapter;
import com.android.lagger.model.entities.User;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.android.lagger.settings.State;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class MeetingWhoFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ViewPager parentPager;
    private Button btnAdd;
    private ListView mList;
    private FriendsListAdapter adapter;
    FloatingActionButton leftBtn;
    FloatingActionButton doneBtn;
    FragmentTransaction fragmentTransaction;

    private List<User> allFriendsList;
    private JsonArray friendsListResp;

    public MeetingWhoFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_who, container, false);
        mContext = getActivity().getApplicationContext();

        addButtonsAndListeners();
        getFriendList();
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void addButtonsAndListeners() {

        leftBtn = (FloatingActionButton) parent.findViewById(R.id.btnLeftPager);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container_body, new MeetingWhereFragment()).commit();
            }
        });

        doneBtn = (FloatingActionButton) parent.findViewById(R.id.btnDonePager);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mList = (ListView) parent.findViewById(R.id.guestList);

    }

    private void getFriendList(){
        allFriendsList = new ArrayList<User>();

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                JsonObject userIdJson = new JsonObject();

                userIdJson.addProperty("idUser", State.getLoggedUserId());

                String invitations = HttpRequest.POST(URL.GET_INVITATION_FROM_FRIENDS, userIdJson);
                String friends = HttpRequest.POST(URL.GET_FRIENDS, userIdJson);

                if(invitations != "" && friends != ""){
                    invitations = invitations.substring(0, invitations.length() - 1);
                    friends = friends.substring(1, friends.length());
                }
                StringBuilder sb = new StringBuilder();
                sb.append(invitations);
                sb.append(",");
                sb.append(friends);

                return sb.toString();
            }
            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {

                allFriendsList = parseFriends(result);

                adapter = new FriendsListAdapter(mContext, allFriendsList, true);
                mList.setAdapter(adapter);
            }
        }.execute();


    }

    private List<User> parseFriends(String result){

        List<User> friendsList = new ArrayList<User>();
        if(!result.equals(",")) {
            JsonParser parser = new JsonParser();
            JsonObject responseJson = (JsonObject) parser.parse(result);

            friendsListResp = responseJson.get("friends").getAsJsonArray();

            Gson gson = new Gson();
            for (JsonElement friendJsonElem : friendsListResp) {
                User friend = gson.fromJson(friendJsonElem, User.class);
                friendsList.add(friend);
            }
        }

        return friendsList;
    }
}

