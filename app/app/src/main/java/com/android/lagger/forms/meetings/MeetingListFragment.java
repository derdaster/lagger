package com.android.lagger.forms.meetings;

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
import com.android.lagger.logic.adapters.MeetingListAdapter;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.serverConnection.GsonHelper;
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
import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter.Section;

/**
 * Created by Kubaa on 2015-04-01.
 */

import android.app.Activity;


public class MeetingListFragment extends Fragment {

    private String[] HEADER_NAMES;
    private ListView mList;
    private MeetingListAdapter adapter;
    private FloatingActionButton btnAdd;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private final int INDEX_OF_INVITED = 0;
    private int INDEX_OF_UPCOMING = 0;
    private Integer[] mHeaderPositions;
    private List<Section> sections = new ArrayList<Section>();

    private List<Meeting> allMeetings;

    Context mContext;

    public MeetingListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meeting_list, container, false);


        mList = (ListView) rootView.findViewById(R.id.meeting_list);

        btnAdd = (FloatingActionButton) rootView.findViewById(R.id.btnAddMeeting);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container_body, new CreateEditMeetingFragment()).commit();
            }
        });

        loadData();

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        HEADER_NAMES = new String[]{getResources().getString(R.string.invitations),
                getResources().getString(R.string.upcoming)};
    }

    public void loadData() {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                JsonObject userIdJson = new JsonObject();

                userIdJson.addProperty("idUser", State.getLoggedUserId());

                String meetings = HttpRequest.POST(URL.GET_MEETINGS, userIdJson);
                String invitations = HttpRequest.POST(URL.GET_INVITATIONS, userIdJson);

                meetings = meetings.substring(0, meetings.length() - 1);
                invitations = invitations.substring(1, invitations.length());

                StringBuilder sb = new StringBuilder();
                sb.append(meetings);
                sb.append(",");
                sb.append(invitations);

                return sb.toString();
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {

                allMeetings = parseMeetings(result);

                adapter = new MeetingListAdapter(mContext, allMeetings);
                addSections();
                createSimpleSecionedListAdapter(adapter);
                addOnClickListenerDependingToIndex(mList);
            }
        }.execute();

    }

    private List<Meeting> parseMeetings(final String result){
        final List<Meeting> meetings = new ArrayList<Meeting>();
        final List<Meeting> invitations = new ArrayList<Meeting>();
        List<Meeting> meetingAndInvitations = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject responseJson = (JsonObject) parser.parse(result);

        JsonArray meetingsResp = responseJson.get("meetings").getAsJsonArray();
        JsonArray invitationsResp = responseJson.get("meetingInvitations").getAsJsonArray();

        Gson gson = new GsonHelper().getGson();
        for (JsonElement meetingJsonElem : meetingsResp) {
            Meeting meeting = gson.fromJson(meetingJsonElem, Meeting.class);
            meetings.add(meeting);
        }

        for (JsonElement invitationJsonElem : invitationsResp) {
            Meeting invitation = gson.fromJson(invitationJsonElem, Meeting.class);
            invitations.add(invitation);
        }

        INDEX_OF_UPCOMING = invitations.size();

        meetingAndInvitations.addAll(invitations);
        meetingAndInvitations.addAll(meetings);

        return meetingAndInvitations;
    }

    private void addSections(){
        mHeaderPositions = new Integer[]{INDEX_OF_INVITED, INDEX_OF_UPCOMING};
        for (int i = 0; i < mHeaderPositions.length; i++) {
            if (sections.size() < 2) {
                sections.add(new Section(mHeaderPositions[i], HEADER_NAMES[i]));
            }
        }
    }

    private void createSimpleSecionedListAdapter(MeetingListAdapter adapter){
        SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(mContext, adapter,
                R.layout.listview_item_header, R.id.header);
        simpleSectionedGridAdapter.setSections(sections.toArray(new Section[0]));
        mList.setAdapter(simpleSectionedGridAdapter);
    }

    private void addOnClickListenerDependingToIndex(ListView meetingList){
        meetingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= INDEX_OF_UPCOMING) {
                    showInvitationDialog(allMeetings.get(i - 1));
                } else {
                    showMeetingDialog(allMeetings.get(i - 2));
                }
            }
        });
    }

    private void showInvitationDialog(Meeting meeting){

        fragmentTransaction = getFragmentManager().beginTransaction();
        SomeDialog newFragment = new SomeDialog(mContext, "Confirm", "Do you want to accept this meeting invitation?", "meetingInvitation");
        newFragment.show(fragmentTransaction, "dialog");

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        newFragment.setArguments(details);
    }

    private void showMeetingDialog(Meeting meeting){

        fragmentTransaction = getFragmentManager().beginTransaction();
        SomeDialog newFragment = new SomeDialog(mContext, "Confirm", "What do you want to do with this meeting?", "meeting");
        newFragment.show(fragmentTransaction, "dialog");

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        newFragment.setArguments(details);
    }

    private void showMeetingDetails(Meeting meeting, Boolean isReadOnly){
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        ViewMeetingFragment detailsMeetingFragment = new ViewMeetingFragment(mContext, isReadOnly);
        detailsMeetingFragment.setArguments(details);

        fragmentTransaction.replace(R.id.container_body, detailsMeetingFragment).commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}