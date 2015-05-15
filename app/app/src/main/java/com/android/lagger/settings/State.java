package com.android.lagger.settings;

import android.content.Context;
import android.net.ConnectivityManager;

import com.android.lagger.model.entities.User;

/**
 * Created by Ewelina Klisowska on 2015-04-09.
 */
public class State {
    private static final Integer DEFAULT_USER_ID = 1;

    private static User loggedUser = null;
    private static final User defaultUser = new User(DEFAULT_USER_ID);


    //returns logged user or default user if nobody is logged
    public static User getLoggedUser(){
        return (loggedUser != null ? loggedUser : defaultUser);
    }

    public static int getLoggedUserId(){
        return getLoggedUser().getId();
    }

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }

    private boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()) {
            return true;
        } else {

            return false;
        }
    }
}
