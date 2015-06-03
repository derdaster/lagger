package com.android.lagger.forms.meetings;

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
import com.android.lagger.controls.basic.DeleteDialog;
import com.android.lagger.controls.basic.InvitationDialog;
import com.android.lagger.controls.basic.RefuseDialog;
import com.android.lagger.logic.adapters.MeetingListAdapter;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.requestObjects.GetAllMeetingsRequest;
import com.android.lagger.requestObjects.GetMeetingInvitationsRequest;
import com.android.lagger.requestObjects.GetMeetingsRequest;
import com.android.lagger.responseObjects.GetAllMeetingsResponse;
import com.android.lagger.services.HttpClient;
import com.android.lagger.settings.State;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;
import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter.Section;

/**
 * Created by Kubaa on 2015-04-01.
 */


public class MeetingListFragment extends Fragment {

    private String[] HEADER_NAMES;
    private ListView mList;
    private MeetingListAdapter adapter;
    private FloatingActionButton btnAdd;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private final int INDEX_OF_INVITED = 0;
    private int PARTITION_INDEX = 0;
    private Integer[] mHeaderPositions;
    private List<Section> sections = new ArrayList<Section>();

    private List<Meeting> allMeetings;

    Context mContext;

    public MeetingListFragment(Context context) {
        mContext = context;
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
                fragmentTransaction.replace(R.id.container_body, new MeetingWhenFragment(mContext)).commit();
            }
        });


        loadAllMeetingsFromServer();

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        HEADER_NAMES = new String[]{getResources().getString(R.string.invitations),
                getResources().getString(R.string.upcoming)};

    }

    public void loadAllMeetingsFromServer() {
        GetMeetingsRequest getMeetingsRequest = new GetMeetingsRequest(State.getLoggedUserId());
        GetMeetingInvitationsRequest getInvitationsRequest = new GetMeetingInvitationsRequest(State.getLoggedUserId());

        GetAllMeetingsRequest getAllMeetingsRequest = new GetAllMeetingsRequest(getMeetingsRequest,
                getInvitationsRequest);

        GetAllMeetingsTask getAllMeetingsTask = new GetAllMeetingsTask();
        getAllMeetingsTask.execute(getAllMeetingsRequest);
    }

    private void addSections() {
        mHeaderPositions = new Integer[]{INDEX_OF_INVITED, PARTITION_INDEX};
        for (int i = 0; i < mHeaderPositions.length; i++) {
            if (sections.size() < 2) {
                sections.add(new Section(mHeaderPositions[i], HEADER_NAMES[i]));
            }
        }
    }

    private void createSimpleSectionedListAdapter(MeetingListAdapter adapter) {
        SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(mContext, adapter,
                R.layout.listview_item_header, R.id.header);
        simpleSectionedGridAdapter.setSections(sections.toArray(new Section[0]));
        mList.setAdapter(simpleSectionedGridAdapter);
    }

    private void addOnClickListenerDependingToIndex(ListView meetingList) {
        meetingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= PARTITION_INDEX) {
                    showInvitationDialog(allMeetings.get(i - 1));
                } else {
                    showMeetingDetails(allMeetings.get(i - 2), true);
                }
            }
        });
        meetingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meeting chosenMeeting = null;
                if (i <= PARTITION_INDEX) {
                    chosenMeeting = allMeetings.get(i - 1);
                } else {
                    chosenMeeting = allMeetings.get(i - 2);
                }
                if (chosenMeeting.getOrganizer().getId() == State.getLoggedUserId()) {
                    showDeleteMeetingDialog(chosenMeeting);
                } else {
                    showRefuseMeetingDialog(chosenMeeting);
                }

                return false;
            }
        });
    }

    private void showInvitationDialog(Meeting meeting) {

        fragmentTransaction = getFragmentManager().beginTransaction();
        InvitationDialog newFragment = new InvitationDialog(mContext, mContext.getResources().getString(R.string.dialog_confirm), mContext.getResources().getString(R.string.dialog_invitation_meeting), InvitationDialog.MEETING_INVITATION_TYPE);
        newFragment.show(fragmentTransaction, "dialog");

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        newFragment.setArguments(details);
    }

    private void showDeleteMeetingDialog(Meeting meeting) {

        fragmentTransaction = getFragmentManager().beginTransaction();
        DeleteDialog newFragment = new DeleteDialog(mContext, mContext.getResources().getString(R.string.dialog_confirm), mContext.getResources().getString(R.string.dialog_meeting_delete), DeleteDialog.MEETING_DELETE_TYPE);
        newFragment.show(fragmentTransaction, "dialog");

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        newFragment.setArguments(details);
    }

    private void showRefuseMeetingDialog(Meeting meeting) {

        fragmentTransaction = getFragmentManager().beginTransaction();
        RefuseDialog newFragment = new RefuseDialog(mContext, mContext.getResources().getString(R.string.dialog_confirm), mContext.getResources().getString(R.string.dialog_meeting_refuse));
        newFragment.show(fragmentTransaction, "dialog");

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        newFragment.setArguments(details);
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

    private class GetAllMeetingsTask extends AsyncTask<GetAllMeetingsRequest, Void, GetAllMeetingsResponse> {
        private HttpClient client;

        public GetAllMeetingsTask() {
            client = new HttpClient(mContext);
        }

        @Override
        protected GetAllMeetingsResponse doInBackground(GetAllMeetingsRequest... params) {
            return client.getAllMeetings(params[0]);
        }

        protected void onPostExecute(final GetAllMeetingsResponse resp) {
            if (!resp.isError()) {
                setAllMeetingsAndPartitionIndex(resp);

                adapter = new MeetingListAdapter(mContext, fragmentManager, allMeetings, -1, -1);
                addSections();
                createSimpleSectionedListAdapter(adapter);
                addOnClickListenerDependingToIndex(mList);

            } else {
                String info = resp.getResponse();
                showInfo(info);
            }

        }

        private void setAllMeetingsAndPartitionIndex(final GetAllMeetingsResponse resp) {
            List<Meeting> meetings = resp.getGetMeetingsResponse().getMeetings();
            List<Meeting> invitations = resp.getGetMeetingInvitationsResponse().getMeetingInvitations();

            setPartitionIndex(invitations.size());

            allMeetings = new ArrayList<Meeting>();
            allMeetings.addAll(invitations);
            allMeetings.addAll(meetings);

        }

        private void setPartitionIndex(int index) {
            PARTITION_INDEX = index;
        }

        private void showInfo(String info) {
            Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
        }
    }
}