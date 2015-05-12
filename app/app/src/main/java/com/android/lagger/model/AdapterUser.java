package com.android.lagger.model;

/**
 * Created by Ewelina Klisowska on 2015-05-12.
 */
public class AdapterUser {
    private Integer id;
    private String email;
    private String login;

    public AdapterUser(Integer id, String email, String login) {
        this.id = id;
        this.email = email;
        this.login = login;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(login);
        sb.append(" (");
        sb.append(email);
        sb.append(")");

        return sb.toString() ;
    }
}
