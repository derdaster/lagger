package com.android.lagger.controls.basic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.lagger.R;
import com.android.lagger.forms.friends.FriendsListFragment;
import com.android.lagger.forms.meetings.MeetingListFragment;
import com.android.lagger.forms.meetings.ViewMeetingFragment;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.model.entities.User;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.AcceptMeetingTask;
import com.android.lagger.tasks.AddFriendTask;
import com.android.lagger.tasks.RemoveFriendTask;
import com.android.lagger.tasks.RemoveMeetingTask;

/**
 * Created by Kubaa on 2015-05-27.
 */
public class InvitationDialog extends DialogFragment {

    private String title;
    private String message;
    private String dialogType;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Context mContext;
    public static final String MEETING_INVITATION_TYPE = "meetingInvitation";
    public static final String FRIEND_INVITATION_TYPE = "friendInvitation";


    public InvitationDialog() {
    }

    public InvitationDialog(Context inContext, String inTitle, String inMessage, String inDialogType) {
        title = inTitle;
        message = inMessage;
        mContext = inContext;
        dialogType = inDialogType;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        Dialog dialog = null;
        switch (dialogType) {
            case MEETING_INVITATION_TYPE:
                dialog = createInvitationMeetingDialog();
                break;
            case FRIEND_INVITATION_TYPE:
                dialog = createInvitationFriendDialog();
                break;
        }
        return dialog;
    }

    private AlertDialog createInvitationMeetingDialog() {
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
                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment(mContext)).commit();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AcceptMeetingTask.acceptMeeting(meeting.getId(), false, mContext);

                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment(mContext)).commit();
                    }
                })
                .setNeutralButton(R.string.view, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showMeetingDetails(meeting, false);

                    }
                })
                .create();
    }

    private AlertDialog createInvitationFriendDialog() {
        Bundle extras = getArguments();
        final User friend = extras.getParcelable("friend");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        com.android.lagger.requestObjects.AddFriendRequest addFriendRequest = new com.android.lagger.requestObjects.AddFriendRequest(State.getLoggedUserId(), friend.getId());
                        AddFriendTask addFriendTask = new AddFriendTask(mContext, false);
                        addFriendTask.execute(addFriendRequest);

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new FriendsListFragment(mContext)).commit();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFriend(friend, true);
                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new FriendsListFragment(mContext)).commit();
                    }
                })
                .create();
    }


    private void deleteFriend(User friend, Boolean isInvitation) {
        com.android.lagger.requestObjects.RemoveFriendRequest removeFriendRequest = new com.android.lagger.requestObjects.RemoveFriendRequest(State.getLoggedUserId(), friend.getId());

        RemoveFriendTask removeFriendTask = new RemoveFriendTask(mContext, isInvitation);
        removeFriendTask.execute(removeFriendRequest);
    }

    private void showMeetingDetails(final Meeting meeting, Boolean isReadonly) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        ViewMeetingFragment detailsMeetingFragment = new ViewMeetingFragment(mContext, isReadonly);
        detailsMeetingFragment.setArguments(details);

        fragmentTransaction.replace(R.id.container_body, detailsMeetingFragment).commit();
    }
}