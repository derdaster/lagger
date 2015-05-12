package com.android.lagger.model;

/**
 * Created by Ewelina Klisowska on 2015-05-12.
 */
public class AdapterUser {
    private Integer id;
    private String email;

    public AdapterUser(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return email;
    }
}
