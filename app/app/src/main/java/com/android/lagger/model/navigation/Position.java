package com.android.lagger.model.navigation;

import java.util.Date;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class Position {
    private Date dateTime;
    private Double latitude;
    private Double longitude;

    public Position(Date dateTime, Double latitude, Double longitude) {
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
