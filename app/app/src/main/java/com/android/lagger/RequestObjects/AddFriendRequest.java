package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class AddFriendRequest extends UserRequest {
    private Integer idFriend;

    public AddFriendRequest(Integer idUser, Integer idFriend) {
        super(idUser);
        this.idFriend = idFriend;
    }
}
