package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class InviteFriendRequest extends UserRequest{
    //FIXME change in serwer;
    private String email;

    public InviteFriendRequest(Integer idUser, String email) {
        super(idUser);
        this.email = email;
    }
}
