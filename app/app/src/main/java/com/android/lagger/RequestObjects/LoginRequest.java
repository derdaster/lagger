package com.android.lagger.RequestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class LoginRequest extends RequestObject{
    private String login;
    private String password;

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
