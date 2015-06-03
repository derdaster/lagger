package com.android.lagger.forms.home;

import android.app.Activity;
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
import com.android.lagger.controls.basic.InvitationDialog;
import com.android.lagger.forms.meetings.ViewMeetingFragment;
import com.android.lagger.logic.adapters.MeetingListAdapter;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.requestObjects.GetAllMeetingsRequest;
import com.android.lagger.requestObjects.GetMeetingsRequest;
import com.android.lagger.responseObjects.GetAllMeetingsResponse;
import com.android.lagger.responseObjects.GetMeetingsResponse;
import com.android.lagger.serverConnection.GsonHelper;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.android.lagger.services.HttpClient;
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
    private FragmentTransaction fragmentTransaction;
    private List<Meeting> allMeetings;
    private List<Meeting> current;
    private List<Meeting> nearest;

    public HomeFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity().getApplicationContext();
        mList = (ListView) rootView.findViewById(R.id.meeting_current_list);
        loadDataFromServer();
        adapter = null;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        HEADER_NAMES = new String[]{getResources().getString(R.string.current_meetings),
                getResources().getString(R.string.nearest_meetings)};
    }

    private void loadDataFromServer(){
        GetMeetingsRequest meetingsRequest = new GetMeetingsRequest(State.getLoggedUserId());
        GetMeetingsTask getMeetingsTask = new GetMeetingsTask();
        getMeetingsTask.execute(meetingsRequest);
    }

    private void addSections() {
        mHeaderPositions = new Integer[]{INDEX_OF_CURRENT, INDEX_OF_NEAREST};
        for (int i = 0; i < mHeaderPositions.length; i++) {
            if (sections.size() < 2) {
                sections.add(new SimpleSectionedListAdapter.Section(mHeaderPositions[i], HEADER_NAMES[i]));
            }
        }
    }

    private void createSimpleSectionedListAdapter(MeetingListAdapter adapter) {
        SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(mContext, adapter,
                R.layout.listview_item_header, R.id.header);
        simpleSectionedGridAdapter.setSections(sections.toArray(new SimpleSectionedListAdapter.Section[0]));
        mList.setAdapter(simpleSectionedGridAdapter);
    }

    private void addOnClickListenerDependingToIndex(ListView meetingList) {
        meetingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= INDEX_OF_NEAREST) {
                    showMeetingDetails(allMeetings.get(i - 1), true);
                } else {
                    showMeetingDetails(allMeetings.get(i - 2), true);
                }
            }
        });
    }

    private void showMeetingDetails(final Meeting meeting, Boolean isReadonly) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        ViewMeetingFragment detailsMeetingFragment = new ViewMeetingFragment(mContext, isReadonly);
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

    private class GetMeetingsTask extends AsyncTask<GetMeetingsRequest, Void, GetMeetingsResponse> {
        private HttpClient client;

        public GetMeetingsTask() {
            client = new HttpClient(mContext);
        }

        @Override
        protected GetMeetingsResponse doInBackground(GetMeetingsRequest... params) {
            return client.getMeetings(params[0]);
        }

        protected void onPostExecute(final GetMeetingsResponse resp) {
            if (!resp.isError()) {
                setAllMeetingsAndPartitionIndex(resp);

                adapter = new MeetingListAdapter(mContext, getFragmentManager(), allMeetings, 0, current.size() - 1);
                addSections();
                createSimpleSectionedListAdapter(adapter);
                addOnClickListenerDependingToIndex(mList);

            } else {
                String info = resp.getResponse();
                showInfo(info);
            }

        }

        private void setAllMeetingsAndPartitionIndex(final GetMeetingsResponse resp) {
            List<Meeting> meetings = resp.getMeetings();
            List<Meeting> currentAndNearest = new ArrayList<>();
            current = new ArrayList<Meeting>();
            nearest = new ArrayList<>();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
                Calendar c = Calendar.getInstance();
                String currentDate = sdf.format(new Date());
                Date currentDateAndTime = sdf.parse(currentDate);

                long currentDateInMilliseconds = c.getTimeInMillis();
                //2 dni do przodu
                long nearestDateInMilliseconds = currentDateInMilliseconds + 172800000;
                Date nearestDateAndTime = new Date(nearestDateInMilliseconds);

                for (Meeting m : meetings) {
                    if (m.getStartTime().before(currentDateAndTime) && currentDateAndTime.before(m.getEndTime()))
                        current.add(m);
                    if (currentDateAndTime.before(m.getStartTime()) && m.getStartTime().before(nearestDateAndTime))
                        nearest.add(m);
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

           setPartitionIndex(current.size());

            allMeetings = new ArrayList<Meeting>();
            currentAndNearest.addAll(current);
            currentAndNearest.addAll(nearest);
            allMeetings = currentAndNearest;
        }

        private void setPartitionIndex(int index) {
            INDEX_OF_NEAREST = index;
        }

        private void showInfo(String info) {
            Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
        }
    }
}