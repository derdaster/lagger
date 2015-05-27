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
public class RefuseDialog extends DialogFragment {

    private String title;
    private String message;
    private String dialogType;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Context mContext;


    public RefuseDialog() {
    }

    public RefuseDialog(Context inContext, String inTitle, String inMessage) {
        title = inTitle;
        message = inMessage;
        mContext = inContext;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        return refuseMeetingDialog();
    }

    private AlertDialog refuseMeetingDialog() {
        Bundle extras = getArguments();
        final Meeting meeting = extras.getParcelable("meeting");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AcceptMeetingTask.acceptMeeting(meeting.getId(), false, mContext);

                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment(mContext)).commit();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
    }
}