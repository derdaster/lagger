package com.android.lagger.services;

import android.os.AsyncTask;

import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.google.gson.JsonObject;

/**
 * Created by Ewelina Klisowska on 2015-04-30.
 */
public class MeetingService {

    public static void acceptMeeting(final Integer meetingId, final boolean isAccepted){

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                JsonObject invitationAcceptJson = new JsonObject();
                //FIXME change idUser to dynamic
                invitationAcceptJson.addProperty("idUser", 1);
                invitationAcceptJson.addProperty("idMeeting", meetingId);
                invitationAcceptJson.addProperty("accept", isAccepted);
//              Fixme REFACTOR
//                AcceptMeetingRequest acceptMeetingRequest = new AcceptMeetingRequest(1, meetingId, isAccepted);

                return  HttpRequest.POST(URL.ACCEPT_MEETING_INVITATION, invitationAcceptJson);
            }

            @Override
            protected void onPostExecute(String result) {
//                dismiss();
//                showInfo(isAccepted);
            }
        }.execute();
        //FIXME pokazywanie toastu
//        showInfo(isAccepted);
    }

}
