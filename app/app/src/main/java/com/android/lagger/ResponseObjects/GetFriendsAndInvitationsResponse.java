package com.android.lagger.responseObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-18.
 */
public class GetFriendsAndInvitationsResponse extends ResponseObject {
    private GetFriendInvitationsResponse getFriendInvitationsResponse;
    private GetFriendsResponse getFriendsResponse;

    public GetFriendsAndInvitationsResponse() {
    }

    public GetFriendsAndInvitationsResponse(GetFriendInvitationsResponse getFriendInvitationsResponse, GetFriendsResponse getFriendsResponse) {
        this.getFriendInvitationsResponse = getFriendInvitationsResponse;
        this.getFriendsResponse = getFriendsResponse;
    }

    public GetFriendInvitationsResponse getGetFriendInvitationsResponse() {
        return getFriendInvitationsResponse;
    }

    public GetFriendsResponse getGetFriendsResponse() {
        return getFriendsResponse;
    }
}
