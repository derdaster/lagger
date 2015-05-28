package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.logic.adapters.FriendsListAdapter;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.model.entities.User;
import com.android.lagger.requestObjects.AddMeetingRequest;
import com.android.lagger.requestObjects.EditMeetingRequest;
import com.android.lagger.requestObjects.GetFriendsRequest;
import com.android.lagger.responseObjects.GetFriendsResponse;
import com.android.lagger.services.HttpClient;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.AddMeetingTask;
import com.android.lagger.tasks.EditMeetingTask;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class MeetingWhoFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ListView mList;
    private FriendsListAdapter adapter;
    private FloatingActionButton leftBtn;
    private FloatingActionButton doneBtn;
    private FragmentTransaction fragmentTransaction;

    private List<User> friendsList;
    private List<User> chosenFriends;

    private Meeting meeting;
    private Boolean isEditMode;

    public MeetingWhoFragment(Context context, Meeting meeting, Boolean isEditMode) {
        mContext = context;
        this.meeting = meeting;
        chosenFriends = meeting.getUsersList();
        this.isEditMode = isEditMode;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_who, container, false);
        mContext = getActivity().getApplicationContext();
        mList = (ListView) parent.findViewById(R.id.guestList);

        getFriendList();
        addButtonsAndListeners();

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
                fragmentTransaction.replace(R.id.container_body, new MeetingWhereFragment(mContext, meeting, isEditMode)).commit();
            }
        });

        doneBtn = (FloatingActionButton) parent.findViewById(R.id.btnDonePager);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMeetingData();
                if (isEditMode) {
                    updateCurrentMeeting();
                } else {
                    createNewMeeting();
                }

                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container_body, new MeetingListFragment(mContext)).commit();
            }
        });
    }

    private void updateCurrentMeeting() {
        EditMeetingRequest editMeetingReq = new EditMeetingRequest(State.getLoggedUserId(), meeting);
        EditMeetingTask editMeetingTask = new EditMeetingTask(mContext);
        editMeetingTask.execute(editMeetingReq);
    }

    private void createNewMeeting() {
        AddMeetingRequest addMeetingRequest = new AddMeetingRequest(meeting);

        AddMeetingTask addMeetingTask = new AddMeetingTask(mContext);
        addMeetingTask.execute(addMeetingRequest);
    }


    private void getFriendList() {
        GetFriendsRequest getFriendsRequest = new GetFriendsRequest(State.getLoggedUserId());
        GetFriendsTask getFriendsTask = new GetFriendsTask();
        getFriendsTask.execute(getFriendsRequest);
    }

    private void updateMeetingData() {
        chosenFriends = adapter.getChosenUsers();
        meeting.setUsersList(chosenFriends);
    }

    private class GetFriendsTask extends AsyncTask<GetFriendsRequest, Void, GetFriendsResponse> {
        private HttpClient client;

        public GetFriendsTask() {
            client = new HttpClient(mContext);
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
                adapter = new FriendsListAdapter(mContext, friendsList, true, chosenFriends);
                mList.setAdapter(adapter);
            } else {
                info = resp.getResponse();
                showInfo(info);
            }
        }

        private void showInfo(String info) {
            Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
        }
    }

}

