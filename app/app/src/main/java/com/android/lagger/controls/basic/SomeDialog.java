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
import com.android.lagger.forms.friends.FriendsListFragment;
import com.android.lagger.forms.meetings.MeetingListFragment;
import com.android.lagger.forms.meetings.ViewMeetingFragment;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.model.entities.User;
import com.android.lagger.requestObjects.AcceptFriendRequest;
import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.requestObjects.LoginRequest;
import com.android.lagger.requestObjects.RemoveFriendRequest;
import com.android.lagger.responseObjects.LoginResponse;
import com.android.lagger.responseObjects.ResponseObject;
import com.android.lagger.services.HttpClient;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.AcceptMeetingTask;
import com.android.lagger.tasks.RemoveFriendTask;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class SomeDialog extends DialogFragment {

    private String title;
    private String message;
    private String dialogType;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Context mContext;
    public static final String MEETING_INVITATION_TYPE =  "meetingInvitation";
    public static final String MEETING_TYPE =  "meeting";
    public static final String FRIEND_TYPE =  "friend";
    public static final String FRIEND_INVITATION_TYPE =  "friendInvitation";

    public SomeDialog() {
    }

    public SomeDialog(Context inContext, String inTitle, String inMessage, String inDialogType) {
        title = inTitle;
        message = inMessage;
        mContext = inContext;
        dialogType = inDialogType;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        Dialog dialog = null;
        switch (dialogType)
        {
            case MEETING_INVITATION_TYPE:
                dialog = createInvitationMeetingDialog();
                break;
            case MEETING_TYPE:
                dialog = createMeetingDialog();
                break;
            case FRIEND_TYPE:
                dialog = createFriendDialog();
                break;
            case FRIEND_INVITATION_TYPE:
                //TODO implement, change!
                dialog = createFriendDialog();
                break;
        }
        return dialog;
    }

    private AlertDialog createInvitationMeetingDialog(){
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
    private AlertDialog createMeetingDialog(){
        Bundle extras = getArguments();
        final Meeting meeting = extras.getParcelable("meeting");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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


    //if delete friend
    private AlertDialog createFriendDialog(){
        Bundle extras = getArguments();
        final User friend = extras.getParcelable("friend");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    deleteFriend(friend, true);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    deleteFriend(friend, false);
                    }
                })
                .create();
    }

    private void deleteFriend(User friend, Boolean delete){
        if(delete){
            RemoveFriendRequest removeFriendRequest = new RemoveFriendRequest(State.getLoggedUserId(), friend.getId());

            RemoveFriendTask removeFriendTask = new RemoveFriendTask(mContext);
            removeFriendTask.execute(removeFriendRequest);

            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_body, new FriendsListFragment()).commit();
        }

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
