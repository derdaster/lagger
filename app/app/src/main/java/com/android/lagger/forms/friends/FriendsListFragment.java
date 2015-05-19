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
import android.widget.ListView;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.controls.basic.SomeDialog;
import com.android.lagger.logic.adapters.FriendsListAdapter;
import com.android.lagger.model.entities.User;
import com.android.lagger.requestObjects.GetFriendInvitationsRequest;
import com.android.lagger.requestObjects.GetFriendsAndInvitationsRequest;
import com.android.lagger.requestObjects.GetFriendsRequest;
import com.android.lagger.responseObjects.GetFriendsAndInvitationsResponse;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.android.lagger.services.HttpClient;
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
 * Created by Kubaa on 2015-04-03.
 */
public class FriendsListFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ListView mList;
    private FriendsListAdapter adapter;
    private FloatingActionButton btnAdd;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private final int INDEX_OF_INVITED = 0;
    private int INDEX_OF_INVITATION_END;
    private String[] HEADER_NAMES;
    private Integer[] mHeaderPositions;
    private ArrayList<SimpleSectionedListAdapter.Section> sections = new ArrayList<SimpleSectionedListAdapter.Section>();

    private List<User> allFriendsList;

    public FriendsListFragment(Context context) {
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_friends_list, container, false);

        mList = (ListView) parent.findViewById(R.id.friends_list);

        fragmentManager = getFragmentManager();
        btnAdd = (FloatingActionButton) parent.findViewById(R.id.btnAddFriend);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container_body, new FriendsAddFragment(mContext)).commit();
            }
        });

        loadAllFriendsFromServer();

        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        HEADER_NAMES = new String[]{mContext.getResources().getString(R.string.invitationsFromFriends),
                mContext.getResources().getString(R.string.allFriends)};
    }

    public void loadAllFriendsFromServer() {
        GetFriendInvitationsRequest invitationsRequest = new GetFriendInvitationsRequest(State.getLoggedUserId());
        GetFriendsRequest friendsRequest = new GetFriendsRequest(State.getLoggedUserId());

        GetFriendsAndInvitationsRequest getAllFriendsRequest = new GetFriendsAndInvitationsRequest(invitationsRequest, friendsRequest);

        GetAllFriendsTask getAllFriendsTask = new GetAllFriendsTask();
        getAllFriendsTask.execute(getAllFriendsRequest);
    }

    private void showFriendInvitationDialog(User friend){

        fragmentTransaction = fragmentManager.beginTransaction();
        SomeDialog friendInvitationDialog = new SomeDialog (mContext, "Confirm", "Do you want to accept this friend invitation?", SomeDialog.FRIEND_INVITATION_TYPE);
        friendInvitationDialog.show(fragmentTransaction, "dialog");

        Bundle details = new Bundle();
        details.putParcelable("friend", friend);

        friendInvitationDialog.setArguments(details);
    }
    private void showFriendDeleteDialog(User friend){
        SomeDialog friendDeleteDialog = new SomeDialog (mContext, "Confirm", "Do you want to delete this friend?", SomeDialog.FRIEND_TYPE);

        Bundle details = new Bundle();
        details.putParcelable("friend", friend);
        friendDeleteDialog.setArguments(details);

        fragmentTransaction = fragmentManager.beginTransaction();
        friendDeleteDialog.show(fragmentTransaction, "dialog");
    }


    private class GetAllFriendsTask extends AsyncTask<GetFriendsAndInvitationsRequest, Void, GetFriendsAndInvitationsResponse> {
        private HttpClient client;

        public GetAllFriendsTask() {
            client = new HttpClient(mContext);
        }

        @Override
        protected GetFriendsAndInvitationsResponse doInBackground(GetFriendsAndInvitationsRequest... params) {
            return client.getFriendsAndInvitationsFromFriends(params[0]);
        }

        protected void onPostExecute(final GetFriendsAndInvitationsResponse resp) {
            if (!resp.isError()) {
                setAllFriendsAndPartitionIndex(resp);

                adapter = new FriendsListAdapter(mContext, allFriendsList, false);

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

                mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i <= INDEX_OF_INVITATION_END){
                            showFriendInvitationDialog(allFriendsList.get(i - 1));
                        }
                        else {
                            showFriendDeleteDialog(allFriendsList.get(i - 2));
                        }
                        return false;
                    }
                });

            } else {
                String info = resp.getResponse();
                showInfo(info);
            }

        }

        private void setAllFriendsAndPartitionIndex(final GetFriendsAndInvitationsResponse resp) {
            List<User> friends = resp.getGetFriendsResponse().getFriends();
            List<User> invitations = resp.getGetFriendInvitationsResponse().getFriendInvitations();

            setPartitionIndex(invitations.size());

            allFriendsList = new ArrayList<User>();
            allFriendsList.addAll(invitations);
            allFriendsList.addAll(friends);

        }

        private void setPartitionIndex(int index) {
            INDEX_OF_INVITATION_END = index;
        }

        private void showInfo(String info) {
            Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
        }
    }
}