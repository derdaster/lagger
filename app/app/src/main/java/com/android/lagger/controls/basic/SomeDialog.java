package com.android.lagger.controls.basic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.forms.meetings.MeetingListFragment;
import com.android.lagger.forms.meetings.ViewMeetingFragment;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.requestObjects.AcceptFriendRequest;
import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.requestObjects.LoginRequest;
import com.android.lagger.responseObjects.LoginResponse;
import com.android.lagger.responseObjects.ResponseObject;
import com.android.lagger.services.HttpClient;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.AcceptMeetingTask;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class SomeDialog extends DialogFragment {

    private String title;
    private String message;
    private Boolean isMeetingInvitation;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Context mContext;
//    private HttpClient client;

    public SomeDialog() {
    }

    public SomeDialog(Context inContext, String inTitle, String inMessage, Boolean inIsMeetingInvitation) {
        title = inTitle;
        message = inMessage;
        mContext = inContext;
        isMeetingInvitation = inIsMeetingInvitation;
//        client = new HttpClient();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        if (isMeetingInvitation) {
            return createMeetingDialog();
        } else {
            return createFriendDialog();
        }
    }

    private AlertDialog createMeetingDialog(){
        Bundle extras = getArguments();
        final Meeting meeting = extras.getParcelable("meeting");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AcceptMeetingTask.acceptMeeting(meeting.getId(), true, mContext);

                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment()).commit();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AcceptMeetingTask.acceptMeeting(meeting.getId(), false, mContext);

                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment()).commit();
                    }
                })
                .setNeutralButton(R.string.view, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showMeetingDetails(meeting);

                    }
                })
                .create();
    }



    private AlertDialog createFriendDialog(){
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO change idFriend to dynamic!
                        Integer idFriend = 1;
                        AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest(State.getLoggedUserId(),
                                idFriend, true);
                        HttpClient.acceptInviationFromFriend(acceptFriendRequest);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO change idFriend to dynamic!
                        Integer idFriend = 1;
                        AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest(State.getLoggedUserId(),
                                idFriend, false);
                        HttpClient.acceptInviationFromFriend(acceptFriendRequest);
                    }
                })
                .create();
    }

    private void showMeetingDetails(final Meeting meeting){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        ViewMeetingFragment detailsMeetingFragment = new ViewMeetingFragment(mContext, false);
        detailsMeetingFragment.setArguments(details);

        fragmentTransaction.replace(R.id.container_body, detailsMeetingFragment).commit();
    }
}
