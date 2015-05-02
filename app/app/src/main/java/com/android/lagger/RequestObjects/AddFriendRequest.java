package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class AddFriendRequest extends UserRequest{
    //FIXME change in serwer;
    private String email;

    public AddFriendRequest(Integer idUser, String email) {
        super(idUser);
        this.email = email;
    }
}
