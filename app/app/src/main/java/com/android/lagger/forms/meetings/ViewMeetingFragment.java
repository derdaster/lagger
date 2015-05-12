package com.android.lagger.forms.meetings;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.model.entities.User;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.services.HttpClient;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.AcceptMeetingTask;

/**
 * Created by Kubaa on 2015-04-08.
 */
public class ViewMeetingFragment extends Fragment {
    private View parent;
    private Context mContext;
    private Button btnAccept;
    private Button btnEdit;
    private Button btnRefuse;

    private TextView labelWhen;
    private TextView labelOrganizer;
    private TextView labelMeetingName;
    private TextView labelWhere;

    private Boolean isReadOnly;
    private Boolean isOrganizer;
    private Meeting meeting;
    private User user;

    FragmentTransaction fragmentTransaction;
    public ViewMeetingFragment(){

    }
    public ViewMeetingFragment(Context mContext, Boolean isReadOnly) {
        this.mContext = mContext;
        this.isReadOnly = isReadOnly;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_view_meeting, container, false);
        btnAccept = (Button) parent.findViewById(R.id.btnAcceptMeeting);
        btnRefuse = (Button) parent.findViewById(R.id.btnRefuseMeeting);
        btnEdit = (Button) parent.findViewById(R.id.btnEditMeeting);

        if(isReadOnly){
            btnAccept.setVisibility(View.INVISIBLE);
            btnRefuse.setVisibility(View.INVISIBLE);
        }
        else {
            addButtonsAndListeners();
        }
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        labelMeetingName = (TextView) getView().findViewById(R.id.tvMeetingName);
        labelWhen = (TextView) getView().findViewById(R.id.labelWhen);
        labelOrganizer = (TextView) getView().findViewById(R.id.labelOrganizer);
        labelWhere = (TextView) getView().findViewById(R.id.labelWhere);

        insertMeetingDetails();
    }

    private void insertMeetingDetails(){
        Bundle extras = getArguments();
        meeting = extras.getParcelable("meeting");
        user = meeting.getOrganizer();

        isOrganizer = State.getLoggedUserId() == meeting.getOrganizer().getId();
        if(!isOrganizer){
            btnEdit.setVisibility(View.INVISIBLE);
        }
        labelMeetingName.setText(meeting.getName());
        labelWhen.setText(meeting.getStartTime().toString());
        labelWhere.setText(meeting.getLocationName());
        labelOrganizer.setText(user.getLogin());

        //TODO insert map
    }

    public void addButtonsAndListeners()
    {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptMeetingTask.acceptMeeting(meeting.getId(), true, mContext);

                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_body, new MeetingListFragment()).commit();

            }
        });

        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptMeetingTask.acceptMeeting(meeting.getId(), false, mContext);

                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_body, new MeetingListFragment()).commit();
            }
        });

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
