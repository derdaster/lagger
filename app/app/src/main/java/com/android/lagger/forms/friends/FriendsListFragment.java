package com.android.lagger.forms.friends;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.lagger.R;
import com.android.lagger.controls.basic.SomeDialog;
import com.android.lagger.forms.main.MainActivity;
import com.android.lagger.forms.meetings.ViewMeetingFragment;
import com.android.lagger.logic.adapters.FriendsListAdapter;
import com.android.lagger.logic.adapters.MeetingListAdapter;
import com.android.lagger.model.User;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.serverConnection.ServerConnection;
import com.android.lagger.settings.State;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class FriendsListFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ListView mList;
    private FriendsListAdapter adapter;
    private Button btnAdd;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private final int INDEX_OF_INVITED = 0;
    private String[] HEADER_NAMES;
    private Integer[] mHeaderPositions;
    private ArrayList<SimpleSectionedListAdapter.Section> sections = new ArrayList<SimpleSectionedListAdapter.Section>();

//    private List<User> friendsList;
    private JsonArray invitationsFromFriendsResp;
    private JsonArray friendsListResp;

    public FriendsListFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_friends_list, container, false);

        mList = (ListView) parent.findViewById(R.id.friends_list);
//        friendsList = new ArrayList<User>();

        fragmentManager = getFragmentManager();
        btnAdd = (Button) parent.findViewById(R.id.btnAddFriend);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container_body, new FriendsAddFragment()).commit();
            }
        });

        getFriendList();

        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        HEADER_NAMES = new String[]{mContext.getResources().getString(R.string.invitationsFromFriends),
                mContext.getResources().getString(R.string.allFriends)};
    }

    private void getFriendList(){
        final List<User> invitationsFromFriends = new ArrayList<User>();
        final List<User> friendsList = new ArrayList<User>();

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                JsonObject userIdJson = new JsonObject();

                //FIXME set userId only for tests
                int userId = 1;
                if( State.loggedUser != null) {
                    userId = State.loggedUser.getId();
                }
                userIdJson.addProperty("idUser", userId);

                String invitations = ServerConnection.POST(ServerConnection.GET_INVITATION_FROM_FRIENDS_URL, userIdJson);
                String friends = ServerConnection.POST(ServerConnection.GET_FRIENDS_URL, userIdJson);

                invitations = invitations.substring(0, invitations.length() - 1);
                friends = friends.substring(1, friends.length());

                StringBuilder sb = new StringBuilder();
                sb.append(invitations);
                sb.append(",");
                sb.append(friends);

                return sb.toString();
            }
            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {

                JsonParser parser = new JsonParser();
                JsonObject responseJson = (JsonObject)parser.parse(result);

                friendsListResp = responseJson.get("friends").getAsJsonArray();
                invitationsFromFriendsResp = responseJson.get("friendInvitations").getAsJsonArray();

                Gson gson = new Gson();
                for(JsonElement friendJsonElem: friendsListResp) {
                    User friend = gson.fromJson(friendJsonElem, User.class);
                    friendsList.add(friend);
                }

                for(JsonElement invitationJsonElem: invitationsFromFriendsResp) {
                    User friend = gson.fromJson(invitationJsonElem, User.class);
                    invitationsFromFriends.add(friend);
                }

                List<User> allFriends = new ArrayList<User>(invitationsFromFriends);
                allFriends.addAll(friendsList);

                adapter = new FriendsListAdapter(mContext, allFriends);
                final int INDEX_OF_INVITATION_END = invitationsFromFriends.size();
                mHeaderPositions = new Integer[]{INDEX_OF_INVITED, INDEX_OF_INVITATION_END};

                for (int i = 0; i < mHeaderPositions.length; i++) {
                    if(sections.size() < 2) {
                        sections.add(new SimpleSectionedListAdapter.Section(mHeaderPositions[i], HEADER_NAMES[i]));
                    }
                }
                SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(mContext, adapter,
                        R.layout.listview_item_header, R.id.header);
                simpleSectionedGridAdapter.setSections(sections.toArray(new SimpleSectionedListAdapter.Section[0]));
                mList.setAdapter(simpleSectionedGridAdapter);
                mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i <= INDEX_OF_INVITATION_END)
                        {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            SomeDialog newFragment = new SomeDialog (mContext, "Confirm", "Do you want to accept this friend invitation?", false);
                            newFragment.show(fragmentTransaction, "dialog");

                        }
                    }
                });
            }
        }.execute();


    }
}