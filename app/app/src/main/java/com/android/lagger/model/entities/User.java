package com.android.lagger.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ewelina Klisowska on 2015-03-19.
 */

public class User implements Parcelable {

    private int id;
    private String login;
    private String password;
    private String email;
    private String phone;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEquals = false;
        if (object != null && object instanceof User) {
            User user = (User) object;
            isEquals = id == user.getId();
        }
        return isEquals;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(phone);

    }

    private User(Parcel in) {
        id = in.readInt();
        login = in.readString();
        password = in.readString();
        email = in.readString();
        phone = in.readString();
    }

}
