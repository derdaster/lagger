package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.internal.view.menu.MenuView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.lagger.R;
import com.android.lagger.logic.adapters.FriendsListAdapter;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.model.entities.User;
import com.android.lagger.requestObjects.AddMeetingRequest;
import com.android.lagger.requestObjects.GetFriendsRequest;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.GetFriendsTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class MeetingWhoFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ViewPager parentPager;
    private Button btnAdd;
    private ListView mList;
    private FloatingActionButton leftBtn;
    private FloatingActionButton doneBtn;
    private FragmentTransaction fragmentTransaction;
    private GetFriendsTask getFriendsTask;

    private List<User> allFriendsList;
    private List<User> choosenFriends;

    private Meeting meeting;

    public MeetingWhoFragment(Context context, Meeting meeting) {
        mContext = context;
        this.meeting = meeting;
        choosenFriends = meeting.getUserList();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_who, container, false);
        mContext = getActivity().getApplicationContext();
        mList = (ListView) parent.findViewById(R.id.guestList);


        addButtonsAndListeners();
        getFriendList();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                allFriendsList = getFriendsTask.getFriendsList();
                ListView lv = (ListView) parent;
                User friend = allFriendsList.get(position);

                if(lv.isItemChecked(position)){
                    choosenFriends.add(friend);
                }
                else{
                    if(choosenFriends.contains(friend)){
                        choosenFriends.remove(friend);
                    }
                }
            }
        });
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
                updateMeetingData();

                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container_body, new MeetingWhereFragment(mContext, meeting)).commit();
            }
        });

        doneBtn = (FloatingActionButton) parent.findViewById(R.id.btnDonePager);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMeetingData();
                AddMeetingRequest addMeetingRequest = new AddMeetingRequest(meeting);
                //todo asynctask add meeting
            }
        });
    }

    private void getFriendList(){
        GetFriendsRequest getFriendsRequest = new GetFriendsRequest(State.getLoggedUserId());
        getFriendsTask = new GetFriendsTask(mContext, mList);
        getFriendsTask.execute(getFriendsRequest);
    }

    private void updateMeetingData(){
        meeting.setUserList(choosenFriends);
    }
}

