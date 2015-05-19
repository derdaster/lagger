package com.android.lagger.responseObjects;

import com.android.lagger.model.entities.User;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-18.
 */
public class GetFriendsResponse extends ResponseObject {
    private List<User> friends;

    public GetFriendsResponse() {
    }

    public GetFriendsResponse(List<User> friends) {
        this.friends = friends;
    }

    public List<User> getFriends() {
        return friends;
    }
}
