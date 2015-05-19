package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-11.
 */
public class FindFriendRequest extends UserRequest {
    private String pattern;

    public FindFriendRequest(Integer idUser, String pattern) {
        super(idUser);
        this.pattern = pattern;
    }
}
