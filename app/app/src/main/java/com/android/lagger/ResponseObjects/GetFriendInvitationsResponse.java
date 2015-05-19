package com.android.lagger.responseObjects;

import com.android.lagger.model.entities.User;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-17.
 */
public class GetFriendInvitationsResponse extends ResponseObject {
    private List<User> friendInvitations;

    public GetFriendInvitationsResponse() {
    }

    public GetFriendInvitationsResponse(List<User> friendInvitations) {
        this.friendInvitations = friendInvitations;
    }

    public List<User> getFriendInvitations() {
        return friendInvitations;
    }
}
