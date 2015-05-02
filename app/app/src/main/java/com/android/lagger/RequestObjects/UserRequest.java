package com.android.lagger.RequestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class UserRequest extends RequestObject{
    private Integer idUser;

    public UserRequest(Integer idUser) {
        this.idUser = idUser;
    }
}
