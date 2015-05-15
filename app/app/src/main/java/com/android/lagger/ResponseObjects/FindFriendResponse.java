package com.android.lagger.responseObjects;

import com.android.lagger.model.entities.User;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-11.
 */
public class FindFriendResponse extends ResponseObject{
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        //FIXME only for test
        StringBuilder sb = new StringBuilder();
        for(User user: users){
            sb.append(user.getEmail());
            sb.append(",");
        }
        return sb.toString();
    }
}
