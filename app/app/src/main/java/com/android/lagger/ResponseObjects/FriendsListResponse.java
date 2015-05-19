package com.android.lagger.responseObjects;

import com.android.lagger.model.entities.User;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class FriendsListResponse extends ResponseObject {
    private List<User> friends;

    public FriendsListResponse(List<User> friends) {
        this.friends = friends;
    }
}
