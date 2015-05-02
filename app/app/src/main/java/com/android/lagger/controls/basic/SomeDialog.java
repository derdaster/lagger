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
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.android.lagger.services.MeetingService;
import com.google.gson.JsonObject;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class SomeDialog extends DialogFragment {

    String title;
    String message;
    Boolean isMeetingInvitation;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Context mContext;
    private Meeting meeting;

    public SomeDialog() {

    }

    public SomeDialog(Context inContext, String inTitle, String inMessage, Boolean inIsMeetingInvitation) {
        title = inTitle;
        message = inMessage;
        mContext = inContext;
        isMeetingInvitation = inIsMeetingInvitation;
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
        meeting = extras.getParcelable("meeting");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MeetingService.acceptMeeting(meeting.getId(), true);

                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment()).commit();
                        showInfo(true);

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MeetingService.acceptMeeting(meeting.getId(), false);

                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, new MeetingListFragment()).commit();
                        showInfo(false);
                    }
                })
                .setNeutralButton(R.string.view, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showMeetingDetails();
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
                        //TODO
                        //acceptFriend(true);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                       // acceptFriend(false);
                    }
                })
                .create();
    }

    private void showMeetingDetails(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);

        Bundle details = new Bundle();
        details.putParcelable("meeting", meeting);

        ViewMeetingFragment detailsMeetingFragment = new ViewMeetingFragment(mContext, false);
        detailsMeetingFragment.setArguments(details);

        fragmentTransaction.replace(R.id.container_body, detailsMeetingFragment).commit();
    }


    private  void showInfo(final boolean isAccepted){
        String messageText;
        if(isAccepted){
            messageText = getResources().getString(R.string.accept_meeting);
        }
        else{
            messageText = getResources().getString(R.string.refuse_meeting);
        }
        Toast.makeText(mContext, messageText, Toast.LENGTH_SHORT).show();
    }
}
