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
import com.android.lagger.forms.meetings.ViewMeetingFragment;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
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
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        acceptMeeting(true);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        acceptMeeting(false);
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
        fragmentTransaction.replace(R.id.container_body, new ViewMeetingFragment()).commit();
    }

    private void acceptMeeting(final boolean isAccepted){
        final int id = getArguments().getInt("id");

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                JsonObject invitationAcceptJson = new JsonObject();
                //FIXME change idUser to dynamic
                invitationAcceptJson.addProperty("idUser", 1);
                invitationAcceptJson.addProperty("idMeeting", id);
                invitationAcceptJson.addProperty("accept", isAccepted);

                return  HttpRequest.POST(URL.ACCEPT_MEETING_INVITATION_URL, invitationAcceptJson);
            }

            @Override
            protected void onPostExecute(String result) {
//                dismiss();
//                showInfo(isAccepted);
            }
        }.execute();
        showInfo(isAccepted);
    }

    private void showInfo(final boolean isAccepted){
        String messageText;
        if(isAccepted){
            messageText = getResources().getString(R.string.accept_meeting);
        }
        else{
            messageText = getResources().getString(R.string.refuse_meeting);
        }
        Toast.makeText(mContext,messageText, Toast.LENGTH_SHORT).show();
    }
}
