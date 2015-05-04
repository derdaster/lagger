package com.android.lagger.services;

import android.os.AsyncTask;

import com.android.lagger.requestObjects.AcceptFriendRequest;
import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.requestObjects.InviteFriendRequest;
import com.android.lagger.requestObjects.LoginRequest;
import com.android.lagger.requestObjects.RequestObject;
import com.android.lagger.responseObjects.LoginResponse;
import com.android.lagger.responseObjects.ResponseObject;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;

/**
 * Created by Ewelina Klisowska on 2015-05-04.
 */
public class HttpClient {
    //FIXME non static object and non static methods
    private static ResponseObject responseObj = null;

    public static ResponseObject acceptMeeting(final AcceptMeetingRequest acceptMeetingRequest){
        return getPostRespFromAsyncTask(URL.ACCEPT_MEETING_INVITATION,
                acceptMeetingRequest);
    }

    public static ResponseObject inviteFriend(final InviteFriendRequest inviteFriendRequest) {
        return getPostRespFromAsyncTask(URL.INVITE_FRIEND, inviteFriendRequest);
    }

    public static ResponseObject acceptInviationFromFriend(final AcceptFriendRequest acceptFriendRequest) {
        return getPostRespFromAsyncTask(URL.ACCEPT_FRIEND, acceptFriendRequest);
    }

    public static LoginResponse login(final LoginRequest loginReq) {
        return (LoginResponse) getPostRespFromAsyncTask(URL.LOGIN, loginReq);
    }



    private static ResponseObject getPostRespFromAsyncTask(final String URL,
                                                           final RequestObject requestObj){
        new AsyncTask<String, Void, ResponseObject>() {
            protected ResponseObject doInBackground(String... urls) {
                return  HttpRequest.POST(URL, requestObj);
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
