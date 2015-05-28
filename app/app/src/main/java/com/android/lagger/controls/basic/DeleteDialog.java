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
public class DeleteDialog extends DialogFragment {

    private String title;
    private String message;
    private String dialogType;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Context mContext;
    public static final String MEETING_DELETE_TYPE = "meeting";
    public static final String FRIEND_DELETE_TYPE = "friend";



    public DeleteDialog() {
    }

    public DeleteDialog(Context inContext, String inTitle, String inMessage, String inDialogType) {
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
            case MEETING_DELETE_TYPE:
                dialog = deleteMeetingDialog();
                break;
            case FRIEND_DELETE_TYPE:
                dialog = deleteFriendDialog();
                break;
        }
        return dialog;
    }


    private AlertDialog deleteMeetingDialog() {
        Bundle extras = getArguments();
        final Meeting meeting = extras.getParcelable("meeting");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        com.android.lagger.requestObjects.RemoveMeetingRequest removeMeetingRequest = new com.android.lagger.requestObjects.RemoveMeetingRequest(State.getLoggedUserId(), meeting.getId());
                        RemoveMeetingTask removeMeetingTask = new RemoveMeetingTask(mContext);
                        removeMeetingTask.execute(removeMeetingRequest);

                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment(mContext)).commit();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        fragmentTransaction = getFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment()).commit();
                    }
                })
                .create();
    }


    private AlertDialog deleteFriendDialog() {
        Bundle extras = getArguments();
        final User friend = extras.getParcelable("friend");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFriend(friend, false);
                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new FriendsListFragment(mContext)).commit();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    private void deleteFriend(User friend, Boolean isInvitation) {
        com.android.lagger.requestObjects.RemoveFriendRequest removeFriendRequest = new com.android.lagger.requestObjects.RemoveFriendRequest(State.getLoggedUserId(), friend.getId());

        RemoveFriendTask removeFriendTask = new RemoveFriendTask(mContext, isInvitation);
        removeFriendTask.execute(removeFriendRequest);
    }
}