package com.android.lagger.settings;

import com.android.lagger.model.entities.User;

import java.util.Locale;

/**
 * Created by Ewelina Klisowska on 2015-04-09.
 */
public class State {
    private static final Integer DEFAULT_USER_ID = 1;

    private static User loggedUser = null;
    private static final User defaultUser = new User(DEFAULT_USER_ID);

    public static final Double DEFAULT_LATITUDE = 51.1078852;
    public static final Double DEFAULT_LONGITUDE = 17.0385376;

    private static final String APP_LANGUAGE = Locale.getDefault().getLanguage();
    private static final String EN_LANGUAGE = "en";
    private static final String PL_LANGUAGE = "pl";

    //returns logged user or default user if nobody is logged
    public static User getLoggedUser() {
        return (loggedUser != null ? loggedUser : defaultUser);
    }

    public static int getLoggedUserId() {
        return getLoggedUser().getId();
    }

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }


    public static String getAppLang() {
        return APP_LANGUAGE;
    }

    public static boolean isEngLang() {
        return APP_LANGUAGE.equals(EN_LANGUAGE);
    }

    public static boolean isPlLang() {
        return APP_LANGUAGE.equals(PL_LANGUAGE);
    }

}
