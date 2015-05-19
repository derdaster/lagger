package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-18.
 */
public class GetFriendsAndInvitationsRequest extends RequestObject {
    private GetFriendInvitationsRequest getFriendInvitationsRequest;
    private GetFriendsRequest getFriendsRequest;

    public GetFriendsAndInvitationsRequest(GetFriendInvitationsRequest getFriendInvitationsRequest,
                                           GetFriendsRequest getFriendsRequest) {
        this.getFriendInvitationsRequest = getFriendInvitationsRequest;
        this.getFriendsRequest = getFriendsRequest;
    }

    public GetFriendInvitationsRequest getGetFriendInvitationsRequest() {
        return getFriendInvitationsRequest;
    }

    public GetFriendsRequest getGetFriendsRequest() {
        return getFriendsRequest;
    }
}
