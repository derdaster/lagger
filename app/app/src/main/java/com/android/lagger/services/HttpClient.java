package com.android.lagger.services;

import com.android.lagger.requestObjects.AcceptFriendRequest;
import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.requestObjects.AddFriendRequest;
import com.android.lagger.requestObjects.FindFriendRequest;
import com.android.lagger.requestObjects.LoginRequest;
import com.android.lagger.requestObjects.RemoveFriendRequest;
import com.android.lagger.responseObjects.AcceptMeetingResponse;
import com.android.lagger.responseObjects.AddFriendResponse;
import com.android.lagger.responseObjects.FindFriendResponse;
import com.android.lagger.responseObjects.LoginResponse;
import com.android.lagger.responseObjects.RemoveFriendResponse;
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

    public AcceptMeetingResponse acceptMeeting(final AcceptMeetingRequest acceptMeetingReq){
        AcceptMeetingResponse resp = null;

        String response = HttpRequest.POST(URL.ACCEPT_MEETING_INVITATION, acceptMeetingReq);
        resp = gson.fromJson(response, AcceptMeetingResponse.class);

        return resp;
    }

    public FindFriendResponse findFriends(final FindFriendRequest findFriendRequest){
        FindFriendResponse resp = null;

        String response = HttpRequest.POST(URL.FIND_FRIENDS, findFriendRequest);
        resp = gson.fromJson(response, FindFriendResponse.class);
        //TODO implement Method
        return resp;
    }


    public AddFriendResponse addFriend(final AddFriendRequest addFriendRequest) {
        AddFriendResponse resp = null;

        String response = HttpRequest.POST(URL.ADD_FRIEND, addFriendRequest);
        resp = gson.fromJson(response, AddFriendResponse.class);

        return resp;
    }

    public RemoveFriendResponse removeFriend(final RemoveFriendRequest removeFriendRequest) {
        RemoveFriendResponse resp = null;
        String response = HttpRequest.POST(URL.REMOVE_FRIEND, removeFriendRequest);
        resp = gson.fromJson(response, RemoveFriendResponse.class);

        return resp;
    }

    public LoginResponse login(final LoginRequest loginReq) {
        LoginResponse loginResp = null;

        String response = HttpRequest.POST(URL.LOGIN, loginReq);
        loginResp = gson.fromJson(response, LoginResponse.class);

        return loginResp;
    }

}
