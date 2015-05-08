package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-04.
 */
public class AcceptFriendRequest extends UserRequest {
    private Integer idFriend;
    private Boolean accept;

    public AcceptFriendRequest(Integer idUser, Integer idFriend, Boolean accept){
        super(idUser);
        this.idFriend = idFriend;
        this.accept = accept;
    }
}
