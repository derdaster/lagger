package com.android.lagger.controls.basic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.android.lagger.R;
import com.android.lagger.forms.meetings.CreateEditMeetingFragment;
import com.android.lagger.forms.meetings.ViewMeetingFragment;

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

    public SomeDialog(Context inContext, String inTitle, String inMessage, Boolean inIsMeetingInvitation)
    {
        title = inTitle;
        message = inMessage;
        mContext = inContext;
        isMeetingInvitation = inIsMeetingInvitation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        if(isMeetingInvitation)
        {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do something
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing (will close dialog)
                        }
                    })
                    .setNeutralButton(R.string.view, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.replace(R.id.container_body, new ViewMeetingFragment()).commit();
                        }
                    })
                    .create();
        }
        else
        {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(R.string.yes,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do something
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing (will close dialog)
                        }
                    })
                    .create();
        }
    }
}
