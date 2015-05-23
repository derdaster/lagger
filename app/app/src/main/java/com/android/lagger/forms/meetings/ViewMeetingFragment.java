package com.android.lagger.forms.meetings;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.model.entities.User;
import com.android.lagger.settings.Parser;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.AcceptMeetingTask;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Kubaa on 2015-04-08.
 */
public class ViewMeetingFragment extends Fragment {
    private View parent;
    private Context mContext;
    private FloatingActionButton btnAccept;
    private FloatingActionButton btnEdit;
    private FloatingActionButton btnRefuse;

    private TextView labelWhen;
    private TextView labelOrganizer;
    private TextView labelMeetingName;
    private TextView labelWhere;

    private Boolean isReadOnly;
    private Boolean isOrganizer;
    private Meeting meeting;
    private User user;

    FragmentTransaction fragmentTransaction;

    public ViewMeetingFragment() {

    }

    public ViewMeetingFragment(Context mContext, Boolean isReadOnly) {
        this.mContext = mContext;
        this.isReadOnly = isReadOnly;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_view_meeting, container, false);
        btnAccept = (FloatingActionButton) parent.findViewById(R.id.btnAcceptMeeting);
        btnRefuse = (FloatingActionButton) parent.findViewById(R.id.btnRefuseMeeting);
        btnEdit = (FloatingActionButton) parent.findViewById(R.id.btnEditMeeting);

        if (isReadOnly) {
            btnAccept.setVisibility(View.INVISIBLE);
            btnRefuse.setVisibility(View.INVISIBLE);
        } else {
            addListenersForBtns();
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

        meeting = insertMeetingDetails();
        setBtnEditVisibilityForUser(meeting.getOrganizer());

        //TODO insert map
    }

    private Meeting insertMeetingDetails() {
        Bundle extras = getArguments();
        Meeting meeting = extras.getParcelable("meeting");
        user = meeting.getOrganizer();

        labelMeetingName.setText(meeting.getName());
        labelWhen.setText(Parser.parseDate(meeting.getStartTime()));
        labelWhere.setText(meeting.getLocationName());
        labelOrganizer.setText(user.getLogin());

        return meeting;
    }

    private void setBtnEditVisibilityForUser(final User user){
        isOrganizer = State.getLoggedUserId() == user.getId();
        if (!isOrganizer) {
            btnEdit.setVisibility(View.INVISIBLE);
        }
    }

    public void addListenersForBtns() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptMeetingTask.acceptMeeting(meeting.getId(), true, mContext);
                replaceWithFragment(new MeetingListFragment(mContext));
            }
        });

        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptMeetingTask.acceptMeeting(meeting.getId(), false, mContext);
                replaceWithFragment(new MeetingListFragment(mContext));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
                replaceWithFragment(new MeetingWhenFragment(mContext, meeting));
            }
        });
    }

    private void replaceWithFragment(Fragment fragment){
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_body, fragment).commit();
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
