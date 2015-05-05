package com.android.lagger.services;

import android.os.AsyncTask;

import com.android.lagger.requestObjects.AcceptFriendRequest;
import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.requestObjects.InviteFriendRequest;
import com.android.lagger.requestObjects.LoginRequest;
import com.android.lagger.requestObjects.RequestObject;
import com.android.lagger.requestObjects.UserRequest;
import com.android.lagger.responseObjects.LoginResponse;
import com.android.lagger.responseObjects.MeetingsResponse;
import com.android.lagger.responseObjects.ResponseObject;
import com.android.lagger.serverConnection.GsonHelper;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.google.gson.Gson;

/**
 * Created by Ewelina Klisowska on 2015-05-04.
 */
public class HttpClient {
    private Gson gson;

    public HttpClient() {
        gson = new GsonHelper().getGson();
    }

    public ResponseObject acceptMeeting(final AcceptMeetingRequest acceptMeetingReq){
        ResponseObject resp = null;

        String response = HttpRequest.POST(URL.ACCEPT_MEETING_INVITATION, acceptMeetingReq);
        resp = gson.fromJson(response, ResponseObject.class);

        return resp;
    }

//FIXME
    public static ResponseObject inviteFriend(final InviteFriendRequest inviteFriendRequest) {
        return new ResponseObject();//getPostRespFromAsyncTask(URL.INVITE_FRIEND, inviteFriendRequest);
    }

    //FIXME
    public static ResponseObject acceptInviationFromFriend(final AcceptFriendRequest acceptFriendRequest) {
        return  new ResponseObject();//getPostRespFromAsyncTask(URL.ACCEPT_FRIEND, acceptFriendRequest);
    }

    public LoginResponse login(final LoginRequest loginReq) {
        LoginResponse loginResp = null;

        String response = HttpRequest.POST(URL.LOGIN, loginReq);
        loginResp = gson.fromJson(response, LoginResponse.class);

        return loginResp;
    }

}
