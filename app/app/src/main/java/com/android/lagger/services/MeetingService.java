package com.android.lagger.services;

import android.os.AsyncTask;

import com.android.lagger.requestObjects.RequestObject;
import com.android.lagger.responseObjects.ResponseObject;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.android.lagger.settings.State;
import com.google.gson.JsonObject;

/**
 * Created by Ewelina Klisowska on 2015-04-30.
 */
public class MeetingService {
    //FIXME non static object and non static methods
    private static ResponseObject responseObj = null;

    public static ResponseObject acceptMeeting(final RequestObject requestObj){

        new AsyncTask<String, Void, ResponseObject>() {

            protected ResponseObject doInBackground(String... urls) {
                return  HttpRequest.POST(URL.ACCEPT_MEETING_INVITATION, requestObj);
            }

            @Override
            protected void onPostExecute(ResponseObject result) {
                responseObj = result;
//                dismiss();
//                showInfo(isAccepted);
            }
        }.execute();
        //FIXME pokazywanie toastu
//        showInfo(isAccepted);
        return responseObj;
    }

}
