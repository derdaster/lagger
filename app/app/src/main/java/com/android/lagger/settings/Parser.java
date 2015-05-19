package com.android.lagger.settings;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ewelina Klisowska on 2015-05-15
 */
public class Parser {
    public static String parseDate(final Date date) {
        String parsedDate = "";
        SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        parsedDate = form.format(date);

        return parsedDate;
    }

}
