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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.controls.basic.SomeDialog;
import com.android.lagger.forms.login.LoginFragment;
import com.android.lagger.forms.main.MainActivity;
import com.android.lagger.logic.adapters.MeetingListAdapter;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.serverConnection.GsonHelper;
import com.android.lagger.serverConnection.ServerConnection;
import com.android.lagger.settings.State;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;
import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter.Section;

/**
 * Created by Kubaa on 2015-04-01.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MeetingListFragment extends Fragment {

    private String[] HEADER_NAMES;
    private ListView mList;
    private MeetingListAdapter adapter;
    private Button btnAdd;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private final int INDEX_OF_INVITED = 0;
    private Integer[] mHeaderPositions;
    private List<Section> sections = new ArrayList<Section>();

    private JsonArray meetingsResp;
    private JsonArray invitationsResp;

    Context mContext;

    public MeetingListFragment() {
        // Required empty public constructor
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

//        meetingsList = new ArrayList<Meeting>();
//        invitationList = new ArrayList<Meeting>();


        btnAdd = (Button) rootView.findViewById(R.id.btnAddMeeting);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container_body, new CreateEditMeetingFragment()).commit();
            }
        });

        getMeetingsList();

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        HEADER_NAMES = new String[]{getResources().getString(R.string.invitations),
                getResources().getString(R.string.upcoming)};
    }

    public List<Meeting> getMeetingsList() {
        final List<Meeting> meetings = new ArrayList<Meeting>();
        final List<Meeting> invitations = new ArrayList<Meeting>();

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                JsonObject userIdJson = new JsonObject();


                //FIXME set userId only for tests
                int userId = 1;
                if (State.loggedUser != null) {
                    userId = State.loggedUser.getId();
                }
                userIdJson.addProperty("idUser", userId);

                String meetings = ServerConnection.POST(ServerConnection.GET_MEETINGS_URL, userIdJson);
                String invitations = ServerConnection.POST(ServerConnection.GET_INVITATIONS_URL, userIdJson);

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

                JsonParser parser = new JsonParser();
                JsonObject responseJson = (JsonObject) parser.parse(result);

                meetingsResp = responseJson.get("meetings").getAsJsonArray();
                invitationsResp = responseJson.get("meetingInvitations").getAsJsonArray();

                Gson gson = new GsonHelper().getGson();
                for (JsonElement meetingJsonElem : meetingsResp) {
                    Meeting meeting = gson.fromJson(meetingJsonElem, Meeting.class);
                    meetings.add(meeting);
                }

                for (JsonElement invitationJsonElem : invitationsResp) {
                    Meeting invitation = gson.fromJson(invitationJsonElem, Meeting.class);
                    invitations.add(invitation);
                }

                List<Meeting> allMeetings = new ArrayList<Meeting>(invitations);
                allMeetings.addAll(meetings);

                adapter = new MeetingListAdapter(mContext, allMeetings);

                final int INDEX_OF_UPCOMING = invitations.size();
                mHeaderPositions = new Integer[]{INDEX_OF_INVITED, INDEX_OF_UPCOMING};
                for (int i = 0; i < mHeaderPositions.length; i++) {
                    if (sections.size() < 2) {
                        sections.add(new Section(mHeaderPositions[i], HEADER_NAMES[i]));
                    }
                }
                SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(mContext, adapter,
                        R.layout.listview_item_header, R.id.header);
                simpleSectionedGridAdapter.setSections(sections.toArray(new Section[0]));
                mList.setAdapter(simpleSectionedGridAdapter);


                mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i <= INDEX_OF_UPCOMING) {
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            SomeDialog newFragment = new SomeDialog(mContext, "Confirm", "Do you want to accept this meeting invitation?", true);
                            newFragment.show(fragmentTransaction, "dialog");

                        } else {
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.replace(R.id.container_body, new ViewMeetingFragment()).commit();
                        }
                    }
                });
            }
        }.execute();
        return meetings;
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