package com.android.lagger.model.entities;

/**
 * Created by Kubaa on 2015-04-01.
 */
public class Meeting extends Entity {

    private String title;
    private String where;
    private String when;
    private String organizer;

    public Meeting(int inID, String inTitle, String inWhere, String inWhen, String inOrganizer) {
        super(inID);
        title = inTitle;
        when = inWhen;
        where = inWhere;
        organizer = inOrganizer;
    }

    public String getTitle() {
        return title;
    }
    public String getWhere() {
        return where;
    }
    public String getWhen() {
        return when;
    }
    public String getOrganizer() {
        return organizer;
    }
}
