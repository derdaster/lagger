package com.android.lagger.settings;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Ewelina Klisowska on 2015-05-15
 */
public class Parser {
    public static String parseDate(final Date date) {
        String parsedDate = "";
        SimpleDateFormat sdf = null;

        if (State.isEngLang()) {
            //EN
            sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm z");
            parsedDate = sdf.format(date);
        } else {
            //PL

            sdf = new SimpleDateFormat("EEE MMM dd, yyyy H:mm a");
            DateTime dt = new DateTime(date);
            DateTimeZone dtZone = DateTimeZone.forID("Europe/Warsaw");
            DateTime dtus = dt.withZone(dtZone);
            TimeZone tzInPoland = dtZone.toTimeZone();
            Date dateInPoland = dtus.toLocalDateTime().toDate(); //Convert to LocalDateTime first

//            sdf.setTimeZone(tzInPoland);

            GregorianCalendar gc = new GregorianCalendar(new Locale("pl", "PL"));
            gc.setTime(dateInPoland);
//            gc.setTimeZone(tzInPoland);
//            parsedDate = dateInPoland;//sdf.format(gc.getTime());
            parsedDate = sdf.format(dateInPoland);
        }
        return parsedDate;
    }

    public static String parseDates(Date startDate, Date endDate){
        String parseDate = "";

        return parseDate;
    }

}
