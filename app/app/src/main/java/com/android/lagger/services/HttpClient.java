package com.android.lagger.services;

import android.content.Context;

import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.requestObjects.AddFriendRequest;
import com.android.lagger.requestObjects.FindFriendRequest;
import com.android.lagger.requestObjects.GetAllMeetingsRequest;
import com.android.lagger.requestObjects.GetMeetingInvitationsRequest;
import com.android.lagger.requestObjects.GetMeetingsRequest;
import com.android.lagger.requestObjects.LoginRequest;
import com.android.lagger.requestObjects.RemoveFriendRequest;
import com.android.lagger.requestObjects.RemoveMeetingRequest;
import com.android.lagger.responseObjects.AcceptMeetingResponse;
import com.android.lagger.responseObjects.AddFriendResponse;
import com.android.lagger.responseObjects.FindFriendResponse;
import com.android.lagger.responseObjects.GetAllMeetingsResponse;
import com.android.lagger.responseObjects.GetMeetingInvitationsResponse;
import com.android.lagger.responseObjects.GetMeetingsResponse;
import com.android.lagger.responseObjects.LoginResponse;
import com.android.lagger.responseObjects.RemoveFriendResponse;
import com.android.lagger.responseObjects.RemoveMeetingResponse;
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
    private HttpRequest httpRequest;

    public HttpClient(Context context) {
        gson = new GsonHelper().getGson();
        httpRequest = new HttpRequest(context);
    }

    public AcceptMeetingResponse acceptMeeting(final AcceptMeetingRequest acceptMeetingReq) {
        AcceptMeetingResponse resp = null;

        ResponseObject responseObj = httpRequest.POST(URL.ACCEPT_MEETING_INVITATION, acceptMeetingReq);
        String response = responseObj.getResponse();

        if (!responseObj.isError()) {
            resp = gson.fromJson(response, AcceptMeetingResponse.class);
            resp.setIsError(false);
        } else {
            resp = new AcceptMeetingResponse();
            resp.setResponse(response);
            resp.setIsError(true);
        }
        return resp;
    }

    public FindFriendResponse findFriends(final FindFriendRequest findFriendRequest) {
        FindFriendResponse resp = null;

        ResponseObject responseObj = httpRequest.POST(URL.FIND_FRIENDS, findFriendRequest);
        String response = responseObj.getResponse();

        if (!responseObj.isError()) {
            resp = gson.fromJson(response, FindFriendResponse.class);
            resp.setIsError(false);
        } else {
            resp = new FindFriendResponse();
            resp.setResponse(response);
            resp.setIsError(true);
        }

        return resp;
    }


    public AddFriendResponse addFriend(final AddFriendRequest addFriendRequest) {
        AddFriendResponse resp = null;

        ResponseObject responseObj = httpRequest.POST(URL.ADD_FRIEND, addFriendRequest);
        String response = responseObj.getResponse();

        if (!responseObj.isError()) {
            resp = gson.fromJson(response, AddFriendResponse.class);
            resp.setIsError(false);
        } else {
            resp = new AddFriendResponse();
            resp.setResponse(response);
            resp.setIsError(true);
        }
        return resp;
    }

    public RemoveFriendResponse removeFriend(final RemoveFriendRequest removeFriendRequest) {
        RemoveFriendResponse resp = null;

        ResponseObject responseObj = httpRequest.POST(URL.REMOVE_FRIEND, removeFriendRequest);
        String response = responseObj.getResponse();

        if (!responseObj.isError()) {
            resp = gson.fromJson(response, RemoveFriendResponse.class);
            resp.setIsError(false);
        } else {
            resp = new RemoveFriendResponse();
            resp.setResponse(response);
            resp.setIsError(true);
        }

        return resp;
    }

    public RemoveMeetingResponse removeMeeting(final RemoveMeetingRequest removeMeetingRequest) {
        RemoveMeetingResponse resp = null;

        ResponseObject responseObj = httpRequest.POST(URL.REMOVE_MEETING, removeMeetingRequest);
        String response = responseObj.getResponse();

        if (!responseObj.isError()) {
            resp = gson.fromJson(response, RemoveMeetingResponse.class);
            resp.setIsError(false);
        } else {
            resp = new RemoveMeetingResponse();
            resp.setResponse(response);
            resp.setIsError(true);
        }

        return resp;
    }

    public LoginResponse login(final LoginRequest loginReq) {
        LoginResponse resp = null;

        ResponseObject responseObj = httpRequest.POST(URL.LOGIN, loginReq);
        String response = responseObj.getResponse();

        if (!responseObj.isError()) {
            resp = gson.fromJson(response, LoginResponse.class);
            resp.setIsError(false);
        } else {
            resp = new LoginResponse();
            resp.setResponse(response);
            resp.setIsError(true);
        }

        return resp;
    }

    public GetAllMeetingsResponse getAllMeetings(final GetAllMeetingsRequest request) {
        GetAllMeetingsResponse respAllMeetings = null;

        GetMeetingsResponse meetingsResp = getMeetings(request.getGetMeetingsRequest());
        GetMeetingInvitationsResponse invitationsResp = getMeetingInvitations(request.getGetMeetingInvitationsRequest());

        if (!meetingsResp.isError() && !invitationsResp.isError()) {
            respAllMeetings = new GetAllMeetingsResponse(meetingsResp, invitationsResp);
            respAllMeetings.setIsError(false);
        } else {
            //get response errors
            respAllMeetings = new GetAllMeetingsResponse(null, null);
            String responseMeetings = meetingsResp.isError() ? meetingsResp.getResponse() : "";
            String responseInvitations = invitationsResp.isError() ? invitationsResp.getResponse() : "";

            String response = responseMeetings;
            if(!responseMeetings.equals(responseInvitations)) {
                response += "\n" + responseInvitations;
            }

            respAllMeetings.setResponse(response);
            respAllMeetings.setIsError(true);
        }

        return respAllMeetings;
    }

    private GetMeetingsResponse getMeetings(final GetMeetingsRequest meetingsReq) {
        GetMeetingsResponse resp = null;

        ResponseObject responseMeetingsObj = httpRequest.POST(URL.GET_MEETINGS, meetingsReq);
        String response = responseMeetingsObj.getResponse();

        if (!responseMeetingsObj.isError()) {
            resp = gson.fromJson(response, GetMeetingsResponse.class);
            resp.setIsError(false);
        } else {
            resp = new GetMeetingsResponse(null);
            resp.setResponse(response);
            resp.setIsError(true);
        }

        return resp;

    }

    private GetMeetingInvitationsResponse getMeetingInvitations(final GetMeetingInvitationsRequest invitationsReq) {
        GetMeetingInvitationsResponse resp = null;

        ResponseObject respInvitationsObj = httpRequest.POST(URL.GET_INVITATIONS, invitationsReq);
        String response = respInvitationsObj.getResponse();

        if (!respInvitationsObj.isError()) {
            resp = gson.fromJson(response, GetMeetingInvitationsResponse.class);
            resp.setIsError(false);
        } else {
            resp = new GetMeetingInvitationsResponse(null);
            resp.setResponse(response);
            resp.setIsError(true);
        }

        return resp;
    }
}
