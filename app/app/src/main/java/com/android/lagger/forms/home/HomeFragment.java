package com.android.lagger.forms.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;

/**
 * Created by Kubaa on 2015-05-13.
 */
public class HomeFragment extends Fragment {

    private Context mContext;
    private ListView mList;
    private String[] HEADER_NAMES;
    private MeetingListAdapter adapter;
    private final int INDEX_OF_CURRENT = 0;
    private int INDEX_OF_NEAREST = 0;
    private Integer[] mHeaderPositions;
    private List<SimpleSectionedListAdapter.Section> sections = new ArrayList<SimpleSectionedListAdapter.Section>();
    FragmentTransaction fragmentTransaction;
    private List<Meeting> allMeetings;


    public HomeFragment(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mList = (ListView) rootView.findViewById(R.id.meeting_current_list);
        loadData();

        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        HEADER_NAMES = new String[]{getResources().getString(R.string.current_meetings),
                getResources().getString(R.string.nearest_meetings)};
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
        final List<Meeting> current = new ArrayList<Meeting>();
        final List<Meeting> nearest = new ArrayList<Meeting>();
        List<Meeting> currentAndNearest = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject responseJson = (JsonObject) parser.parse(result);

        JsonArray meetingsResp = responseJson.get("meetings").getAsJsonArray();

        Gson gson = new GsonHelper().getGson();
        for (JsonElement meetingJsonElem : meetingsResp) {
            Meeting meeting = gson.fromJson(meetingJsonElem, Meeting.class);
            meetings.add(meeting);
        }
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
            Calendar c = Calendar.getInstance();
            String currentDate = sdf.format(new Date());
            Date currentDateAndTime = sdf.parse(currentDate);


            long currentDateInMilliseconds = c.getTimeInMillis();
            //2 dni do przodu
            long nearestDateInMilliseconds = currentDateInMilliseconds + 172800000;
            Date nearestDateAndTime = new Date(nearestDateInMilliseconds);

            for(Meeting m : meetings){
                if(m.getStartTime().before(currentDateAndTime) && currentDateAndTime.before(m.getEndTime()))
                    current.add(m);
                if(currentDateAndTime.before(m.getStartTime()) && m.getStartTime().before(nearestDateAndTime))
                    nearest.add(m);
            }
        } catch (ParseException ex)
        {
            ex.printStackTrace();
        }

        INDEX_OF_NEAREST = current.size();

        currentAndNearest.addAll(current);
        currentAndNearest.addAll(nearest);

        return currentAndNearest;
    }

    private void addSections(){
        mHeaderPositions = new Integer[]{INDEX_OF_CURRENT, INDEX_OF_NEAREST};
        for (int i = 0; i < mHeaderPositions.length; i++) {
            if (sections.size() < 2) {
                sections.add(new SimpleSectionedListAdapter.Section(mHeaderPositions[i], HEADER_NAMES[i]));
            }
        }
    }

    private void createSimpleSecionedListAdapter(MeetingListAdapter adapter){
        SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(mContext, adapter,
                R.layout.listview_item_header, R.id.header);
        simpleSectionedGridAdapter.setSections(sections.toArray(new SimpleSectionedListAdapter.Section[0]));
        mList.setAdapter(simpleSectionedGridAdapter);
    }

    private void addOnClickListenerDependingToIndex(ListView meetingList){
        meetingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= INDEX_OF_NEAREST) {
                    //showInvitationDialog(allMeetings.get(i - 1));
                } else {
                    showMeetingDialog(allMeetings.get(i - 2));
                }
            }
        });
    }

    private void showMeetingDialog(Meeting meeting){

        fragmentTransaction = getFragmentManager().beginTransaction();
        SomeDialog newFragment = new SomeDialog(mContext, "Confirm", "What do you want to do with this meeting?", SomeDialog.MEETING_DELETE_TYPE);
        newFragment.show(fragmentTransaction, "dialog");

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        newFragment.setArguments(details);
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