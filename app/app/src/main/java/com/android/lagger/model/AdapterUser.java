package com.android.lagger.model;

import com.android.lagger.model.entities.User;

/**
 * Created by Ewelina Klisowska on 2015-05-12.
 */
public class AdapterUser {
    private Integer id;
    private String email;
    private String login;

    public AdapterUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.login = user.getLogin();
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {  return login; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(login);
        sb.append(" (");
        sb.append(email);
        sb.append(")");

        return sb.toString() ;
    }
}
